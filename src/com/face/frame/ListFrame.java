package com.face.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bytedeco.javacpp.annotation.Index;

import com.face.db.PersonDAO;
import com.face.model.Person;



public class ListFrame extends JFrame implements ListSelectionListener, MouseListener, ActionListener {

	private JPanel contentPane;
	DefaultListModel<Person> model;
	JList<Person> jList;
	JScrollPane jScrollPane;
	List<Person> plist;
	JPopupMenu pMenu;
	JMenuItem[] jItems;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListFrame frame = new ListFrame();
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
	public ListFrame() {
		setTitle("查找");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		model = new DefaultListModel<>();
		jList = new JList<>(model);
		jScrollPane = new JScrollPane(jList);
		jScrollPane.setBounds(100, 100, 200, 200);
		contentPane.add(jScrollPane);
		PersonDAO personDAO = new PersonDAO();
		plist = personDAO.findAll();
		for (Person p : plist) {
			model.addElement(p);
		}
		jList.addListSelectionListener(this);
		jList.addMouseListener(this);
		pMenu = new JPopupMenu();
		String[] items = { "修改", "删除" };
		jItems = new JMenuItem[2];
		for (int i = 0; i < jItems.length; i++) {
			jItems[i] = new JMenuItem(items[i]);
			jItems[i].setActionCommand(i + "");
			jItems[i].addActionListener(this);
			pMenu.add(jItems[i]);
		}
		setVisible(true);
	}

	int index = -1;

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			index = jList.getSelectedIndex();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			pMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Person p = null;
		String strIndex = ((JMenuItem) e.getSource()).getActionCommand();
		// 当用户点击后，才执行
		if (index >= 0) {
			// 执行修改
			if (strIndex.equals("0")) {
				p = model.getElementAt(index);
				new UpdateFrame(p);
			} else { // 执行删除
				p = model.getElementAt(index);
				// 从jlist中移除用户
				Frame delete = new DeleteFrame(p);
				PersonDAO pdo = new PersonDAO();
				Person temp = pdo.findByNo(p.getPersonNo());
				if (temp == null) {
					System.out.println("zheli");
					model.removeElementAt(index);
					jList.setModel(model);
				}
			}
		}
	}

}
