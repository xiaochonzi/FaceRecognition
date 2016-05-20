package com.face.test;

import java.io.File;

import org.bytedeco.javacpp.opencv_contrib.*;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_highgui.*;
import org.bytedeco.javacpp.opencv_objdetect.*;

import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import com.face.utils.PicUtils;

public class DBTest {
	public static void main(String []args){
		File dir = new File("e:/img");
		File []files = dir.listFiles();
		for(File file :files){
			IplImage srcImg = cvLoadImage(file.getAbsolutePath());
			if(srcImg.width()==160&&srcImg.height()==180){
				continue;
			}
			PicUtils.saveImage(srcImg, file.getAbsolutePath());
		}
	}
}
