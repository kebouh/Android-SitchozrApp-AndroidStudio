package com.voipsitchozr.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;

import media.VideoReceiver;
import media.VideoSender;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.socket.UdpSocket;
import com.voipsitchozr.utils.ConcurrentQueue;
import com.voipsitchozr.utils.SCallback;
import com.voipsitchozr.views.ChatLayout;
import com.voipsitchozr.views.ControllerLayout;
import com.voipsitchozr.views.VideoSurfaceView;

import com.voipsitchozr.camera.CameraManager;
import com.voipsitchozr.chat.ChatView;
import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.socket.DatagramSocketReceiver;
import com.voipsitchozr.socket.DatagramSocketSender;
import com.voipsitchozr.tcp.TcpManager;

public class VoipManager {

	public	static	int widthScreen = 0;
	public	static	int	heightScreen = 0;
	
	private VideoSender	videoSender;
	private VideoReceiver	videoReceiver;
	public  CameraManager mCamera;
	private VideoSurfaceView contactView;
	private VideoSurfaceView selfView;
	private SCallback callback = null;
	public Context context = null;
	private FrameLayout frameLayout = null;
	private ConcurrentQueue<byte[]> queue;
	private ChatLayout chatLayout;
	private ChatView chatView;
	public static Activity	activity;
	TcpManager tcpManager = null;
	UdpSocket udpSocket;

	public VoipManager(Activity activity, FrameLayout frameLayout) {
		this.activity = activity;
		this.context = activity;
		this.callback = new CallbackSelfSurface();
		this.frameLayout = frameLayout;

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		this.widthScreen = metrics.widthPixels;
		this.heightScreen = metrics.heightPixels;

		this.queue = new ConcurrentQueue<byte[]>();
		this.mCamera = new CameraManager();
		//this.videoSender = new VideoSender(queue);
		//this.videoReceiver = new VideoReceiver();
	}

	@SuppressLint("NewApi")
	public void initialiazeViews() throws IOException {

        mCamera.initCamera();

        initContactView();
		initSelfView();

		frameLayout.addView(contactView);

		chatView = new ChatView(context, frameLayout);
		chatView.initChatManager();

		ControllerLayout controller = new ControllerLayout(context, frameLayout, mCamera, selfView.getHolder());
		controller.addWidgets(selfView, this);

		frameLayout.addView(selfView);
		frameLayout.addView(controller);
	}


		public void		initializeConnexion ()throws SocketException {

		tcpManager = new TcpManager(context, chatView, this);

		try {
			tcpManager.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //todo: uncomment tcp
	}
	
	private	void	initSelfView()
	{
		selfView = new VideoSurfaceView(context);
		selfView.setSurfaceCreatedCallback(callback);
		selfView.setId(0);
		selfView.setZOrderOnTop(true);
        selfView.setZOrderMediaOverlay(true);
	}
	
	private	void	initContactView()
	{
		contactView = new VideoSurfaceView(context, queue);
		contactView.setSurfaceCreatedCallback(new CallbackContactSurfaceCreated());
		contactView.setId(1);
        //contactView.setZOrderMediaOverlay(true);
	}

	public	void startSendVideo(int port) throws SocketException {
		udpSocket = new UdpSocket(queue, port);
		udpSocket.start(contactView);
		/*DatagramSocketSender sock = new DatagramSocketSender("127.0.0.1", port);
		videoSender.setDatagramSocket(sock);
		videoSender.start();

		videoReceiver.setDatagramSocket(new DatagramSocketReceiver(sock.getDatagram()));
		videoReceiver.initAndStartVideoReceiver(contactView);*/
		//videoReceiver.start();
	}

	public void disconnect() {
		tcpManager.disconnect();
		onStop();
		activity.finish();
	}

	private class CallbackSelfSurface implements SCallback {

		@Override
		public void callbackSurfaceCreated() throws IOException {

			mCamera.addPreviewCallBack(this);
			try {
				mCamera.setPreviewDisplay(selfView.getHolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
			mCamera.startPreviewStream(selfView.getHolder());
            System.out.println("Camera height " + String.valueOf(mCamera.getPreviewSize().height));
			System.out.println("Camera width " + String.valueOf(mCamera.getPreviewSize().width));
            //videoSender.start(); //TODO: start from message code received
        }

		@Override
		public void callbackPreviewFrames(byte[] data) {
			if (queue.getSize() > CameraOptions.queueLimit)
				queue.clear();
			queue.add(data);
		}
	}

	class CallbackContactSurfaceCreated implements SCallback {

		@Override
		public void callbackSurfaceCreated() {

          System.out.println("SURFACE CONTACT CREATED");
            FrameLayout cl = new FrameLayout(context);
            FrameLayout.LayoutParams paramsc = (FrameLayout.LayoutParams)contactView.getLayoutParams();
            paramsc.gravity = Gravity.CENTER;
			//paramsc.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            //cl.setLayoutParams(paramsc);
            //videoReceiver.initAndStartVideoReceiver(contactView);
		}

		@Override
		public void callbackPreviewFrames(byte[] data) {
			// TODO Auto-generated method stub
		}
	}

	
	public	void	onResume()
	{/*
		try {
			mCamera.startPreviewStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		videoSender.start();
		videoReceiver.start();*/
	}
	
	public	void	onStop()
	{
		mCamera.stopPreviewStream();/*
	if (videoSender != null && videoSender.isAlive())
		videoSender.interrupt();*/
		if (udpSocket != null)
			udpSocket.onStop();
		tcpManager.interrupt();
		if (videoReceiver != null && videoReceiver.isAlive())
		videoReceiver.interrupt();
		activity.finish();
	}
	
	
	public void		onPause()
	{
		mCamera.stopPreviewStream();
		if (udpSocket != null)
		udpSocket.onStop();
	/*if (videoSender != null && videoSender.isAlive())
		videoSender.interrupt();*/
		if (videoReceiver != null && videoReceiver.isAlive())
		videoReceiver.interrupt();
		activity.finish();
	}

	public String		getLocalIp()
	{
		WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		return ip;
	}

	public CameraManager getCamera()
	{
		return mCamera;
	}
}