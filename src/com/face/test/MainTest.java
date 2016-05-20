package com.face.test;

import org.bytedeco.javacpp.opencv_contrib.*;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_highgui.*;
import org.bytedeco.javacpp.opencv_objdetect.*;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

import com.face.control.FaceDetect;
import com.face.utils.PicUtils;

import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class MainTest {

	public static void main(String[] args) throws IOException {
		CanvasFrame dstFrame = new CanvasFrame("dst");
		dstFrame.setDefaultCloseOperation(3);
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
				//PicUtils.saveImage(dstImg, fileName);
				new Thread(new Server(addr, fileName)).start();
			}
		} catch (Exception e) {
			
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
