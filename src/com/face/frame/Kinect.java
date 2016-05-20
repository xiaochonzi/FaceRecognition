package com.face.frame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

import com.face.control.FaceDetect;
import com.face.utils.PicUtils;

public class Kinect {
	public Kinect(){
		CanvasFrame dstFrame = new CanvasFrame("dst");
		FaceDetect faceDetect = null;
		faceDetect = new FaceDetect();
		try {
			FrameGrabber grabber = FrameGrabber.createDefault(0);
			grabber.start();
			IplImage srcImg = grabber.grab();
			CvMemStorage storage = CvMemStorage.create();
			InetAddress addr = InetAddress.getLocalHost();
			while((srcImg = grabber.grab())!=null){
				final String fileName ="e:/temp/"+ PicUtils.date()+".png";
				IplImage dstImg = faceDetect.faceTrick(srcImg,storage,fileName);
				dstFrame.showImage(dstImg);
				new Thread(new Server(addr, fileName)).start();
			}
		} catch (Exception | IOException e) {		
			e.printStackTrace();
		}
	}
}
class Server implements Runnable {
	private InetAddress addr ;
	private String fileName;
	public Server(InetAddress addr,String fileName) {
		this.addr = addr;
		this.fileName = fileName;
	}
	@Override
	public void run() {
		byte[] buf = new byte[8192];
		try {
			byte[] end = fileName.getBytes();
			DatagramPacket dp_end = new DatagramPacket(end, end.length, addr, 20080);
			DatagramSocket ds = new DatagramSocket();
			ds.send(dp_end);
			System.out.println("send ok!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
