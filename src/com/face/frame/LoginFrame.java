package com.face.frame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JButton cancelBT ;
	private JButton loginBT;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
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
	public LoginFrame() {
		setTitle("管理员登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titleLB = new JLabel("ATM视频监控系统");
		titleLB.setFont(new Font("宋体", Font.PLAIN, 16));
		titleLB.setHorizontalAlignment(SwingConstants.CENTER);
		titleLB.setBounds(134, 31, 147, 28);
		contentPane.add(titleLB);
		
		JLabel lblNewLabel = new JLabel("用户名");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(117, 85, 54, 15);
		contentPane.add(lblNewLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		nameField.setBounds(197, 82, 105, 25);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("密码");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 13));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(117, 128, 54, 15);
		contentPane.add(lblNewLabel_1);
		
		cancelBT = new JButton("取消");
		cancelBT.setBounds(81, 201, 93, 23);
		contentPane.add(cancelBT);
		cancelBT.addActionListener(this);
		loginBT = new JButton("登录");
		loginBT.setBounds(247, 201, 93, 23);
		loginBT.addActionListener(this);
		contentPane.add(loginBT);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		passwordField.setBounds(197, 125, 105, 25);
		contentPane.add(passwordField);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancelBT){
			this.dispose();
		}else{
			String username = nameField.getText();
			String password = passwordField.getText();
			login(username, password);
		}
	}
	private void login(String username,String password){
		if(!username.equals("")&&!password.equals("")){
			if(username.equals("视频监控")&&password.equals("123")){
				System.out.println("123");
				new MainFrame();
				this.dispose();
			}else {
				JOptionPane.showMessageDialog(null, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null, "请输入用户名或密码", "警告", JOptionPane.WARNING_MESSAGE);
		}
	}
}
