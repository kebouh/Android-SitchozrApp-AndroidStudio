package media;

import java.io.ByteArrayOutputStream;

import com.voipsitchozr.camera.CameraManager;
import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.packet.DatagramPacketWraper;
import com.voipsitchozr.socket.DatagramSocketSender;
import com.voipsitchozr.thread.AbstractSendPacketThread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import com.voipsitchozr.utils.ConcurrentQueue;

public class VideoSender /* extends AbstractSendPacketThread <byte[]>*/
{

	private static VideoSender videoSender = null;
	public		VideoSender(/*ConcurrentQueue<byte[]> queue*/)
	{
		//super(queue);
	}

	public static VideoSender getInstance() {
		if (videoSender == null)
			videoSender = new VideoSender();
		return videoSender;
	}

	public	 DatagramPacketWraper	preparePacket(byte[] copy)
	{
		if (copy == null)
		{
				System.out.println("data is null in sending");
				return null;
		}
		//System.out.println("queue sender: " + super.queue.getSize());
		YuvImage yuv_image = new YuvImage(copy, ImageFormat.NV21, CameraOptions.width,
				CameraOptions.height, null);
		Rect rect = new Rect(0, 0, CameraOptions.width, CameraOptions.height);
		DatagramPacketWraper pc = new DatagramPacketWraper(rotateBitmap(yuv_image, CameraManager.orientation, rect));
		pc.createPacket();
		return pc;
	}

	private byte[] rotateBitmap(YuvImage yuvImage, int orientation, Rect rectangle)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		yuvImage.compressToJpeg(rectangle, CameraOptions.compressionQuality, os);

		Matrix matrix = new Matrix();
		byte[] bytes = os.toByteArray();
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		//if (bitmap.getHeight() > bitmap.getWidth())
		matrix.postRotate(orientation);


		Bitmap n = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		n.compress(Bitmap.CompressFormat.JPEG, CameraOptions.compressionQuality, stream);
		return stream.toByteArray();
	}
}
