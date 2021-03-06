package com.face.control;

import org.bytedeco.javacpp.opencv_contrib.*;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_highgui.*;
import org.bytedeco.javacpp.opencv_objdetect.*;

import com.face.utils.PicUtils;

import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.bytedeco.javacpp.Loader;

public class FaceDetect {
	private String classifierName = null;
	private CvHaarClassifierCascade classifier = null;

	public FaceDetect() {
		if (classifierName == null) {
			File file = new File("classifier/haarcascade_frontalface_alt_tree.xml");
			File file1 = new File("classifier/haarcascade_frontalface_alt.xml");
			File file2 = new File("classifier/haarcascade_frontalface_alt2.xml");
			File file3 = new File("classifier/haarcascade_frontalface_default.xml");
			classifierName = file1.getAbsolutePath();
			classifier = new CvHaarClassifierCascade(cvLoad(classifierName));
			if (classifier.isNull()) {
				System.err.println("Error loading classifier file \"" + classifierName + "\".");
				System.exit(1);
			}
		}
	}

	public CvSeq faceDect(IplImage srcImg, CvMemStorage storage) {
		cvClearMemStorage(storage);
		IplImage grayImg = PicUtils.grayImgFromIplImage(srcImg);
		IplImage thresImg = PicUtils.Threshold(srcImg);
		CvSeq contour = new CvSeq(null);
		cvFindContours(thresImg, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST,
				CV_CHAIN_APPROX_SIMPLE);
		// while(contour!=null&&!contour.isNull()){
		// if(contour.elem_size()>0){
		// CvSeq points = cvApproxPoly(contour, Loader.sizeof(CvContour.class),
		// storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour)*0.02, 0);
		// cvDrawContours(srcImg, points, CvScalar.BLUE, CvScalar.BLUE, -1, 1,
		// CV_AA);
		// }
		// contour = contour.h_next();
		// }
		CvSeq faces = cvHaarDetectObjects(
				grayImg, //被检测图像
				classifier, //分类器
				storage, // 用来存储检测到的一序列候选目标矩形框的内存区域
				1.2f, // 在前后两次相继的扫描中，搜索窗口的比例系数
				2, // 构成检测目标的相邻矩形的最小个数
				CV_HAAR_DO_CANNY_PRUNING);
		return faces;
	}

	public IplImage faceTrick(IplImage srcImg, CvMemStorage storage,String fileName) {
		CvSeq faces = faceDect(srcImg, storage);
		int total = faces.total();
		if(total > 2){
			CvFont font = new CvFont();
			cvInitFont(font, CV_FONT_HERSHEY_SIMPLEX, 1, 1);
			cvPutText(srcImg, "WARNING!", cvPoint(5, 20), font, CvScalar.RED);
		}
		
		
		for (int i = 0; i < total; i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			int x = r.x(), y = r.y(), w = r.width(), h = r.height();
			System.out.println(w + "," + h);
			IplImage temp = cvCreateImage(cvSize(w, h), srcImg.depth(), srcImg.nChannels());
			cvSetImageROI(srcImg, r);
			cvCopy(srcImg, temp);
			PicUtils.saveImage(temp, fileName);
			cvResetImageROI(srcImg);
			cvRectangle(srcImg, cvPoint(x, y), cvPoint(x + w, y + h), CvScalar.RED, 1, CV_AA, 0);
		}
		return srcImg;
	}

	public void test(IplImage srcImg, CvMemStorage storage) {
		CvSeq faces = faceDect(srcImg, storage);
		int total = faces.total();
		if (total >= 2) {
			CvRect r1 = new CvRect(cvGetSeqElem(faces, 0));
			CvRect r2 = new CvRect(cvGetSeqElem(faces, 1));
			double dis = PicUtils.getDistance(cvPoint(r1.x(), r1.y()), cvPoint(r2.x(), r2.y()));
			System.out.println(dis);
		}
	}

}
