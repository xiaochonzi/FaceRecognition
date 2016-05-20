package com.face.frame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;



class ImagePanel extends JPanel {
	private String imgPath;
	private Image img;
	private int width;
	private int height;


	public ImagePanel(){}
	
	public ImagePanel(Image img) {
		this.img = img;
		setSize(img);
	}
	public ImagePanel(String path) {
		try {
			imgPath = path;
//			String path2 = "temp2.jpg";
			//ImageUtils.scale(path, "temp2.jpg", 200, 250, false);
			img = ImageIO.read(new FileInputStream(new File(imgPath)));
			setSize(img);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Image getImg() {
		return img;
	}
	public void setImg(Image img) {
		
		this.img = img;
	}
	public String getImgPath(){
		return imgPath;
	}
	
	private void setSize(Image img) {
		if (img != null) {
			Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		//	Dimension size = new Dimension(200, 250);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int x = 0, y = 0;
		if (img == null) {
			return;
		}
		g.drawImage(img, 0, 0, this);

	}
}
