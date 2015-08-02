package com.opulenmedical.testpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends Activity {

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCR/";
	public static final String lang = "terry";

	private final String TAG = "testproc";
	private int i = 0;
	private List<Mat> resultList = new ArrayList<Mat>();
	private PictureInfo pictureInfo;
	private List<SampleInfo> sampleInfoList;

	private TessBaseAPI baseApi = new TessBaseAPI();

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");

			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	// 其实不是我说的
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path
							+ " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}

		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata"))
				.exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang
						+ ".traineddata");
				// GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/"
						+ lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				// while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				// gin.close();
				out.close();

				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG,
						"Was unable to copy " + lang + " traineddata "
								+ e.toString());
			}
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!OpenCVLoader.initDebug()) {
			Log.d(TAG,
					"Internal OpenCV library not found. Using OpenCV Manager for initialization");
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this,
					mLoaderCallback);
		} else {
			Log.d(TAG, "OpenCV library found inside package. Using it!");
			mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		}
	}

	public void takePhoto(View v) {
		// 启动系统提供的拍照activity
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), "haha.jpg")));
		startActivityForResult(intent, 10);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(this, "拍照成功", 0).show();

	}

	public void analysis(View v) {

		baseApi.init(DATA_PATH, lang);
		pictureInfo = new PictureInfo();
		sampleInfoList = new ArrayList<SampleInfo>();

		i = 0;

//		// 解析图片时需要使用到的参数都封装在这个对象里了
//		Options opt = new Options();
//		// 不为像素申请内存，只获取图片宽高
//		opt.inJustDecodeBounds = true;
		String fileName = Environment.getExternalStorageDirectory().getPath()
				+ "/haha.jpg";
//		BitmapFactory.decodeFile(fileName, opt);
//		// 拿到图片宽高
//		int imageWidth = opt.outWidth;
//		int imageHeight = opt.outHeight;
//
//		Display dp = getWindowManager().getDefaultDisplay();
//		// 拿到屏幕宽高
//		int screenWidth = dp.getWidth();
//		int screenHeight = dp.getHeight();
//
//		// 计算缩放比例
//		int scale = 1;
//		int scaleWidth = imageWidth / screenWidth;
//		int scaleHeight = imageHeight / screenHeight;
//		if (scaleWidth >= scaleHeight && scaleWidth >= 1) {
//			scale = scaleWidth;
//		} else if (scaleWidth < scaleHeight && scaleHeight >= 1) {
//			scale = scaleHeight;
//		}
//
//		// 设置缩放比例
//		opt.inSampleSize = scale;
//		opt.inJustDecodeBounds = false;
//		Bitmap bmp = BitmapFactory.decodeFile(fileName, opt);

		
		Bitmap bmp = BitmapFactory.decodeFile(fileName);
		
		
		// 原图RGBA
		Mat src = new Mat();
		Utils.bitmapToMat(bmp, src);
		
		//取消镜面效果
		Core.flip(src, src, 0);
		
		writeImageToFile(src, i, true);
		i++;
		
		//旋转90
		Core.transpose(src,src);
		Core.flip(src,src,1); // code = 0 - x axis; 1 - y ; -1 - both
		
	
		
		writeImageToFile(src, i, true);
		i++;

		// 高斯模糊
		Mat blur = new Mat();
		// 二值处理
		Mat threshold = new Mat();
		// Open处理
		Mat open = new Mat();

		// int i = 0;

		// 高斯模糊
		Imgproc.GaussianBlur(src, blur, new Size(5, 5), 0, 0);

		// 灰化处理
		Imgproc.cvtColor(blur, blur, Imgproc.COLOR_RGBA2GRAY);

		// 二值处理
		Imgproc.threshold(blur, threshold, 0, 255, Imgproc.THRESH_OTSU
				+ Imgproc.THRESH_BINARY);

		// 为开操作准备
		Mat rect = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(
				10, 3));

		// 开操作
		Imgproc.morphologyEx(threshold, open, Imgproc.MORPH_OPEN, rect);

		writeImageToFile(threshold, i, false);
		i++;

		// 保存处理过的轮廓
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		// 查找所有轮廓
		Imgproc.findContours(open, contours, new Mat(), Imgproc.RETR_EXTERNAL,
				Imgproc.CHAIN_APPROX_SIMPLE);

		int pictureHeight = open.height();
		int pictureWidth = open.width();
		int pictureCenterX = pictureWidth / 2;
		int pictureCenterY = pictureHeight / 2;

		pictureInfo.setPictureCenterX(pictureCenterX);
		pictureInfo.setPictureCenterY(pictureCenterY);
		pictureInfo.setPictureHeight(pictureHeight);
		pictureInfo.setPictureWidth(pictureWidth);

		// 查找最大的轮廓
		double maxArea = getMaxAera(contours);

		// 遍历轮廓
		for (MatOfPoint contour : contours) {

			// 保存每个样本
			SampleInfo sampleInfo = new SampleInfo();

			if (Imgproc.contourArea(contour) > 0.1 * maxArea) {

				// 转换
				MatOfPoint2f m2 = new MatOfPoint2f();
				m2.fromList(contour.toList());
				// 获取最小外界矩形
				RotatedRect rotateRect = Imgproc.minAreaRect(m2);

				// 修正角度（当大于一定角度是需要修正）
				double angle = rotateRect.angle;
				if (Math.abs(angle) > 30) {
					if (angle > 0) {
						angle = 90 - angle;
					} else {
						angle = angle + 90;
					}

				}

				// 找到该轮廓的外接矩形
				Rect boundRect = Imgproc.boundingRect(contour);

				// private int sampleInPictureLeftX;
				// private int sampleInPictureLeftY;
				// private int sampleInPictureCenterX ;
				// private int sampleInPictureCenterY;

				// 从原图中取出外接矩形区域，此区域就是样本的图像
				Mat sample = new Mat(src, boundRect);

				sampleInfo.setSampleInPictureLeftX(boundRect.x);
				sampleInfo.setSampleInPictureLeftY(boundRect.y);
				sampleInfo.setSampleInPictureCenterX(boundRect.x
						+ (boundRect.width / 2));
				sampleInfo.setSampleInPictureCenterX(boundRect.y
						+ (boundRect.height / 2));
				sampleInfo.setSampleHeight(sample.height());
				sampleInfo.setSampleWidth(sample.width());
				sampleInfo.setSampleCenterX(sample.width() / 2);
				sampleInfo.setSampleCenterY(sample.height() / 2);

				// 为旋转做准备，定义旋转矩阵
				Mat rotmat = Imgproc.getRotationMatrix2D(rotateRect.center,
						angle, 1);

				// 用作旋转后Mat
				Mat rotatedSample = new Mat();

				// 仿反射旋转，调正样本
				Imgproc.warpAffine(sample, rotatedSample, rotmat,
						sample.size(), Imgproc.INTER_LINEAR
								+ Imgproc.WARP_FILL_OUTLIERS);

				// 旋转后样本的相关参数
				sampleInfo.setSampleRotatedHeight(rotatedSample.height());
				sampleInfo.setSampleRotatedWidth(rotatedSample.width());
				sampleInfo.setSampleRotatedCenterX(rotatedSample.width() / 2);
				sampleInfo.setSampleRotatedCenterY(rotatedSample.height() / 2);

				// 用作旋转后Mat
				Mat blur2Open = new Mat();

				// 高斯模糊
				Imgproc.GaussianBlur(rotatedSample, blur2Open, new Size(5, 5),
						0, 0);

				// 灰化处理
				Imgproc.cvtColor(blur2Open, blur2Open, Imgproc.COLOR_RGBA2GRAY);

				// 二值处理
				Imgproc.threshold(blur2Open, blur2Open, 0, 255,
						Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

				// 为开操作准备
				Mat rectOpen = Imgproc.getStructuringElement(
						Imgproc.MORPH_RECT, new Size(10, 3));

				// 开操作
				Imgproc.morphologyEx(blur2Open, blur2Open, Imgproc.MORPH_OPEN,
						rectOpen);

				// 保存轮廓（其中一个样本）
				List<MatOfPoint> sampleContours = new ArrayList<MatOfPoint>();

				// 查找所有轮廓（此处能查出检测区域有多少个样本）
				Imgproc.findContours(blur2Open, sampleContours, new Mat(),
						Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

				double onlyOneMax = getMaxAera(sampleContours);

				// 因为旋转后，样本的位置发生的些许变化，重新去去白色的样本轮廓，此处应该返回1
				for (MatOfPoint sampleContour : sampleContours) {

					if (Imgproc.contourArea(sampleContour) == onlyOneMax) {

						// 找到该轮廓的外接矩形
						Rect sampleBoundRect = Imgproc
								.boundingRect(sampleContour);

						// 从旋转后的样本中再次取出外接矩形区域
						Mat cleanSample = new Mat(rotatedSample,
								sampleBoundRect);

						// 旋转后再次二值化处理等后的样本的相关参数
						sampleInfo.setSampleAdjustHeight(cleanSample.height());
						sampleInfo.setSampleAdjustWidth(cleanSample.width());
						sampleInfo
								.setSampleAdjustCenterX(cleanSample.width() / 2);
						sampleInfo
								.setSampleAdjustCenterY(cleanSample.height() / 2);

						writeImageToFile(cleanSample, i, true);
						i++;

						// 只留下样本区域的Mat转成HSV
						Mat hsvForBule = new Mat();

						// // 转为HSV
						// Imgproc.cvtColor(cleanSample, hsvForBule,
						// Imgproc.COLOR_RGB2HSV);
						//
						//
						// // 高斯模糊
						// Imgproc.GaussianBlur(hsvForBule, hsvForBule, new
						// Size(5, 5), 0, 0);
						//
						// // 蓝色100-140，生成蓝色区域的二值图（单通道）//不靠谱。。。。
						// Core.inRange(hsvForBule, new Scalar(100, 30, 30), new
						// Scalar(140,255, 255), hsvForBule);
						//
						//
						// // 为闭合操作准备 Size(？, ？)此处的Size要根据具体情况调正
						// Mat rectClose =
						// Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
						// Size(20, 3));
						//
						// // 闭合操作
						// Imgproc.morphologyEx(hsvForBule, hsvForBule,
						// Imgproc.MORPH_CLOSE, rectClose);
						//
						// writeImageToFile(hsvForBule,i,false);
						// i++;

						// x,y求导计算的结果，定位比较靠谱
						//hsvForBule = sobelOper(cleanSample);

						// 边缘很多，但是不容易连通
						 hsvForBule = cannyOper(cleanSample);
						 

						// hsvForBule=thresholdOper(cleanSample);

						// 保存处理过的轮廓
						List<MatOfPoint> textContours = new ArrayList<MatOfPoint>();

						// 查找所有的有可能是文字的轮廓
						Imgproc.findContours(hsvForBule, textContours,
								new Mat(), Imgproc.RETR_EXTERNAL,
								Imgproc.CHAIN_APPROX_SIMPLE);

						double maxAreaForText = 0;

						// 样本图片中心点X
						int centerX = cleanSample.width() / 2;
						

						// 样本图片中心点Y
						int centerY = cleanSample.height() / 2;

						int textCenterMaxY = 0;

						// TextInfo textInfo = new TextInfo();

						// 遍历所有可能是文字的区域
						for (MatOfPoint textContour : textContours) {

							// 计算该轮廓的面积
							double area = Imgproc.contourArea(textContour);
							// Log.d(TAG, String.valueOf(area));

							// 获得该区域（可能是文字区域，可能不是）的矩形
							Rect textBound = Imgproc.boundingRect(textContour);

							// 计算矩形中心点X
							int textCenterX = textBound.x + textBound.width / 2;

							// 中心X比值（0-100）
							int XScale = 0;
							
							int heightScale =0;
							int widthScale=0;

							// 如果文字区域中心点X小于样本图片中心点X
							if (centerX > textCenterX) {
								XScale = textCenterX * 100 / centerX;
							} else {
								XScale = centerX * 100 / textCenterX;
							}
							
							heightScale=cleanSample.height()*10/textBound.height;
							widthScale=cleanSample.width()*10/textBound.width;
							
							

							// 30%误差精度（用于判断最大面积的中心区域的中心点应位于中心位置
							if (XScale > 70 && heightScale>50 && widthScale >10) {
								if (area > maxAreaForText) {

									maxAreaForText = area;
									textCenterMaxY = textBound.y
											+ textBound.height / 2;

								}

							}

						}
						// 如果放反了，则旋转180度（根据最大面积的区域是否在下面判断）
						if (textCenterMaxY > centerY) {

							// 处理过的样本旋转180度
							Core.flip(cleanSample, cleanSample, -1);

							// 二值化的图像也旋转180度
							Core.flip(hsvForBule, hsvForBule, -1);

							// 保存处理过的轮廓
							List<MatOfPoint> flipContours = new ArrayList<MatOfPoint>();

							// 重新查找所有轮廓
							Imgproc.findContours(hsvForBule, flipContours,
									new Mat(), Imgproc.RETR_EXTERNAL,
									Imgproc.CHAIN_APPROX_SIMPLE);

							double maxAreaFlip = 0;

							for (MatOfPoint flipContour : flipContours) {

								double area = Imgproc.contourArea(flipContour);

								// 获得该区域（可能是文字区域，可能不是）的矩形
								Rect textBound = Imgproc
										.boundingRect(flipContour);
								// 计算矩形中心点X
								int textCenterX = textBound.x + textBound.width
										/ 2;

								// 中心X比值（0-100）
								int XScale = 0;
								
								int heightScale =0;
								int widthScale=0;
								
								heightScale=cleanSample.height()*10/textBound.height;
								widthScale=cleanSample.width()*10/textBound.width;

								// 如果文字区域中心点X小于样本图片中心点X
								if (centerX > textCenterX) {
									XScale = textCenterX * 100 / centerX;
								} else {
									XScale = centerX * 100 / textCenterX;
								}

								// 30%误差精度（用于判断最大面积的中心区域的中心点应位于中心位置
								if (XScale > 70 && heightScale>50 && widthScale >10) {
									if (area > maxAreaFlip) {
										maxAreaFlip = area;

									}

								}

							}
							sampleInfo.setCleanSample(cleanSample);

							getTextOfSample(flipContours, cleanSample,
									maxAreaFlip, sampleInfo,cannyOperOnly(cleanSample,30,3));

						} else {
							sampleInfo.setCleanSample(cleanSample);
							getTextOfSample(textContours, cleanSample,
									maxAreaForText, sampleInfo,cannyOperOnly(cleanSample,30,3));
						}

					}

				}

			}

			sampleInfoList.add(sampleInfo);

		}

		// 遍历每个样本，找到检验区域，并设置结果
		for (SampleInfo sif : sampleInfoList) {

			// HashMap<String, Mat> resultMatMap = new HashMap<String, Mat>();

			List<TextInfo> tiL = sif.getTextInfoList();

			// 判断有没有CT
			boolean hasC = false;
			boolean hasT = false;

			int countS = 0;

			if (tiL == null) {
				continue;
			}
			for (TextInfo tif : tiL) {
				String text = tif.getText();
				if ("C".equalsIgnoreCase(text)) {

					hasC = true;
				} else if ("T".equalsIgnoreCase(text)) {
					hasT = true;
				} else if ("S".equalsIgnoreCase(text)) {
					countS++;

				}

			}
			// 单卡
			if (hasC && hasT && countS == 1) {

				int x = 0;
				int y = 0;
				int width = 0;
				int height = 0;
				int y1 = 0;
				int y2 = 0;
				String proName = "unknown";

				for (TextInfo tif : tiL) {
					String text = tif.getText();
					if ("C".equalsIgnoreCase(text)) {

						// C的坐标
						y = tif.getTextInSampleLeftY();
						y1 = y;

					} else if ("T".equalsIgnoreCase(text)) {
						y2 = tif.getTextInSampleLeftY() + tif.getTextHeight();

					} else if ("S".equalsIgnoreCase(text)) {

						x = tif.getTextInSampleLeftX();
						width = tif.getTextWidth();

					} else if (tif.getTextInSampleCenterY() < sif
							.getSampleHeight() / 4) {// 项目名称

						// 此处应该判断该项目是不是清单内的项目

						proName = tif.getText();

					}

				}

				// 计算监测区域高度
				height = y2 - y1;
				// 找到检测区
				Rect testRect = new Rect();
				testRect.x = x;
				testRect.y = y;
				testRect.width = width;
				testRect.height = height;

				Mat resultMat = new Mat(sif.getCleanSample(), testRect);
				HashMap<String, Mat> hm = new HashMap<String, Mat>();
				hm.put(proName, resultMat);
				sif.setResultMatMap(hm);

			} else if (countS == 3) { // 三联卡

				int diff = sif.getCleanSample().width() / 6;
				int[] sx = new int[3];
				int[] px = new int[3];

				int si = 0;
				for (TextInfo tifForS : tiL) {
					String text = tifForS.getText();
					if ("S".equalsIgnoreCase(text)) {

						sx[si] = tifForS.getTextInSampleCenterX();
						si++;
					}

				}
				Arrays.sort(sx);

				int pi = 0;
				for (TextInfo tifForP : tiL) {
					String text = tifForP.getText();
					if (!("S".equalsIgnoreCase(text))
							&& tifForP.getTextInSampleCenterY() > sif.getSampleHeight()* 15/ 70
							&& tifForP.getTextInSampleCenterY() < sif.getSampleHeight()* 22/ 70) {

						px[pi] = tifForP.getTextInSampleCenterX();
						pi++;
					}

				}
				Arrays.sort(px);

				HashMap<String, Mat> hm = new HashMap<String, Mat>();

				for (TextInfo tif : tiL) {

					int x = 0;
					int y = 0;
					int width = 0;
					int height = 0;
					int y1 = 0;
					int y2 = 0;
					// 中心X
					int x1 = 0;
					int x2 = 0;
					String proName = "unknown";
					String text = tif.getText();

					if ("S".equalsIgnoreCase(text)) {

						int locateP = 0;
						for (int j = 0; j < sx.length; j++) {
							if (tif.getTextInSampleCenterX() == sx[j]) {
								locateP = j;
								break;
							}
						}

						x2 = tif.getTextInSampleCenterX();
						y2 = tif.getTextInSampleLeftY();
						x = tif.getTextInSampleLeftX();
						width = tif.getTextWidth();

						for (TextInfo tifForSearchProName : tiL) {

							String textNotS = tifForSearchProName.getText();

							// 非S区域
							if (!("S".equalsIgnoreCase(textNotS))
									&& tifForSearchProName
											.getTextInSampleCenterX() == px[locateP]) {
								x1 = tifForSearchProName
										.getTextInSampleCenterX();

								if (Math.abs(x2 - x1) < diff) {

									y1 = tifForSearchProName
											.getTextInSampleLeftY();

									y = y1 + (y2 - y1) * 11 / 58;
									height = (y2 - y1) * 13 / 58;

									proName = tifForSearchProName.getText();
									// 找到检测区
									Rect testRect = new Rect();
									testRect.x = x;
									testRect.y = y;
									testRect.width = width;
									testRect.height = height;
									Mat resultMat = new Mat(
											sif.getCleanSample(), testRect);

									hm.put(proName, resultMat);
									break;
								}

							}

						}

					}

				}

				if (hm != null && hm.size() != 0) {
					sif.setResultMatMap(hm);

				}

			}

		}

		pictureInfo.setSampleInfoList(sampleInfoList);
		int count =0;
		for (SampleInfo sampleInfo : sampleInfoList) {
			
			count++;
			Log.i(TAG, "*********第 "+count + "个样品的检测项目******");

	
			int hei=sampleInfo.getSampleHeight();
			int wid=sampleInfo.getSampleWidth();
			
			int pix = hei*wid;
			//Log.i(TAG, pix + "像素");

			HashMap<String, Mat> hm = sampleInfo.getResultMatMap();
			if (hm == null) {
				continue;
			}
			//Log.i(TAG, i + "个");
			for (String key : hm.keySet()) {

				Mat testedMat = hm.get(key);
				//Log.i(TAG, "***********" + "宽：" + wid + "高： " + hei + " "+  pix + "像素");
				Log.i(TAG, key);
				Log.i(TAG, i + "个");
				writeImageToFile(testedMat, i, true);
				i++;
			}

		}

		baseApi.end();

	}

	public void writeImageToFile(Mat image, int i, boolean b) {

		String fileName = Environment.getExternalStorageDirectory().getPath()
				+ "/test" + i + ".png";

		File file = new File(fileName);

		Mat mBgr = new Mat();
		if (b) {
			Imgproc.cvtColor(image, mBgr, Imgproc.COLOR_RGBA2BGRA);
		} else {
			mBgr = image;
		}

		Imgcodecs.imwrite(file.getAbsolutePath(), mBgr);

	}

	private double getMaxAera(List<MatOfPoint> contours) {
		double maxArea = 0;
		for (MatOfPoint wrapper : contours) {
			double area = Imgproc.contourArea(wrapper);
			if (area > maxArea)
				maxArea = area;

		}
		return maxArea;
	}

	private void getTextOfSample(List<MatOfPoint> contours, Mat cleanSample,
			double maxArea, SampleInfo sampleInfo,Mat hsvForBule) {
		// 样本图片中心点X

		int leftX = sampleInfo.getSampleAdjustWidth() * 100 / 12;
		int rightX = sampleInfo.getSampleAdjustWidth() * 100 - leftX;

		int upY = sampleInfo.getSampleAdjustHeight() * 100 / 15;
		int downY = sampleInfo.getSampleAdjustHeight() * 100 - upY;

		// 样本图片中心点Y
		int centerY = sampleInfo.getSampleAdjustCenterY();

		List<TextInfo> textInfoList = new ArrayList<TextInfo>();
		// 遍历轮廓
		for (MatOfPoint contour : contours) {
			double area = Imgproc.contourArea(contour);
			if ((area > (0.02 * maxArea)) && (area < maxArea)) {

				// 找到该轮廓的外接矩形
				Rect boundRect = Imgproc.boundingRect(contour);
				int x = boundRect.x;
				int y = boundRect.y;
				if (x * 100 < leftX || x * 100 > rightX || y * 100 < upY
						|| y * 100 > downY) {

				} else {

					// 从原图中取出外接矩形区域
					Mat textAera = new Mat(cleanSample, boundRect);
					Mat textAeraSobel =new Mat(hsvForBule, boundRect);

					if (textAera.rows() != 0 && textAera.cols() != 0) {
						int textInSampleLeftX = boundRect.x;
						int textInSampleLeftY = boundRect.y;
						int textInSampleCenterX = boundRect.x + boundRect.width;
						int textInSampleCenterY = boundRect.y
								+ boundRect.height;
						int textWidth = textAera.width();
						int textHeight = textAera.height();
						int textCenterX = textWidth / 2;
						int textCenterY = textHeight / 2;

						TextInfo textInfo = new TextInfo();

						textInfo.setTextInSampleLeftX(textInSampleLeftX);
						textInfo.setTextInSampleLeftY(textInSampleLeftY);
						textInfo.setTextInSampleCenterX(textInSampleCenterX);
						textInfo.setTextInSampleCenterY(textInSampleCenterY);
						textInfo.setTextWidth(textWidth);
						textInfo.setTextHeight(textHeight);
						textInfo.setTextCenterX(textCenterX);
						textInfo.setTextCenterY(textCenterY);
						textInfo.setText(getRecognizText(textAera,textAeraSobel));

						textInfoList.add(textInfo);
						writeImageToFile(textAera, i, true);
						i++;

					}

				}

			}
		}

		if (textInfoList.size() != 0) {

			sampleInfo.setTextInfoList(textInfoList);
		}

	}

	private String getRecognizText(Mat textMat,Mat textAeraSobel) {

		Mat sobelAafter = adaptiveThresholdOnly(textMat,textAeraSobel);
		Bitmap bmp = Bitmap.createBitmap(sobelAafter.width(),
				sobelAafter.height(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(sobelAafter, bmp);
		baseApi.setImage(bmp);

//		baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,
//				"qwertyuiopasdfghjklzxcvbnmQWRTYUIPASDFGJKLZXCVBNM-");
		baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,
				"PpSsCcD-dimerRThnlFABN");
		baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST,
				"!@#$%^&*()_+=[]}{;:'\"\\|~`,./<>?1234567890HOE");

		baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
		String recognizedText = baseApi.getUTF8Text();
		recognizedText = recognizedText.replace(" ", "");

		Log.i(TAG, recognizedText);

		writeImageToFile(textMat, i, true);
		i++;

		return recognizedText;

	}

	// Sobel运算，对图像分割，腐蚀和膨胀的操作
	// 输入彩色图像，输出二值化图像
	private Mat sobelOper(Mat in) {

		Mat mat_blur = in.clone();
		Imgproc.GaussianBlur(in, mat_blur, new Size(5, 5), 0, 0);

		Mat mat_gray = new Mat();
		Imgproc.cvtColor(mat_blur, mat_gray, Imgproc.COLOR_RGBA2GRAY);

		writeImageToFile(mat_gray, i, false);
		i++;

		int scale = 1;
		int delta = -1;
		// CV_16S
		int ddepth = CvType.CV_16S;

		Mat grad_x = new Mat();
		Mat grad_y = new Mat();
		Mat abs_grad_x = new Mat();
		Mat abs_grad_y = new Mat();

		// 对X求导
		Imgproc.Sobel(mat_gray, grad_x, ddepth, 1, 0, 3, scale, delta);
		// 对Y求导
		Imgproc.Sobel(mat_gray, grad_y, ddepth, 0, 1, 3, scale, delta);

		Core.convertScaleAbs(grad_x, abs_grad_x);
		Core.convertScaleAbs(grad_y, abs_grad_y);

		Mat grad = new Mat();

		// 此处X和Y的比重可以调节
		Core.addWeighted(abs_grad_x, 1.5, abs_grad_y, 1, 0, grad);

		// 分割
		Mat mat_threshold = new Mat();
		Imgproc.threshold(grad, mat_threshold, 0, 255, Imgproc.THRESH_OTSU
				+ Imgproc.THRESH_BINARY);

		writeImageToFile(mat_threshold, i, false);
		i++;

		int diffhw = 0;
		if (in.height() > in.width()) {
			diffhw = in.height() / in.width();

		} else {
			diffhw = in.width() / in.height();
		}

		Mat rectClose;
		if (diffhw >= 2) {
			// 为闭合操作准备 Size(？, ？)此处的Size要根据具体情况调正
			rectClose = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
					new Size(15, 5));

		} else {
			// 为闭合操作准备 Size(？, ？)此处的Size要根据具体情况调正
			rectClose = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
					new Size(10, 20));

		}

		// 闭合操作
		Imgproc.morphologyEx(mat_threshold, mat_threshold, Imgproc.MORPH_CLOSE,
				rectClose);

		writeImageToFile(mat_threshold, i, false);
		i++;

		return mat_threshold;
	}

	private Mat cannyOper(Mat in) {

		Mat mat_blur = in.clone();
		Imgproc.GaussianBlur(in, mat_blur, new Size(5, 5), 0, 0);

		Mat mat_gray = new Mat();
		Imgproc.cvtColor(mat_blur, mat_gray, Imgproc.COLOR_RGBA2GRAY);

		writeImageToFile(mat_gray, i, false);
		i++;

		Mat edges = new Mat();

		int lowThreshold = 30;

		int ratio = 3;
		int kernel_size = 3;

		Imgproc.Canny(mat_gray, edges, lowThreshold, lowThreshold * ratio,
				kernel_size, false);

		writeImageToFile(edges, i, false);
		i++;

		// 分割

//		Imgproc.threshold(edges, mat_threshold, 0, 255, Imgproc.THRESH_OTSU
//				+ Imgproc.THRESH_BINARY);
//
//		writeImageToFile(mat_threshold, i, false);
//		i++;
		
		
		int diffhw = 0;
		if (in.height() > in.width()) {
			diffhw = in.height() / in.width();

		} else {
			diffhw = in.width() / in.height();
		}

		
		Mat rectClose;
		if (diffhw >= 2) {
			// 为闭合操作准备 Size(？, ？)此处的Size要根据具体情况调正
		
			int heightScalar=in.height()*1000/1094;
			int x=heightScalar*30/1000;
			int y=heightScalar*5/1000;
			
			rectClose = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,
					new Size(x, y));

		} else {
			int heightScalar=in.height()*1000/1094;
			int x=heightScalar*15/1000;
			int y=heightScalar*20/1000;
			// 为闭合操作准备 Size(？, ？)此处的Size要根据具体情况调正
			rectClose = Imgproc.getStructuringElement(Imgproc. MORPH_ELLIPSE,
					new Size(x, y));

		}
		

	
		Mat mat_threshold = new Mat();

		// 闭合操作
		Imgproc.morphologyEx(edges, mat_threshold, Imgproc.MORPH_CLOSE,
				rectClose);
		
		writeImageToFile(mat_threshold, i, false);
		i++;
		
		
		
		//颜色翻转
		Mat test = in.clone();
		
		writeImageToFile(in, i, true);
		i++;
		
		
		
		Mat m= new Mat(test.rows(), test.cols(), test.type(), Scalar.all(255));

		List<Mat> ls =new ArrayList<Mat>();
		
		Core.split(test,ls);
		
		for(int ml=0 ; ml< 3;ml++){
			Mat tmp = ls.get(ml);
			Core.bitwise_not(tmp,tmp);
		}
		
		Core.merge(ls, test);
		
		writeImageToFile(test, i, true);
		i++;
		
		
		cannyOperOnly(test,6,3);
		
//		
//		writeImageToFile(test, i, true);
//		i++;
		
		//test=sobelOper(test);
//		Mat m1= new Mat();
//		Mat kernel =new Mat(3,3,CvType.CV_32F,new Scalar(-1));   
//	    // 分配像素置  
//	  //  kernel.at<float>(1,1) = 8;  
//	    kernel.put(1, 1, 8.9);
//	    
//	    Imgproc.filter2D(test, m1, test.depth(), kernel);
//	   // filter2D(scr,rst,scr.depth(),kernel); 
//		
//		writeImageToFile(m1, i, true);
//		i++;


		return mat_threshold;
	}
	
	
	
	private Mat cannyOperOnly(Mat in,int lowThreshold,int ratio) {

		Mat mat_blur = in.clone();
		Imgproc.GaussianBlur(in, mat_blur, new Size(5, 5), 0, 0);

		Mat mat_gray = new Mat();
		Imgproc.cvtColor(mat_blur, mat_gray, Imgproc.COLOR_RGBA2GRAY);

		writeImageToFile(mat_gray, i, false);
		i++;

		Mat edges = new Mat();

		//int lowThreshold = 30;

		//int ratio = 3;
		int kernel_size = 3;

		Imgproc.Canny(mat_gray, edges, lowThreshold, lowThreshold * ratio,
				kernel_size, false);

		writeImageToFile(edges, i, false);
		i++;

		


		return edges;
	}

	// ! Sobel运算//对图像分割，腐蚀和膨胀的操作
	// ! 输入彩色图像，输出二值化图像
	private Mat adaptiveThresholdOnly(Mat in,Mat textAeraSobel) {

		Mat mat_blur = in.clone();
		Imgproc.GaussianBlur(in, mat_blur, new Size(5, 5), 0, 0);

		Mat mat_gray = new Mat();
		Imgproc.cvtColor(mat_blur, mat_gray, Imgproc.COLOR_RGBA2GRAY);

		writeImageToFile(mat_gray, i, false);
		i++;
		
//		 Imgproc.pyrDown(mat_gray, mat_gray);
//	     Imgproc.pyrDown(mat_gray, mat_gray);

		// 分割
		Mat mat_threshold = new Mat();

		//暂定为7
		int blockSize = 7;
		
		//越大轮廓越清楚，但会丢东西 暂定为3
		double offset = 3;
		Imgproc.adaptiveThreshold(mat_gray, mat_threshold, 255,
				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,
				blockSize, offset);
		// 
//		Imgproc.threshold(mat_gray, mat_threshold, 20, 255,
//				Imgproc.THRESH_OTSU+Imgproc.THRESH_BINARY_INV);
		
		int width= 0;
		double scalar =1.0f;
		if(mat_threshold.height()>20){
			scalar= (mat_threshold.height()*1000/20f);
			width= (int)(mat_threshold.width()*1000 /scalar);
		}
		else{
			scalar= (20f*1000/mat_threshold.height());
			width= (int)(mat_threshold.width() *scalar/1000);
		}
		
		Imgproc.resize(mat_threshold, mat_threshold, new Size(width, 20));
		//Core.multiply(mat_threshold, new Scalar(scalar,scalar), mat_threshold);
		

		writeImageToFile(mat_threshold, i, false);
		i++;
		
//		Mat result=new Mat();
//		writeImageToFile(textAeraSobel, i, false);
//		i++;
		
		
//		Core.max(mat_threshold, textAeraSobel, result);
//		writeImageToFile(result, i, false);
//		i++;
		
		
		

		return mat_threshold;
	}

	// //! Sobel运算//对图像分割，腐蚀和膨胀的操作
	// //! 输入彩色图像，输出二值化图像
	// private Mat thresholdOper(Mat in) {
	//
	// Mat mat_blur = in.clone();
	// Imgproc.GaussianBlur(in, mat_blur, new Size(5, 5), 0, 0);
	//
	// Mat mat_gray = new Mat();
	// Imgproc.cvtColor(mat_blur, mat_gray, Imgproc.COLOR_RGBA2GRAY);
	//
	// writeImageToFile(mat_gray, i, false);
	// i++;
	//
	//
	// // 分割
	// Mat mat_threshold = new Mat();
	//
	//
	// Imgproc.threshold(mat_gray, mat_threshold, 210, 255,
	// Imgproc.THRESH_BINARY_INV);
	//
	// // 为闭合操作准备 Size(？, ？)此处的Size要根据具体情况调正
	// Mat rectClose = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
	// new Size(10, 3));
	//
	// // 闭合操作
	// Imgproc.morphologyEx(mat_threshold, mat_threshold, Imgproc.MORPH_CLOSE,
	// rectClose);
	//
	// writeImageToFile(mat_threshold, i, false);
	// i++;
	//
	// return mat_threshold;
	// }

}
