package com.face.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.face.db.PersonDAO;
import com.face.model.Person;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JTree;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.management.loading.PrivateClassLoader;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SearchFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField noTF;
	private JTextField nameTF;
	private JTextField levelTF;
	private JTextField dateTF;
	private JTextField cardIdTF;
	private Person person;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PersonDAO pDao = new PersonDAO();
					SearchFrame frame = new SearchFrame(pDao.findByNo("A001"));
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
	public SearchFrame(Person p) {
		this.person = p;
		setResizable(false);
		setTitle("编号查询");
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
		
		JLabel imgLb = new JLabel();
		imgLb.setHorizontalAlignment(SwingConstants.CENTER);
		imgLb.setBounds(35, 30, 200, 250);
		String imgPath = p.getPersonImage();
		ImageIcon icon  = new ImageIcon(imgPath);
		imgLb.setIcon(icon);
		contentPane.add(imgLb);
		
		JLabel label = new JLabel("编号");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(342, 55, 54, 15);
		contentPane.add(label);
		
		noTF = new JTextField();
		noTF.setEditable(false);
		noTF.setBounds(427, 48, 100, 30);
		contentPane.add(noTF);
		noTF.setColumns(10);
		noTF.setText(p.getPersonNo());
		
		JLabel lblNewLabel_1 = new JLabel("姓名");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(342, 102, 54, 15);
		contentPane.add(lblNewLabel_1);
		
		nameTF = new JTextField();
		nameTF.setEditable(false);
		nameTF.setColumns(10);
		nameTF.setBounds(427, 95, 100, 30);
		contentPane.add(nameTF);
		nameTF.setText(p.getPersonName());
		
		levelTF = new JTextField();
		levelTF.setEditable(false);
		levelTF.setColumns(10);
		levelTF.setBounds(427, 135, 100, 30);
		contentPane.add(levelTF);
		levelTF.setText(p.getPersonLevel());
		
		dateTF = new JTextField();
		dateTF.setEditable(false);
		dateTF.setColumns(10);
		dateTF.setBounds(427, 175, 100, 30);
		contentPane.add(dateTF);
		dateTF.setText(p.getDate());
		
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
		cardIdTF.setEditable(false);
		cardIdTF.setColumns(10);
		cardIdTF.setBounds(427, 215, 150, 30);
		contentPane.add(cardIdTF);
		cardIdTF.setText(p.getPersonCardId());
		
		JLabel label_3 = new JLabel("地址");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(342, 266, 54, 15);
		contentPane.add(label_3);
		
		JTextArea areaTA = new JTextArea();
		areaTA.setEditable(false);
		areaTA.setWrapStyleWord(true);
		areaTA.setLineWrap(true);
		areaTA.setBounds(426, 262, 151, 62);
		areaTA.setText(p.getPersonAddr());
		contentPane.add(areaTA);
		
		JButton cancelBT = new JButton("取消");
		cancelBT.setBounds(343, 377, 93, 23);
		contentPane.add(cancelBT);
		cancelBT.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JButton updateBT = new JButton("修改");
		updateBT.setBounds(484, 377, 93, 23);
		contentPane.add(updateBT);
		updateBT.addActionListener(this);
		
	}
	public void close(){
		super.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new UpdateFrame(person);
		this.dispose();
	}
}
