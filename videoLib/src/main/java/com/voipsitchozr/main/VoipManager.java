package com.voipsitchozr.main;

import java.io.IOException;
import java.net.SocketException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.socket.UdpSocketAudio;
import com.voipsitchozr.socket.UdpSocketVideo;
import com.voipsitchozr.utils.AudioManagerState;
import com.voipsitchozr.utils.ConcurrentQueue;
import com.voipsitchozr.utils.SCallback;
import com.voipsitchozr.views.ChatLayout;
import com.voipsitchozr.views.ControllerLayout;
import com.voipsitchozr.views.VideoSurfaceView;

import com.voipsitchozr.camera.CameraManager;
import com.voipsitchozr.chat.ChatView;
import com.voipsitchozr.tcp.TcpManager;

public class VoipManager {

	public	static	int widthScreen = 0;
	public	static	int	heightScreen = 0;
	public  CameraManager mCamera;
	private VideoSurfaceView contactView;
	private VideoSurfaceView selfView;
	private SCallback callback = null;
	public 	static Context context = null;
	public Activity activity = null;
	private FrameLayout frameLayout = null;
	private ConcurrentQueue<byte[]> queue;
	public	ChatView chatView;
	private TcpManager tcpManager = null;
	private UdpSocketVideo udpSocket;
	private UdpSocketAudio		socketAudio;
	private boolean				videoMode = false;
	private boolean				audioMode = false;
	private boolean				controllerMode = false;
	private boolean				chatMode = false;
	private static VoipManager  voipManager = null;
	public boolean				isInCall = false;

	public VoipManager(Context context) {
		this.context = context;
		AudioManagerState.initAndSaveState(context);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		this.widthScreen = metrics.widthPixels;
		this.heightScreen = metrics.heightPixels;
		voipManager = this;
		this.queue = new ConcurrentQueue<byte[]>();
	}

	public void setVideoMode(boolean val) {videoMode = val;}
	public void setAudioMode(boolean val) {audioMode = val;}
	public void setControllerMode(boolean val) {controllerMode = val;}
	public void setChatMode(boolean val) {chatMode = val;}

	public static VoipManager getInstance() { return VoipManager.voipManager; }

	@SuppressLint("NewApi")
	public void initialiaze(Activity activity, FrameLayout frameLayout) throws IOException {

		isInCall = true;
		this.activity = activity;
		this.frameLayout = frameLayout;

		if (videoMode) {
			this.mCamera = new CameraManager();
			this.callback = new CallbackSelfSurface();
			mCamera.initCamera();
			initContactView();
			initSelfView();
			frameLayout.addView(contactView);
		}

		if (chatMode) {
			chatView = new ChatView(context, frameLayout);
			chatView.initChatManager();
		}
		ControllerLayout controller = null;
		if (controllerMode) {
			controller = new ControllerLayout(context, frameLayout, mCamera, selfView.getHolder());
			controller.addWidgets(selfView, this);
		}
		if (videoMode)
		frameLayout.addView(selfView);
		if (controllerMode)
		frameLayout.addView(controller);
		startSendAudio();
	}

	public void initializeVideo() {
		this.callback = new CallbackSelfSurface();
		mCamera.initCamera();

		initContactView();
		initSelfView();
		frameLayout.addView(contactView);
		frameLayout.addView(selfView);
	}

	public void initializeChat() {
		chatView = new ChatView(context, frameLayout);
		chatView.initChatManager();
	}

	public void initializeController() {
		ControllerLayout controller = new ControllerLayout(context, frameLayout, mCamera, selfView.getHolder());
		controller.addWidgets(selfView, this);
		frameLayout.addView(controller);
	}

		public void		initializeTcpConnexion ()throws SocketException {

		tcpManager = new TcpManager(context);
		try {
			tcpManager.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void startSendAudio() {
		socketAudio = new UdpSocketAudio(null, ConnexionOptions.AUDIO_PORT);
		socketAudio.start();
	}

	public	void startSendVideo() throws SocketException {
		System.out.println("udp: start send viudeo");
		udpSocket = new UdpSocketVideo(queue, ConnexionOptions.VIDEO_PORT);
		udpSocket.start(contactView);

		/*DatagramSocketSender sock = new DatagramSocketSender("127.0.0.1", port);
		videoSender.setDatagramSocket(sock);
		videoSender.start();

		videoReceiver.setDatagramSocket(new DatagramSocketReceiver(sock.getDatagram()));
		videoReceiver.initAndStartVideoReceiver(contactView);*/
		//videoReceiver.start();
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
          	startSendVideo();
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
	{
		isInCall = true;
		try {
			mCamera.startPreviewStream(selfView.getHolder());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*
		videoSender.start();
		videoReceiver.start();*/
	}
	
	public	void	onStop()
	{
		isInCall = false;
		AudioManagerState.restoreState();
		mCamera.stopPreviewStream();/*
	if (videoSender != null && videoSender.isAlive())
		videoSender.interrupt();*/
		if (udpSocket != null)
			udpSocket.onStop();
		//tcpManager.interrupt();
		if (udpSocket != null)
		udpSocket.onStop();
		if (socketAudio != null)
		socketAudio.onStop();
		AudioManagerState.restoreState();
		/*if (videoReceiver != null && videoReceiver.isAlive())
		videoReceiver.interrupt();
		videoReceiver.*/
		activity.finish();
	}
	
	
	public void		onPause()
	{
		mCamera.stopPreviewStream();

/*
		if (udpSocket != null)
		udpSocket.onStop();
		if (socketAudio != null)
		socketAudio.onStop();
		AudioManagerState.restoreState();
		((Activity)context).finish();
*/
	}


	public TcpManager getTcpManager() {return tcpManager;}
}