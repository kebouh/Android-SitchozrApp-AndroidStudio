package com.voipsitchozr.views;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.options.SelfViewOptions;



import com.voipsitchozr.utils.ConcurrentQueue;
import com.voipsitchozr.utils.SCallback;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.io.IOException;

public class VideoSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	SurfaceHolder holder;
	LayoutParams params;
	SCallback scallBack;
	ConcurrentQueue<byte[]> queue;
	Context	context;
	SurfaceView		view;
	public VideoSurfaceView(Context context) {
		super(context);
		view = this;
		holder = this.getHolder();
		holder.addCallback(this);
		this.context = context;
	}

	public VideoSurfaceView(Context context, ConcurrentQueue<byte[]> queue) {
		super(context);
		holder = this.getHolder();
		holder.addCallback(this);
		this.queue = queue;
		this.context = context;
	}

	public void setSurfaceCreatedCallback(SCallback cb) {
		scallBack = cb;
	}

	public	void	setSelfOptions()
	{
		this.setSize(SelfViewOptions.width, SelfViewOptions.height);
		this.setPartOfScreenForSelf(SelfViewOptions.partOfParent);
		//this.setRotationX(90);
		this.setPosition(SelfViewOptions.x, SelfViewOptions.y);
		this.setLayoutParams(params);
	}

	public	void	setContactOptions()
	{
		this.setSize(ContactViewOptions.width, ContactViewOptions.height);
		this.setPartOfScreenForContact(ContactViewOptions.partOfParent);
		this.setPosition(ContactViewOptions.x, ContactViewOptions.y);
		this.setLayoutParams(params);
	}

	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d("suface view", "Surface Created: " + this.getId());
		params = this.getLayoutParams();
		//this.setPosition(0, 0);
		if (this.getId() == 0)
		setMoveListener();
		if (getId() == 0)
			setSelfOptions();
		else
			setContactOptions();
		if (scallBack != null)
			try {
				scallBack.callbackSurfaceCreated();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.d("suface view", "Surface Changed: " + this.getId());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d("suface view", "Surface Destroyed: " + this.getId());
	}

	public void setSize(int width, int height) {
		params.height = height;
		params.width = width;
	}

	public void setPartOfScreenForSelf(int part) {
		if (SelfViewOptions.partOfParent != -1)
		{
			params.height = VoipManager.heightScreen / part;
			params.width = VoipManager.widthScreen / part;
		}
	}
	
	public void setPartOfScreenForContact(int part) {
		if (ContactViewOptions.partOfParent != -1)
		{
			params.height = VoipManager.heightScreen / part;
			params.width = VoipManager.widthScreen / part;
			
		}
	}

	public void setPosition(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	public float getXPosition() {
		return this.getX();
	}

	public float getYPosition() {
		return this.getY();
	}

	public void setOverlay(boolean val) {
		this.setZOrderOnTop(val);
	}
	
	
	public	void	setMoveListener()
	{
		this.setOnTouchListener(new OnTouchListener() {
			PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
			PointF StartPT = new PointF(); // Record Start Position of 'img'

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int eid = event.getAction();
				switch (eid) {
					case MotionEvent.ACTION_MOVE:
						PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
						view.setX((int) (StartPT.x + mv.x));
						view.setY((int) (StartPT.y + mv.y));
						StartPT = new PointF(view.getX(), view.getY());
						System.out.println("X: " + view.getX() + " - Y: " + view.getY());
						break;
					case MotionEvent.ACTION_DOWN:
						DownPT.x = event.getX();
						DownPT.y = event.getY();
						StartPT = new PointF(v.getX(), v.getY());
						break;
					case MotionEvent.ACTION_UP:
						// Nothing have to do
						break;
					default:
						break;
				}
				return true;
			}
		});
	}
}
