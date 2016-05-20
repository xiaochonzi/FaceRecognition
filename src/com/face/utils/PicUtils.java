package com.face.utils;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class PicUtils {

	public static IplImage grayImgFromIplImage(IplImage srcImg) {
		IplImage grayImg = null;
		if (srcImg.arrayChannels() > 1) {
			CvSize size = cvSize(srcImg.width(), srcImg.height());
			grayImg = cvCreateImage(size, IPL_DEPTH_8U, 1);
			cvCvtColor(srcImg, grayImg, CV_BGR2GRAY);
		} else {
			grayImg = srcImg;
		}
		return grayImg;
	}

	public static CvMat grayImgFromCvMat(CvMat srcMat) {
		CvMat grayMat = cvCreateMat(srcMat.rows(), srcMat.cols(), CV_8U);
		if (srcMat.channels() > 1) {
			cvCvtColor(srcMat, grayMat, CV_BGR2GRAY);
		} else {
			cvCopy(srcMat, grayMat);
		}
		return grayMat;
	}

	/*
	 * 固定阀值 去除噪声
	 */
	public static IplImage Threshold(IplImage srcImg) {
		IplImage grayImg = grayImgFromIplImage(srcImg);
		cvThreshold(grayImg, grayImg, 127, 255, CV_THRESH_BINARY);
		return grayImg;
	}
	/*
	 * 光线补偿
	 */
	public static IplImage merge(IplImage srcImg) {
		IplImage[] pImgageChannel = new IplImage[4];
		IplImage pImage = cvCreateImage(cvGetSize(srcImg), srcImg.depth(), srcImg.nChannels());
		if (pImage != null) {
			for (int i = 0; i < srcImg.nChannels(); i++) {
				pImgageChannel[i] = cvCreateImage(cvGetSize(pImage), pImage.depth(), 1);
			}
			cvSplit(srcImg, pImgageChannel[0], pImgageChannel[1], pImgageChannel[2], pImgageChannel[3]);
			for (int i = 0; i < pImage.nChannels(); i++) {
				cvEqualizeHist(pImgageChannel[i], pImgageChannel[i]);
			}
			cvMerge(pImgageChannel[0], pImgageChannel[1], pImgageChannel[2], pImgageChannel[3], pImage);
			return pImage;
		}
		return null;
	}
	
	/*
	 * 高斯平滑
	 */
	public static IplImage smooth(IplImage srcImg){
		IplImage grayImg = grayImgFromIplImage(srcImg);
		// 高斯平滑
		cvSmooth(grayImg, grayImg, CV_GAUSSIAN, 9, 9, 2, 2);
		return grayImg;
	}
	
	/*
	 * 直方图均衡
	 */
	public static IplImage equalizeHist(IplImage srcImg){	
		IplImage grayImg = grayImgFromIplImage(srcImg);
		cvEqualizeHist(grayImg, grayImg);
		return grayImg;
	}
	
	public static double getDistance(CvPoint p1,CvPoint p2){
		double a = Math.pow(p1.x()-p2.x(), 2);
		double b = Math.pow(p1.y()-p2.y(), 2);
		double c = a+b;
		return Math.sqrt(c);
	}
	
	public static void saveImage(IplImage srcImg,String fileName){
		CvSize size = cvSize(160,180);
		IplImage dstImg = cvCreateImage(size, srcImg.depth(), srcImg.nChannels());
		cvResize(srcImg, dstImg,CV_INTER_AREA);
		cvSaveImage(fileName, dstImg);
	}
	
	public static String date(){
		long current = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf.format(current);
	}
}
