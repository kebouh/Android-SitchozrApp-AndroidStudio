package com.voipsitchozr.options;

import android.hardware.Camera;


@SuppressWarnings("deprecation")
public class CameraOptions {

	static public int FRONT_CAMERA = Camera.CameraInfo.CAMERA_FACING_FRONT;
	static public int BACK_CAMERA = Camera.CameraInfo.CAMERA_FACING_BACK;
	static public int initWidth = -1;
	static public int initHeight = -1;
	static public int width = -1;
	static public int height = -1;
	static public int minFps = 10;
	static public int maxFps = 25;
	static public int fps = 15;
	static public int compressionQuality = 50;
	static public int queueLimit = 10;
	static public int currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
	static public boolean recommendedPreviewSize = false;
}