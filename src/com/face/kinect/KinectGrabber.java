package com.face.kinect;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;

import org.OpenNI.*;

import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.*;

public class KinectGrabber {
	private Context context;
	private ImageGenerator imageGen;
	private int imWidth, imHeight;

	public KinectGrabber() {
		try {
			context = new Context();
			License license = new License("PrimeSense", "0KOIk2JeIBYClPWVnMoRKn5cdY4=");
			context.addLicense(license);
			
			imageGen = ImageGenerator.create(context);
			MapOutputMode mapMode = new MapOutputMode(640, 480, 30);
			imageGen.setMapOutputMode(mapMode);
			imageGen.setPixelFormat(PixelFormat.RGB24);
			context.setGlobalMirror(true);
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			context.startGeneratingAll();
			ImageMetaData imageMD = imageGen.getMetaData();
			imWidth = imageMD.getFullXRes();
			imHeight = imageMD.getFullYRes();
			System.out.println("Kinect (w,h): (" + imWidth + ", " + 
                    imHeight + "); ");
		} catch (StatusException e) {
			e.printStackTrace();
		}
	}
	
	public IplImage grab() {
		try {
			context.waitAnyUpdateAll();
			IplImage iplImage = IplImage.create(640, 480, IPL_DEPTH_8U, 3);
			//ByteBuffer imageBB = imageGen.getImageMap().createByteBuffer();
			ByteBuffer imageBB = iplImage.getByteBuffer();
			ImageMap iMap = imageGen.getImageMap();
			iMap.copyToBuffer(imageBB, 640*480*3);
			return iplImage;
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void stop() {
		try {
			context.stopGeneratingAll();
		} catch (StatusException e) {
		}

		context.release();
	}
}
