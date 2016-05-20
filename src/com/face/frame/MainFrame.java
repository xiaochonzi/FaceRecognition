package com.face.frame;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.face.control.FaceRecognition;
import com.face.db.PersonDAO;
import com.face.model.Person;


import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JButton;

public class MainFrame {

	private JFrame frame;
	private ImageCanvas recCanvas;
	private ImageCanvas regCanvas;
	private JLabel jlName;
	private JLabel jlLevel;
	private JTextArea addrArea;
	
	protected int index;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws Exception
	 */
	private void initialize() throws Exception {

		frame = new JFrame();
		frame.setTitle("主界面");
		frame.setResizable(false);
		frame.setBounds(100, 100, 750, 470);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("文件");
		menuBar.add(fileMenu);

		JMenuItem openMenuItem = new JMenuItem("启动");
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Kinect();
			}
		});
		fileMenu.add(openMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("退出");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		JMenu editMenu = new JMenu("编辑");
		menuBar.add(editMenu);

		JMenuItem noMenuItem = new JMenuItem("编号查询");
		noMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
		noMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String inputValue = JOptionPane.showInputDialog("请输入要查询的编号");
				if (inputValue != null && !inputValue.equals("")) {
					search(inputValue);
				}
			}
		});

		JMenuItem addMenuItem = new JMenuItem("添加");
		addMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
		addMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertFrame();
			}
		});
		editMenu.add(addMenuItem);
		editMenu.add(noMenuItem);

		JMenuItem allMenu = new JMenuItem("全部查询");
		allMenu.setHorizontalAlignment(SwingConstants.CENTER);
		allMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ListFrame();
			}
		});
		editMenu.add(allMenu);

		JMenu helpMenu = new JMenu("帮助");
		menuBar.add(helpMenu);

		JMenuItem aboutMenuItem = new JMenuItem("关于");
		helpMenu.add(aboutMenuItem);

		frame.getContentPane().setLayout(null);

		recCanvas = new ImageCanvas();
		recCanvas.setBounds(71, 40, 160, 180);
		frame.getContentPane().add(recCanvas);

		regCanvas = new ImageCanvas();
		regCanvas.setBounds(437, 40, 160, 180);
		frame.getContentPane().add(regCanvas);

		JLabel lb1 = new JLabel("姓名：");
		lb1.setHorizontalAlignment(SwingConstants.RIGHT);
		lb1.setBounds(406, 260, 69, 15);
		frame.getContentPane().add(lb1);

		jlName = new JLabel("空");
		jlName.setHorizontalAlignment(SwingConstants.LEFT);
		jlName.setBounds(517, 260, 54, 15);
		frame.getContentPane().add(jlName);

		JLabel lblNewLabel = new JLabel("危险级别：");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(406, 297, 69, 15);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("地址：");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(406, 334, 69, 15);
		frame.getContentPane().add(lblNewLabel_1);

		jlLevel = new JLabel("空");
		jlLevel.setHorizontalAlignment(SwingConstants.LEFT);
		jlLevel.setBounds(517, 297, 54, 15);
		frame.getContentPane().add(jlLevel);

		addrArea = new JTextArea();
		addrArea.setBounds(517, 330, 160, 46);
		frame.getContentPane().add(addrArea);

		JLabel label = new JLabel("原图");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(117, 20, 54, 15);
		frame.getContentPane().add(label);

		JLabel label_1 = new JLabel("数据库图像");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(468, 20, 80, 15);
		frame.getContentPane().add(label_1);

		JButton btn = new JButton("训练");
		btn.setBounds(101, 293, 93, 23);
		frame.getContentPane().add(btn);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				PersonDAO pDao = new PersonDAO();
//				List<Person> persons = pDao.findAll();
//				Image image = Toolkit.getDefaultToolkit().getImage(persons.get(0).getPersonImage());
//				regCanvas.setImage(image);
				FaceRecognition faceRecognition = new FaceRecognition();
				faceRecognition.lern();
			}
		});
		
		new Thread(new Client(recCanvas,regCanvas,jlName,jlLevel,addrArea)).start();
		
	}

	public void search(String no) {
		PersonDAO pDao = new PersonDAO();
		Person person = pDao.findByNo(no);
		if (person != null) {
			System.out.println(person);
			new SearchFrame(person);
		}
	}

	
}

class ImageCanvas extends Canvas {
	private Image image = null;
	private Toolkit toolkit = null;

	@Override
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, this);
		} else {
			this.image = image;
		}
	}

	public void setImage(Image image) {
		this.image = image;
		repaint();
	}
}

class Client implements Runnable{
	FaceRecognition faRecognition;
	private ImageCanvas recCanvas;
	private ImageCanvas regCnavas;
	private JLabel jlName;
	private JLabel jlLevel;
	private JTextArea addrArea;
	public Client(ImageCanvas recCanvas,ImageCanvas regCanvas,JLabel jlName,JLabel jlLevel,JTextArea addrArea){
		faRecognition = new FaceRecognition();
		this.recCanvas = recCanvas;
		this.regCnavas = regCanvas;
		this.jlName = jlName;
		this.jlLevel = jlLevel;
		this.addrArea = addrArea;
	}
	@Override
    public void run() {
        System.out.println("run....");
        byte [] buf = new byte[8192];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        List<Person> pList = new PersonDAO().findAll();
        Map<String,Person> map = new HashMap<String,Person>();
        for(int i=0;i<pList.size();i++){
        	Person p = pList.get(i);
        	map.put(p.getId()+"", p);
        }
        try{
            DatagramPacket dp_receive = new DatagramPacket(buf, buf.length);
            DatagramSocket ds = new DatagramSocket(20080);
            while(true){
            	System.out.println("server reveice ...");
                ds.receive(dp_receive);
                String end = new String(dp_receive.getData(),0,dp_receive.getLength());
                if(end==null){
                	regCnavas.setImage(null);
                	jlName.setText("");
                	jlLevel.setText("");
                	addrArea.setText("");
                	continue;
                }  
                Toolkit toolkit =Toolkit.getDefaultToolkit();
                Image image = toolkit.createImage(end);
                recCanvas.setImage(image);
                int id = faRecognition.recoginzeFileList(end);
                Person p = map.get(id+"");
                end = p.getPersonImage();
                image = toolkit.createImage(end);
                regCnavas.setImage(image);
                jlName.setText(p.getPersonName());
                jlLevel.setText(p.getPersonLevel());
                addrArea.setText(p.getPersonAddr());
            }     
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
