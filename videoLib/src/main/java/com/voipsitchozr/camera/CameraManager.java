package com.voipsitchozr.camera;

import java.io.IOException;
import java.util.List;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.CameraOptions;

import com.voipsitchozr.utils.SCallback;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

@SuppressWarnings("deprecation")
public class CameraManager implements Camera.PreviewCallback {

	private Camera mCamera;
	private Parameters params;
	private SCallback previewCallback = null;
	private SurfaceHolder holder;
	public static int orientation = 0;
	/*private int	tempWidth;
	private int	tempHeight;*/
	

	public CameraManager() {
		mCamera = Camera.open(CameraOptions.currentCameraId);
		params = mCamera.getParameters();
	}

	public void addPreviewCallBack(SCallback cb) {
		previewCallback = cb;
	}

	public void initCamera() {
		mCamera.setPreviewCallback(this);
	}

    public	void	startPreviewStream(SurfaceHolder holder) throws IOException {

		orientation = getCameraDisplayOrientation(CameraOptions.currentCameraId, null);
		if (CameraOptions.currentCameraId == CameraOptions.FRONT_CAMERA)
			orientation *= -1;
		if (holder != null)
        this.holder = holder;
        this.setOptions();
		this.mCamera.setPreviewDisplay(holder);
		this.mCamera.setPreviewCallback(this);
       // mCamera.setDisplayOrientation(getCameraDisplayOrientation(CameraOptions.currentCameraId, null));
        //params.setRotation(90);
        //setCameraDisplayOrientation(CameraOptions.currentCameraId, mCamera);
		this.mCamera.startPreview();

    }

	public void setOptions() {
	/*	tempHeight = CameraOptions.height;
		tempWidth = CameraOptions.width;*/
		setPreviewSize();
		//setFpsRange();
		//setFrameRate();
		params.set("orientation", "portrait");
		//params.set("rotation", 90);

		mCamera.setDisplayOrientation(getCameraDisplayOrientation(CameraOptions.currentCameraId, null));
		mCamera.setParameters(params);
	}

	public void setPreviewSize() {
		if (CameraOptions.recommendedPreviewSize || CameraOptions.width == -1
				|| CameraOptions.height == -1) {
			CameraOptions.width = params.getPreferredPreviewSizeForVideo().width;
			CameraOptions.height = params.getPreferredPreviewSizeForVideo().height;
		}
		Size size = getOptimalPreviewSize(params.getSupportedPreviewSizes(), CameraOptions.width, CameraOptions.height);
		CameraOptions.width = size.width;
		CameraOptions.height = size.height;
		params.setPreviewSize(size.width, size.height);
		//params.setPreviewSize(CameraOptions.width, CameraOptions.height);
	}

	public Size 			getPreviewSize() {
		return params.getPreviewSize();
	}
	public List<Size> 		getListPreviewSizes() {
		return params.getSupportedPreviewSizes();
	}
	public List<Integer>	getListFramesRate() {
		return params.getSupportedPreviewFrameRates();
	}
	public int 				getFrameRate() { return params.getPreviewFrameRate(); }

	public void setFpsRange() {
		if (CameraOptions.minFps != -1 && CameraOptions.maxFps != -1) {
			params.setPreviewFpsRange(CameraOptions.minFps,
					CameraOptions.maxFps);
		}
	}

	public void setFrameRate() {
		if (CameraOptions.fps != -1) {
			params.setPreviewFrameRate(CameraOptions.fps);
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (previewCallback != null)
			previewCallback.callbackPreviewFrames(data);
	}

	public void setPreviewDisplay(SurfaceHolder holder) throws IOException {
		if (mCamera != null)
		mCamera.setPreviewDisplay(holder);
	}

	public	void	stopPreviewStream()
	{
		if (mCamera != null)
		{
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		}
	}

	public void		openCamera()
	{
		mCamera = Camera.open(CameraOptions.currentCameraId);
		params = mCamera.getParameters();
	}
	
	public void switchCamera() {
		if (mCamera != null && Camera.getNumberOfCameras() > 1) {
			stopPreviewStream();
			if (CameraOptions.currentCameraId == CameraOptions.BACK_CAMERA)
			{
				CameraOptions.currentCameraId = CameraOptions.FRONT_CAMERA;
				//CameraOptions.recommendedPreviewSize = true;
			}
			else
			{
				CameraOptions.currentCameraId = CameraOptions.BACK_CAMERA;
				//CameraOptions.recommendedPreviewSize = false;
			}
			try {
				openCamera();
				startPreviewStream(holder);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mCamera.release();
			}
		}
	}
	
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	    final double ASPECT_TOLERANCE = 0.05;
	    double targetRatio = (double) w/h;

	    if (sizes==null) return null;

	    Size optimalSize = null;

	    double minDiff = Double.MAX_VALUE;

	    int targetHeight = h;

	    // Find size
	    for (Size size : sizes) {
	        double ratio = (double) size.width / size.height;
	        if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	        if (Math.abs(size.height - targetHeight) < minDiff) {
	            optimalSize = size;
	            minDiff = Math.abs(size.height - targetHeight);
	        }
	    }

	    if (optimalSize == null) {
	        minDiff = Double.MAX_VALUE;
	        for (Size size : sizes) {
	            if (Math.abs(size.height - targetHeight) < minDiff) {
	                optimalSize = size;
	                minDiff = Math.abs(size.height - targetHeight);
	            }
	        }
	    }
	    return optimalSize;
	}
	public static int getCameraDisplayOrientation(
	         int cameraId, Camera camera) {
	     Camera.CameraInfo info =
	             new Camera.CameraInfo();
	     Camera.getCameraInfo(cameraId, info);
	     int rotation = ((Activity)VoipManager.context).getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
		return result;
	 }
	 
}
