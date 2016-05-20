package com.face.control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.management.RuntimeErrorException;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_contrib.FaceRecognizer;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.face.db.PersonDAO;
import com.face.model.Person;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_legacy.*;
import static org.bytedeco.javacpp.opencv_contrib.*;

public class FaceRecognition {
	private static final Logger LOGGER = Logger.getLogger(FaceRecognition.class.getName());
	// 训练的人脸数
	private int nTrainFaces = 0;
	// 训练的图像
	IplImage[] trainingFaceImgArr;
	// 测试的图像
	IplImage[] testFaceImgArr;
	// 人数矩阵
	CvMat personNumTruthMat;
	// 人数
	int nPersons;
	// 人的姓名
	final List<String> personNames = new ArrayList<String>();
	// the egien数
	int nEigens = 0;
	// egien矩阵
	IplImage[] eigenVectArr;
	// egien值
	CvMat eigenValMat;
	// 平均图像
	IplImage pAvgTrainImg;
	// 投影训练
	CvMat projectedTrainFaceMat;

	public FaceRecognition() {
	}

	/*
	 * 训练
	 */
	public void lern() {
		trainingFaceImgArr = loadFaceImgArray();
		nTrainFaces = trainingFaceImgArr.length;
		if (nTrainFaces < 3) {
			return;
		}

		// 主成分分析法对训练的面孔
		doPCA();

		// 将训练图像投影到主成分分析子空间上
		projectedTrainFaceMat = cvCreateMat(nTrainFaces, nEigens, CV_32FC1);
		for (int j = 0; j < nTrainFaces; j++) {
			for (int k = 0; k < nEigens; k++) {
				projectedTrainFaceMat.put(j, k, 0.0);
			}
		}

		if (nTrainFaces < 5) {
			LOGGER.info("projectedTrainFaceMat contents:\n" + oneChannelCvMatToString(projectedTrainFaceMat));
		}
		final FloatPointer floatPointer = new FloatPointer(nEigens);
		// 将人脸图像通过Eigenface变换矩阵，投射到子空间中
		for (int i = 0; i < nTrainFaces; i++) {
			cvEigenDecomposite(trainingFaceImgArr[i], nEigens, eigenVectArr, 0, null, pAvgTrainImg, floatPointer);
			for (int j = 0; j < nEigens; j++) {
				projectedTrainFaceMat.put(i, j, floatPointer.get(j));
			}
		}
		// 保存识别数据至xml
		storeTrainingData();
		// 保存所有的特征值成图像
		storeEigenFaceImages();

	}

	/*
	 * 从文件中识别
	 */
	public int recoginzeFileList(String testFile) {
		int i = 0;
		CvMat trainPersonNumMat;
		float confidence = 0.0f;
		IplImage testImg = cvLoadImage(testFile, CV_LOAD_IMAGE_GRAYSCALE);
		if(testImg == null){
			return -1;
		}
		trainPersonNumMat = loadTrainingData();
		int iNearest;
		int nearest;
		int truth;
		testFaceImgArr = new IplImage[] { testImg };
		int nFaces = testFaceImgArr.length;
		FloatPointer floatPointer = new FloatPointer(nEigens);
		// 将人脸图像通过Eigenface变换矩阵，投射到子空间中
		cvEigenDecomposite(
				testFaceImgArr[0], //输入图像
				nEigens, 	//特征空间的eigen数量
				eigenVectArr, 	//特征空间中的特征脸
				0, 		//默认为0
				null, 	//默认为null
				pAvgTrainImg, 	//特征空间中的平均图像
				floatPointer);	//人脸在子空间中的投影，特征值
		FloatPointer pConfidence = new FloatPointer(confidence);
		iNearest = findNearestNeighbor(floatPointer, new FloatPointer(pConfidence));
		confidence = pConfidence.get();
		nearest = trainPersonNumMat.data_i().get(iNearest);
		LOGGER.info("nearest = " + nearest + ", Confidence = " + confidence);
		return nearest;
	}

	/*
	 * 找出相似度最高，返回其索引，存储相似值
	 * 欧几里得距离 
	 * 公式：0ρ = sqrt( (x1-x2)^2+(y1-y2)^2 )　|x| = √( x2 + y2 )
	 */
	private int findNearestNeighbor(FloatPointer projectedTestFace, FloatPointer floatPointer) {
		double leastDistSq = Double.MAX_VALUE;
		int i = 0;
		int iTrain = 0;
		int iNearest = 0;
		for (iTrain = 0; iTrain < nTrainFaces; iTrain++) {
			double distSq = 0;
			for (i = 0; i < nEigens; i++) {
				float projectedTrainFaceDistance = (float) projectedTrainFaceMat.get(iTrain, i);//获取特征空间中某个点的特征值
				float d_i = projectedTestFace.get(i) - projectedTrainFaceDistance;
				distSq += d_i * d_i;
				// eigenValMat.data_fl().get(i);
			}
			if (distSq < leastDistSq) {
				leastDistSq = distSq;
				iNearest = iTrain;
			}
		}
		//可信度
		float pConfidence = (float) (1.0f - Math.sqrt(leastDistSq / (float) (nTrainFaces * nEigens)) / 255.0f);
		floatPointer.put(pConfidence);
		return iNearest;
	}

	private void storeEigenFaceImages() {
		cvSaveImage("out_avarageImage.bmp", pAvgTrainImg);

		if (nEigens > 0) {
			int COLUMNS = 8;
			int nCols = Math.min(nEigens, COLUMNS);
			int nRows = 1 + (nEigens / COLUMNS);
			int w = eigenVectArr[0].width();
			int h = eigenVectArr[0].height();
			CvSize size = cvSize(nCols * w, nRows * h);
			final IplImage bigImg = cvCreateImage(size, IPL_DEPTH_8U, 1);
			for (int i = 0; i < nEigens; i++) {
				IplImage byteImg = convertFloatImageToUcharImage(eigenVectArr[i]);
				int x = w * (i % COLUMNS);
				int y = h * (i / COLUMNS);
				CvRect ROI = cvRect(x, y, w, h);
				cvSetImageROI(bigImg, ROI);
				cvCopy(byteImg, bigImg, null);
				cvResetImageROI(bigImg);
				cvReleaseImage(byteImg);
			}
			cvSaveImage("out_eigenfaces.bmp", bigImg);
			cvReleaseImage(bigImg);
		}
	}

	/*
	 * 将给定的浮点图像转换为无符号字符图像
	 */
	private IplImage convertFloatImageToUcharImage(IplImage srcImg) {
		IplImage dstImg;
		// 伸缩32位浮点像素适应8位浮点像素
		if (srcImg != null && (srcImg.width() > 0 && srcImg.height() > 0)) {
			double[] minVal = new double[1];
			double[] maxVal = new double[1];
			cvMinMaxLoc(srcImg, minVal, maxVal);
			if (minVal[0] < -1e30) {
				minVal[0] = -1e30;
			}
			if (maxVal[0] > 1e30) {
				maxVal[0] = 1e30;
			}
			if (maxVal[0] - minVal[0] == 0.0f) {
				maxVal[0] = minVal[0] + 0.001;
			}
			dstImg = cvCreateImage(cvSize(srcImg.width(), srcImg.height()), 8, 1);
			cvConvertScale(srcImg, dstImg, 255.0 / (maxVal[0] - minVal[0]),
					-minVal[0] * 255.0 / (maxVal[0] - minVal[0]));
			return dstImg;
		}
		return null;
	}

	// 存储训练文件到facedata.xml
	private void storeTrainingData() {
		CvFileStorage fileStorage;
		int i;
		fileStorage = cvOpenFileStorage("facedata.xml", null, CV_STORAGE_WRITE, null);
		cvWriteInt(fileStorage, "nPersons", nPersons);
		for (i = 0; i < nPersons; i++) {
			String varname = "personName_" + (i + 1);
			cvWriteString(fileStorage, varname, personNames.get(i), 0);
		}
		cvWriteInt(fileStorage, "nEigens", nEigens);
		cvWriteInt(fileStorage, "nTrainFaces", nTrainFaces);
		cvWrite(fileStorage, "trainPersonNumMat", personNumTruthMat);
		cvWrite(fileStorage, "eigenValMat", eigenValMat);
		cvWrite(fileStorage, "projectedTrainFaceMat", projectedTrainFaceMat);
		cvWrite(fileStorage, "avgTrainImg", pAvgTrainImg);
		for (i = 0; i < nEigens; i++) {
			String varname = "eigenVect_" + i;
			cvWrite(fileStorage, varname, eigenVectArr[i]);
		}
		cvReleaseFileStorage(fileStorage);
	}

	private String oneChannelCvMatToString(CvMat cvMat) {
		if (cvMat.channels() != 1) {
			throw new RuntimeException("错误的参数，矩阵必须有通道");
		}
		final int type = cvMat.type();
		StringBuilder s = new StringBuilder("[");
		for (int i = 0; i < cvMat.rows(); i++) {
			for (int j = 0; j < cvMat.cols(); j++) {
				if (type == CV_32FC1 || type == CV_32SC1) {
					s.append(cvMat.get(i, j));
				} else {
					throw new RuntimeException("错误参数，矩阵必须有1通道");
				}
				if (j < cvMat.cols() - 1) {
					s.append(", ");
				}
			}
			if (i < cvMat.rows() - 1) {
				s.append("\n ");
			}
		}
		s.append("]");
		return s.toString();
	}

	/*
	 * 从faceddata.xml 读取训练数据
	 */
	private CvMat loadTrainingData() {
		// 训练过程中的人数组
		CvMat pTrainPersonNumMat = null;
		CvFileStorage fileStorage;
		int i;
		fileStorage = cvOpenFileStorage("facedata.xml", null, CV_STORAGE_READ, null);

		if (fileStorage == null) {
			return null;
		}
		personNames.clear();
		nPersons = cvReadIntByName(fileStorage, null, "nPersons", 0);
		if (nPersons == 0) {
			return null;
		}

		for (i = 0; i < nPersons; i++) {
			String sPersonName;
			String varname = "personName_" + (i + 1);
			sPersonName = cvReadStringByName(fileStorage, null, varname, "");
			personNames.add(sPersonName);
		}
		// 载入数据
		nEigens = cvReadIntByName(fileStorage, null, "nEigens", 0);
		nTrainFaces = cvReadIntByName(fileStorage, null, "nTrainFaces", 0);
		Pointer pointer = cvReadByName(fileStorage, null, "trainPersonNumMat");
		pTrainPersonNumMat = new CvMat(pointer);
		pointer = cvReadByName(fileStorage, null, "eigenValMat");
		eigenValMat = new CvMat(pointer);
		pointer = cvReadByName(fileStorage, null, "projectedTrainFaceMat");
		projectedTrainFaceMat = new CvMat(pointer);
		pointer = cvReadByName(fileStorage, null, "avgTrainImg");
		pAvgTrainImg = new IplImage(pointer);
		eigenVectArr = new IplImage[nTrainFaces];
		for (i = 0; i <= nEigens; i++) {
			String varname = "eigenVect_" + i;
			pointer = cvReadByName(fileStorage, null, varname);
			eigenVectArr[i] = new IplImage(pointer);
		}
		cvReleaseFileStorage(fileStorage);
		return pTrainPersonNumMat;
	}

	/*
	 * 主成分分析，发现平均图像，表示在给定的数据集的任何图像的特征脸
	 */
	private void doPCA() {
		int i;
		// 迭代算法的终止准则
		CvTermCriteria calcLimit;
		CvSize faceImgSize = new CvSize();
		nEigens = nTrainFaces - 1;

		// 分配图像的特征向量
		faceImgSize.width(trainingFaceImgArr[0].width());
		faceImgSize.height(trainingFaceImgArr[0].height());
		eigenVectArr = new IplImage[nEigens];
		for (i = 0; i < nEigens; i++) {
			eigenVectArr[i] = cvCreateImage(faceImgSize, IPL_DEPTH_32F, 1);
		}
		// 分配特征值数组
		eigenValMat = cvCreateMat(1, nEigens, CV_32FC1);
		// 分配平均图像
		pAvgTrainImg = cvCreateImage(faceImgSize, IPL_DEPTH_32F, 1);
		// 设置主成分终止准则
		calcLimit = cvTermCriteria(CV_TERMCRIT_ITER, // 类型
				nEigens, // 最大迭代次数
				1 // 结果准确性
		);
		// 计算平均图像，特征值和特征向量
		cvCalcEigenObjects(nTrainFaces, trainingFaceImgArr, eigenVectArr, CV_EIGOBJ_NO_CALLBACK, 0, null, calcLimit,
				pAvgTrainImg, eigenValMat.data_fl());
		cvNormalize(eigenValMat, eigenValMat, 1, 0, CV_L1, null);

	}

	/*
	 * 载入图像和姓名
	 */
	private IplImage[] loadFaceImgArray() {
		IplImage[] faceImgArr;
		List<Person> personList;
		String imgFileName;
		int iFace = 0;
		int nFaces = 0;
		int i;
		try {
			personList = new PersonDAO().findAll();
			// 计数人脸数
			if (personList != null) {
				nFaces = personList.size();
			}
			// 分配人脸图像序列和人数矩阵
			faceImgArr = new IplImage[nFaces];
			personNumTruthMat = cvCreateMat(1, nFaces, CV_32SC1);
			for (int j = 0; j < nFaces; j++) {
				personNumTruthMat.put(0, j, 0);
			}
			personNames.clear();
			nPersons = 0;
			for (iFace = 0; iFace < nFaces; iFace++) {
				String personName;// 当前姓名
				String sPersonName;// 唯一姓名
				int personNumber;// 标签
				Person p = personList.get(iFace);
				personNumber = p.getId();
				personName = p.getPersonName();
				imgFileName = p.getPersonImage();
				sPersonName = personName;
				if (personNumber > nPersons) {
					// 添加至人姓名列表
					personNames.add(sPersonName);
					nPersons = personNumber;
				}
				// 记录标签
				personNumTruthMat.put(0, iFace, personNumber);
				faceImgArr[iFace] = cvLoadImage(imgFileName, CV_LOAD_IMAGE_GRAYSCALE);
				if (faceImgArr[iFace] == null) {
					throw new RuntimeException("不能加载图像");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return faceImgArr;
	}

	public static void main(String[] args) {
		FaceRecognition faceRecognition = new FaceRecognition();
		 faceRecognition.lern();
		faceRecognition.recoginzeFileList("3.jpg");
	}
}
