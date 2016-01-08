package media;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.example.voipsitchozr.R;
import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.socket.DatagramSocketReceiver;

import com.voipsitchozr.utils.ConcurrentQueue;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

public class VideoReceiver  extends Thread{
	static int a = 0;
	SurfaceView view;
	SurfaceHolder holder;
	int port;
	int viewWidth;
	int viewHeight;
	ConcurrentQueue<byte[]> queue;
	DatagramSocketReceiver socket;
	Matrix matrix ;

	
	public VideoReceiver() {
		queue = new ConcurrentQueue<byte[]>();

	}

	public void		initAndStartVideoReceiver(SurfaceView view)
	{
		this.view = view;
		this.holder = view.getHolder();
		viewWidth = view.getWidth();
		viewHeight = view.getHeight();
		this.start();
	}

	@Override
	public void run() {
		threadDraw();
		//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		while (!Thread.currentThread().isInterrupted()) {
			try {
				System.out.println("udp: receive packet");
				if (queue.getSize() > CameraOptions.queueLimit)
					queue.clear();
				queue.add(socket.receivePacket());
			} catch (IOException e) {
				e.printStackTrace();
				socket.close();
			}
		}
	}

	public void drawFrame(byte[] frame) {
		if (frame == null)
		{
			return;
		}
		Log.d("FRAME", frame.toString());
/*
		String imgString = Base64.encodeToString(frame,	Base64.NO_WRAP);
		imgString = imgString.replace("data:image/jpeg;","");

		byte[] str = Base64.decode(imgString, Base64.DEFAULT);
*/
		Bitmap bmp = BitmapFactory.decodeByteArray(frame, 0, frame.length);
		Canvas c = null;
		if (bmp != null) {
			try {
				c = holder.lockCanvas();
				synchronized (holder) {
					if (c != null) {

						Bitmap resc = rescaleBitmap(bmp);
						Paint paint = new Paint();
						paint.setFilterBitmap(true);
						System.out.println("size: screen " + VoipManager.widthScreen + " " + resc.getHeight() + " " + resc.getWidth());
						c.drawBitmap(resc, 0, 0, paint);
						//}
					}
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
					bmp.recycle();
				}
			}
		}
	}



	public Bitmap rescaleBitmap(Bitmap bmp) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)VoipManager.context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;

		Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, width, (width - bmp.getWidth()) + bmp.getHeight(), true);

		return resizedbitmap;
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleHeight, scaleWidth);
		matrix.postRotate(-90);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}




	public void threadDraw() {

		Thread thread = new Thread() {
			@Override
			public void run() {
				//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				while (!Thread.currentThread().isInterrupted()) {
					if (!queue.isEmpty()) {
						byte[] data = queue.poll();
						if (data != null)
						drawFrame(data);
						else
							System.out.println("packet == NULLLLLLLLLL");
					}
				}

			}
		};
		thread.start();
	}

	public void setDatagramSocket(DatagramSocketReceiver socket) {
		this.socket = socket;
	}

	public void	setSurfaceViewHolder(SurfaceHolder holder)
	{
		this.holder = holder;
	}


}
