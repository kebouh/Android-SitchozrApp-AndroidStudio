package memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sources.sitchozt.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class ImageLoader {

	WindowManager wm;
	Display display;
	int width;
	int height;
	MemoryCache memoryCache = new MemoryCache();
	Context context;
	FileCache fileCache;

	private Map<View, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<View, String>());

	ExecutorService executorService;

	Handler handler = new Handler();

	public ImageLoader(Context context) {
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		this.context = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(7);
	}

	final int stub_int = R.drawable.defaultpicture;

	@SuppressWarnings("deprecation")
	public void displayImage(String url, View imageView, int type, int h, int w) {

		imageViews.put(imageView, url);
		Bitmap bitmap = null;
		if (w != 1)
			bitmap = memoryCache.get(url);

		if (bitmap != null) {
			if (type == 0)
				((ImageView) imageView).setImageBitmap(bitmap);
			else
				((ImageSwitcher) imageView)
						.setImageDrawable(new BitmapDrawable(bitmap));

		} else {
			queuePhoto(url, imageView, type, h, w);
			if (type == 0)
				((ImageView) imageView).setImageResource(stub_int);
		}
	}

	private void queuePhoto(String url, View imageView, int t, int h, int w) {

		PhotoToLoad p = new PhotoToLoad(url, imageView, t, h, w);
		executorService.submit(new PhotosLoader(p));
	}

	private class PhotoToLoad {
		public String url;
		public View imageView;
		public int type;
		public int height;
		public int width;

		public PhotoToLoad(String u, View i, int t, int h, int w) {
			url = u;
			imageView = i;
			type = t;
			height = h;
			width = w;
		}
	}

	class PhotosLoader implements Runnable {

		PhotoToLoad photoToLoad;

		public PhotosLoader(PhotoToLoad ptl) {
			this.photoToLoad = ptl;
		}

		@Override
		public void run() {
			try {

				if (photoToLoad.imageView != null)
					if (imageViewReused(photoToLoad))
						return;

				Bitmap bmp = getBitmap(photoToLoad);

				memoryCache.put(photoToLoad.url, bmp);

				if (photoToLoad.imageView == null)
					return;
				if (imageViewReused(photoToLoad))
					return;

				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);

				handler.post(bd);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private Bitmap getBitmap(PhotoToLoad p) {

		File f = fileCache.getFile(p.url);

		Bitmap b = decodeFile(f, p);

		if (b != null)
			return b;

		try {

			Bitmap bitmap = null;
			URL imageUrl = new URL(p.url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();

			OutputStream os = new FileOutputStream(f);

			Utils.copyStream(is, os);

			os.close();
			conn.disconnect();
			bitmap = decodeFile(f, p);

			return bitmap;

		} catch (Throwable e) {
			e.printStackTrace();

			if (e instanceof OutOfMemoryError)
				memoryCache.clear();

			return null;
		}
	}

	public void prefloadImage(String url, int type, int h, int w) {
		PhotoToLoad p = new PhotoToLoad(url, null, type, h, w);
		executorService.submit(new preloadPictures(p));
	}

	private class preloadPictures implements Runnable {
		PhotoToLoad photo;

		public preloadPictures(PhotoToLoad p) {
			photo = p;
		}

		@Override
		public void run() {
			Bitmap bmp = getBitmap(photo);
			memoryCache.put(photo.url, bmp);
		}
	}

	private Bitmap decodeFile(File f, PhotoToLoad p) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			final int REQUIRED_SIZE = width;

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 2;
			p.height = p.imageView.getHeight();
			p.width = p.imageView.getWidth();
			if (p.height != 0) {
				if (height_tmp > p.height || width_tmp > p.width) {
					System.out.println("condition check size");
					while ((width_tmp / scale) > p.height
							&& (width_tmp / scale) >= p.width) {
						scale *= 2;
					}
				}
			} else {
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE) {
						break;
					}
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * private Bitmap decodeFile(File f) {
	 * 
	 * try {
	 * 
	 * BitmapFactory.Options o = new BitmapFactory.Options();
	 * o.inJustDecodeBounds = true; FileInputStream stream1 = new
	 * FileInputStream(f); BitmapFactory.decodeStream(stream1, null, o);
	 * stream1.close();
	 * 
	 * final int REQUIRED_SIZE = 85;
	 * 
	 * int width_tmp = o.outWidth, height_tmp = o.outHeight; int scale = 1;
	 * 
	 * 
	 * while (true) { if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 <
	 * REQUIRED_SIZE) { break; }
	 * 
	 * width_tmp /= 2; height_tmp /= 2; scale *= 2; }
	 * 
	 * BitmapFactory.Options o2 = new BitmapFactory.Options(); o2.inSampleSize =
	 * scale; FileInputStream stream2 = new FileInputStream(f); Bitmap bitmap =
	 * BitmapFactory.decodeStream(stream2, null, o2); stream2.close(); return
	 * bitmap; } catch(FileNotFoundException e) { e.printStackTrace(); }
	 * catch(IOException e) { e.printStackTrace(); }
	 * 
	 * return null; }
	 */

	boolean imageViewReused(PhotoToLoad ptl) {

		String tag = imageViews.get(ptl.imageView);

		if (tag == null || !tag.equals(ptl.url))
			return true;
		return false;
	}

	class BitmapDisplayer implements Runnable {

		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;

			if (bitmap != null) {
				if (photoToLoad.type == 0)
					((ImageView) photoToLoad.imageView).setImageBitmap(bitmap);
				else
					((ImageSwitcher) photoToLoad.imageView)
							.setImageDrawable(new BitmapDrawable(bitmap));

			} else {
				// photoToLoad.imageView.setImageResource(stub_int);
				if (photoToLoad.type == 0)
					((ImageView) photoToLoad.imageView)
							.setImageResource(stub_int);
				else
					((ImageSwitcher) photoToLoad.imageView)
							.setImageResource(stub_int);
			}
		}
	}

	/*
	 * class BitmapDisplayerSwitcher implements Runnable {
	 * 
	 * Bitmap bitmap; PhotoToLoadWI photoToLoadWi; public
	 * BitmapDisplayerSwitcher(Bitmap b, PhotoToLoadWI p) { bitmap = b;
	 * photoToLoadWi = p; }
	 * 
	 * @Override public void run() {
	 * 
	 * if(bitmap != null) { photoToLoadWi.imageSwitcher.setImageDrawable(new
	 * BitmapDrawable(bitmap)); } else {
	 * photoToLoadWi.imageSwitcher.setImageResource(stub_int); } } }
	 */

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}
}