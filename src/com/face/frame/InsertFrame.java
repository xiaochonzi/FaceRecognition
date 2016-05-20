package com.face.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.face.db.PersonDAO;
import com.face.model.Person;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class InsertFrame extends JFrame implements ActionListener,MouseListener{

	private JPanel contentPane;
	private ImagePanel imgPanel;
	private JTextField noTF;
	private JTextField nameTF;
	private JTextField levelTF;
	private JTextField dateTF;
	private JTextField cardIdTF;
	private JTextArea areaTA;
	private JButton cancelBT;
	private JButton updateBT;
	private PersonDAO pDao;
	private JLabel imgLb;
	private String imgPath;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InsertFrame frame = new InsertFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public InsertFrame() {
		pDao = new PersonDAO();
		setTitle("添加");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 665, 448);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);
		
		JLabel lblNewLabel = new JLabel("照片");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(97, 33, 79, 31);
		contentPane.add(lblNewLabel);
		
		imgLb = new JLabel("点击选择图片");
		imgLb.setHorizontalAlignment(SwingConstants.CENTER);
		imgLb.setBounds(35, 30, 200, 250);
		imgPath = null;
		ImageIcon icon  = new ImageIcon(imgPath);
		imgLb.setIcon(icon);
		imgLb.addMouseListener(this);
		contentPane.add(imgLb);
		
		JLabel label = new JLabel("编号");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(342, 55, 54, 15);
		contentPane.add(label);
		
		noTF = new JTextField();
		noTF.setBounds(427, 48, 100, 30);
		contentPane.add(noTF);
		noTF.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("姓名");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(342, 102, 54, 15);
		contentPane.add(lblNewLabel_1);
		
		nameTF = new JTextField();
		nameTF.setColumns(10);
		nameTF.setBounds(427, 95, 100, 30);
		contentPane.add(nameTF);
		
		levelTF = new JTextField();
		levelTF.setColumns(10);
		levelTF.setBounds(427, 135, 100, 30);
		contentPane.add(levelTF);
		
		dateTF = new JTextField();
		dateTF.setColumns(10);
		dateTF.setBounds(427, 175, 100, 30);
		contentPane.add(dateTF);
		
		JLabel label_1 = new JLabel("危险级别");
		label_1.setBounds(342, 142, 54, 15);
		contentPane.add(label_1);
		
		JLabel lblNewLabel_2 = new JLabel("修改时间");
		lblNewLabel_2.setBounds(342, 182, 54, 15);
		contentPane.add(lblNewLabel_2);
		
		JLabel label_2 = new JLabel("身份证号");
		label_2.setBounds(342, 222, 54, 15);
		contentPane.add(label_2);
		
		cardIdTF = new JTextField();
		cardIdTF.setColumns(10);
		cardIdTF.setBounds(427, 215, 150, 30);
		contentPane.add(cardIdTF);
		
		JLabel label_3 = new JLabel("地址");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(342, 266, 54, 15);
		contentPane.add(label_3);
		
		areaTA = new JTextArea();
		areaTA.setWrapStyleWord(true);
		areaTA.setLineWrap(true);
		areaTA.setBounds(426, 262, 151, 62);
		contentPane.add(areaTA);
		
		cancelBT = new JButton("取消");
		cancelBT.setBounds(343, 377, 93, 23);
		cancelBT.setActionCommand("cancle");
		contentPane.add(cancelBT);
		
		updateBT = new JButton("确认修改");
		updateBT.setBounds(484, 377, 93, 23);
		updateBT.setActionCommand("update");
		contentPane.add(updateBT);
		
		cancelBT.addActionListener(this);
		updateBT.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("cancle")){
			dispose();
		}else {
			System.out.println("修改");
			String no = noTF.getText();
			String name = nameTF.getText();
			String level = levelTF.getText();
			String date = dateTF.getText();
			String cardId = cardIdTF.getText();
			String area = areaTA.getText();
			String path = imgPath;
			if(no!=null && name!=null && level!=null && date!=null && cardId!=null && area!=null){
				Person p = new Person();
				p.setPersonName(name);
				p.setPersonNo(no);
				p.setPersonLevel(level);
				p.setDate(date);
				p.setPersonCardId(cardId);
				p.setPersonAddr(area);
				p.setPersonImage(path);
				checkPwd(p);
			}else{
				JOptionPane.showMessageDialog(null, "请填写完整", "警告", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	private void checkPwd(Person p){
		int i=3;
		String pwd;
		do
		{
			pwd = JOptionPane.showInputDialog("请输入密码(您只有三次机会)");
			if(!pwd.equals("123456")){
				i--;	
			}else{
				int flag = JOptionPane.showConfirmDialog(null, "确认要修改");
				if (flag == 0&& pDao.insert(p) ) {
					JOptionPane.showMessageDialog(null, "添加成功", "信息", JOptionPane.INFORMATION_MESSAGE);
					close();
					return;
				} else {
					JOptionPane.showMessageDialog(null, "添加失败", "警告", JOptionPane.ERROR_MESSAGE);
				}
			}
			JOptionPane.showMessageDialog(null, "您还有"+i+"次机会", "提示", JOptionPane.WARNING_MESSAGE);
		}while(i!=0);
	}
	private void close() {
		this.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JFileChooser jfc=new JFileChooser("E:/temp");
		//创建jpg、gif文件类型过滤器
		FileFilter filterJpeg = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
		FileFilter filterGif = new FileNameExtensionFilter("PNG file", "png");
		//删除默认的文件后缀类型过滤器
		jfc.removeChoosableFileFilter(jfc.getFileFilter());
		//为文件对话框设置后缀过滤器
		jfc.addChoosableFileFilter(filterJpeg);		
		jfc.addChoosableFileFilter(filterGif);
		//显示文件对话框
		jfc.showDialog(this,"请选择要打开的图片");
		//获取图片文件路径
		try {
			String path=jfc.getSelectedFile().getAbsolutePath();
			imgPath = path;
			//创建图标对象
			Icon icon=new ImageIcon(path);
			imgLb.setIcon(icon);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			jfc.cancelSelection();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
