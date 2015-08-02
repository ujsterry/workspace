package com.opulenmedical.testpaper;

import java.util.List;

public class PictureInfo {
	
	private int pictureWidth ;
	private int pictureHeight ;
	private int pictureCenterX ;
	private int pictureCenterY; 
	private List<SampleInfo> sampleInfoList;
	
	public int getPictureWidth() {
		return pictureWidth;
	}
	public void setPictureWidth(int pictureWidth) {
		this.pictureWidth = pictureWidth;
	}
	public int getPictureHeight() {
		return pictureHeight;
	}
	public void setPictureHeight(int pictureHeight) {
		this.pictureHeight = pictureHeight;
	}
	public int getPictureCenterX() {
		return pictureCenterX;
	}
	public void setPictureCenterX(int pictureCenterX) {
		this.pictureCenterX = pictureCenterX;
	}
	public int getPictureCenterY() {
		return pictureCenterY;
	}
	public void setPictureCenterY(int pictureCenterY) {
		this.pictureCenterY = pictureCenterY;
	}
	public List<SampleInfo> getSampleInfoList() {
		return sampleInfoList;
	}
	public void setSampleInfoList(List<SampleInfo> sampleInfoList) {
		this.sampleInfoList = sampleInfoList;
	}

	
	

}
