package com.face.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.face.db.PersonDAO;
import com.face.model.Person;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.security.auth.x500.X500Principal;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class DeleteFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField noTF;
	private JTextField nameTF;
	private PersonDAO pDao;
	private Person p;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Person p = new Person();
					p.setId(12);
//					DeleteFrame frame = new DeleteFrame();
//					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DeleteFrame(Person p) {
		this.p = p;
		pDao = new PersonDAO();
		setTitle("删除");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 403);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel noLB = new JLabel("编号");
		noLB.setHorizontalAlignment(SwingConstants.CENTER);
		noLB.setBounds(273, 103, 54, 15);
		contentPane.add(noLB);

		noTF = new JTextField();
		noTF.setEditable(false);
		noTF.setBounds(337, 98, 70, 25);
		contentPane.add(noTF);
		noTF.setColumns(10);
		noTF.setText(p.getPersonNo());
		
		JLabel nameLB = new JLabel("姓名");
		nameLB.setHorizontalAlignment(SwingConstants.CENTER);
		nameLB.setBounds(273, 165, 54, 15);
		contentPane.add(nameLB);

		nameTF = new JTextField();
		nameTF.setEditable(false);
		nameTF.setBounds(337, 160, 70, 25);
		contentPane.add(nameTF);
		nameTF.setColumns(10);
		nameTF.setText(p.getPersonName());
		
		JButton cancleBT = new JButton("取消");
		cancleBT.setBounds(234, 332, 93, 23);
		contentPane.add(cancleBT);
		cancleBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		JButton okBT = new JButton("删除");
		okBT.setBounds(356, 332, 93, 23);
		contentPane.add(okBT);
		okBT.addActionListener(this);
		JLabel imgLbl = new JLabel("照片");
		imgLbl.setHorizontalAlignment(SwingConstants.CENTER);
		imgLbl.setBounds(110, 288, 54, 15);
		contentPane.add(imgLbl);
		
		JLabel imgLb = new JLabel("New label");
		imgLb.setHorizontalAlignment(SwingConstants.CENTER);
		imgLb.setBounds(35, 30, 200, 250);
		ImageIcon icon  = new ImageIcon(p.getPersonImage());
		imgLb.setIcon(icon);
		contentPane.add(imgLb);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		checkPwd(p.getPersonNo());
	}

	private void close() {
		this.dispose();
	}

	private boolean delete(String no) {
		Boolean mark = pDao.deleteByNo(no);
		return mark;
	}

	private void checkPwd(String no){
		int i=3;
		String pwd;
		do
		{
			pwd = JOptionPane.showInputDialog("请输入密码(您只有三次机会)");
			if(pwd == null){
				return;
			}
			if(!pwd.equals("123456")){
				i--;	
			}else{
				int flag = JOptionPane.showConfirmDialog(null, "确认要删除");
				if (flag == 0 && delete(no)) {
					JOptionPane.showMessageDialog(null, "删除成功", "信息", JOptionPane.INFORMATION_MESSAGE);
					close();
					return;
				}else{
					JOptionPane.showMessageDialog(null, "删除失败", "警告", JOptionPane.ERROR_MESSAGE);
				}
			}
			JOptionPane.showMessageDialog(null, "您还有"+i+"次机会", "提示", JOptionPane.WARNING_MESSAGE);
		}while(i!=0);
		
	}
}
