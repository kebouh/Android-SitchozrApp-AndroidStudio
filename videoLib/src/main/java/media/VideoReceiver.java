package media;

import java.io.IOException;

import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.socket.DatagramSocketReceiver;

import com.voipsitchozr.utils.ConcurrentQueue;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Base64;
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
		while (!Thread.currentThread().isInterrupted()) {
			try {
				System.out.println("TCP LOOP THREAD RECEIVER");
				Log.d("UDP", "loop thread");
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
			System.out.println("TCP Frame is null in receiving");
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
							//c.setMatrix(matrix);
							c.drawBitmap(/*getResizedBitmap(*/bmp/*, ContactViewOptions.width, ContactViewOptions.height)*/, null, new RectF(0, 0, ContactViewOptions.width, ContactViewOptions.height), null);
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
				while (!Thread.currentThread().isInterrupted()) {
					if (!queue.isEmpty()) {
						System.out.println("DRAWFRAME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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