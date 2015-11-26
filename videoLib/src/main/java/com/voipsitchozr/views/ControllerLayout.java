package com.voipsitchozr.views;


import com.example.voipsitchozr.R;
import com.voipsitchozr.camera.CameraManager;
import com.voipsitchozr.main.VoipManager;

import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

public class ControllerLayout extends RelativeLayout {

	Context	context;
	Button	mute;
	Button	cam;
	Button	changeCam;
	Button	hung;
	
	boolean	muteActivated = false;
	boolean	camActivated = true;

	
	LayoutParams	paramMute;
	LayoutParams	paramCam;
	LayoutParams	paramChangeCam;
	LayoutParams	paramHung;
	
	FrameLayout		frameLayout;
	
	ControllerLayout	controller;
	CameraManager		mCamera;
	SurfaceHolder holder;
	public ControllerLayout(Context context, FrameLayout frameLayout, CameraManager mCamera, SurfaceHolder holder) {
		super(context);

		this.holder = holder;
		controller = this;
		this.frameLayout = frameLayout;
		this.context = context;
		this.mCamera = mCamera;
		mute = new Button(context);
		cam = new Button(context);
		changeCam = new Button(context);
		hung = new Button(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//context.getResources().getDrawable(android.R.drawable.vide)
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		this.setLayoutParams(params);
		
		hung.setId(39);
		mute.setId(40);
		cam.setId(41);
		changeCam.setId(42);
		
		paramHung = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramMute = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramCam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramChangeCam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		paramHung.addRule(RelativeLayout.LEFT_OF, cam.getId());
		paramCam.addRule(RelativeLayout.CENTER_IN_PARENT);
		//paramMute.addRule(RelativeLayout.LEFT_OF, cam.getId());
		paramChangeCam.addRule(RelativeLayout.RIGHT_OF, cam.getId());
		// TODO Auto-generated constructor stub
		hung.setLayoutParams(paramHung);
		mute.setLayoutParams(paramMute);
		cam.setLayoutParams(paramCam);
		changeCam.setLayoutParams(paramChangeCam);
	}

	public	void	addWidgets(VideoSurfaceView chatLayout, VoipManager voipManager)
	{
		this.setBackgroundColor(Color.parseColor("#802c3e50"));
		
		hung.setBackgroundResource(R.drawable.ic_hung_up);
		mute.setBackgroundResource(R.drawable.ic_no_mute);
		cam.setBackgroundResource(R.drawable.ic_videocam);
		changeCam.setBackgroundResource(R.drawable.ic_switch_cam);
		this.addView(hung);
		this.addView(cam);
		//this.addView(mute);
		this.addView(changeCam);
		addListeners(chatLayout, voipManager);
	}

	public	void	addListeners(VideoSurfaceView chatLayout, final VoipManager voipManager)
	{
		chatLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		System.out.println("Clicked!");
				if (controller.getVisibility() == View.INVISIBLE)
				{
					Animation bottomDown = AnimationUtils.loadAnimation(context,
			            R.animator.bottom_down);

					controller.startAnimation(bottomDown);
					controller.setVisibility(View.VISIBLE);
				}
				else
				{
					Animation bottomUp = AnimationUtils.loadAnimation(context,
				            R.animator.bottom_up);

						controller.startAnimation(bottomUp);
						controller.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		mute.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!muteActivated)
				{
					muteActivated = true;
					mute.setBackgroundResource(R.drawable.ic_mute);
				}
				else
				{
					muteActivated = false;
					mute.setBackgroundResource(R.drawable.ic_no_mute);
				}
			}
		});
		
		cam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!camActivated)
				{
					try {
						mCamera.openCamera();
						mCamera.startPreviewStream(holder);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					camActivated = true;
					cam.setBackgroundResource(R.drawable.ic_videocam);
				}
				else
				{
					mCamera.stopPreviewStream();
					camActivated = false;
					cam.setBackgroundResource(R.drawable.ic_videocam_off);
				}
			}
		});
		
		changeCam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.switchCamera();
			}
		});
		
		hung.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				voipManager.disconnect();
			}
		});
	}
}
