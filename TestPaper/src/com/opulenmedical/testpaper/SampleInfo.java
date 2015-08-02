package com.opulenmedical.testpaper;

import java.util.HashMap;
import java.util.List;

import org.opencv.core.Mat;

public class SampleInfo {
	
	private int sampleInPictureLeftX;
	private int sampleInPictureLeftY;
	private int sampleInPictureCenterX ;
	private int sampleInPictureCenterY;
	private int sampleWidth ;
	private int sampleHeight ;
	private int sampleCenterX ;
	private int sampleCenterY;
	private int sampleRotatedWidth ;
	private int sampleRotatedHeight ;
	private int sampleRotatedCenterX ;
	private int sampleRotatedCenterY;
	private int sampleAdjustWidth ;
	private int sampleAdjustHeight ;
	private int sampleAdjustCenterX ;
	private int sampleAdjustCenterY;
	private List<TextInfo> textInfoList;
	private Mat cleanSample;
	private HashMap<String,Mat> resultMatMap;
	public int getSampleInPictureLeftX() {
		return sampleInPictureLeftX;
	}
	public void setSampleInPictureLeftX(int sampleInPictureLeftX) {
		this.sampleInPictureLeftX = sampleInPictureLeftX;
	}
	public int getSampleInPictureLeftY() {
		return sampleInPictureLeftY;
	}
	public void setSampleInPictureLeftY(int sampleInPictureLeftY) {
		this.sampleInPictureLeftY = sampleInPictureLeftY;
	}
	public int getSampleInPictureCenterX() {
		return sampleInPictureCenterX;
	}
	public void setSampleInPictureCenterX(int sampleInPictureCenterX) {
		this.sampleInPictureCenterX = sampleInPictureCenterX;
	}
	public int getSampleInPictureCenterY() {
		return sampleInPictureCenterY;
	}
	public void setSampleInPictureCenterY(int sampleInPictureCenterY) {
		this.sampleInPictureCenterY = sampleInPictureCenterY;
	}
	public int getSampleWidth() {
		return sampleWidth;
	}
	public void setSampleWidth(int sampleWidth) {
		this.sampleWidth = sampleWidth;
	}
	public int getSampleHeight() {
		return sampleHeight;
	}
	public void setSampleHeight(int sampleHeight) {
		this.sampleHeight = sampleHeight;
	}
	public int getSampleCenterX() {
		return sampleCenterX;
	}
	public void setSampleCenterX(int sampleCenterX) {
		this.sampleCenterX = sampleCenterX;
	}
	public int getSampleCenterY() {
		return sampleCenterY;
	}
	public void setSampleCenterY(int sampleCenterY) {
		this.sampleCenterY = sampleCenterY;
	}
	public int getSampleRotatedWidth() {
		return sampleRotatedWidth;
	}
	public void setSampleRotatedWidth(int sampleRotatedWidth) {
		this.sampleRotatedWidth = sampleRotatedWidth;
	}
	public int getSampleRotatedHeight() {
		return sampleRotatedHeight;
	}
	public void setSampleRotatedHeight(int sampleRotatedHeight) {
		this.sampleRotatedHeight = sampleRotatedHeight;
	}
	public int getSampleRotatedCenterX() {
		return sampleRotatedCenterX;
	}
	public void setSampleRotatedCenterX(int sampleRotatedCenterX) {
		this.sampleRotatedCenterX = sampleRotatedCenterX;
	}
	public int getSampleRotatedCenterY() {
		return sampleRotatedCenterY;
	}
	public void setSampleRotatedCenterY(int sampleRotatedCenterY) {
		this.sampleRotatedCenterY = sampleRotatedCenterY;
	}
	public int getSampleAdjustWidth() {
		return sampleAdjustWidth;
	}
	public void setSampleAdjustWidth(int sampleAdjustWidth) {
		this.sampleAdjustWidth = sampleAdjustWidth;
	}
	public int getSampleAdjustHeight() {
		return sampleAdjustHeight;
	}
	public void setSampleAdjustHeight(int sampleAdjustHeight) {
		this.sampleAdjustHeight = sampleAdjustHeight;
	}
	public int getSampleAdjustCenterX() {
		return sampleAdjustCenterX;
	}
	public void setSampleAdjustCenterX(int sampleAdjustCenterX) {
		this.sampleAdjustCenterX = sampleAdjustCenterX;
	}
	public int getSampleAdjustCenterY() {
		return sampleAdjustCenterY;
	}
	public void setSampleAdjustCenterY(int sampleAdjustCenterY) {
		this.sampleAdjustCenterY = sampleAdjustCenterY;
	}
	public List<TextInfo> getTextInfoList() {
		return textInfoList;
	}
	public void setTextInfoList(List<TextInfo> textInfoList) {
		this.textInfoList = textInfoList;
	}
	public HashMap<String, Mat> getResultMatMap() {
		return resultMatMap;
	}
	public void setResultMatMap(HashMap<String, Mat> resultMatMap) {
		this.resultMatMap = resultMatMap;
	}
	public Mat getCleanSample() {
		return cleanSample;
	}
	public void setCleanSample(Mat cleanSample) {
		this.cleanSample = cleanSample;
	}
	
	

	
	
	
	
	


}
