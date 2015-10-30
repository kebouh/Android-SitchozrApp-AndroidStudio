package activities;

import java.util.ArrayList;
import memory.ImageLoader;
import sources.sitchozt.R;
import datas.Images;
import datas.Manager;
import Abstract.AbstractUsersData;
import adapters.GridViewAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

/**
 * The activity of the pictures gallery This activity allows the user to change
 * is profile picture and change the display order of the pictures by a drag and
 * drop
 */
public class PictureGallery extends Activity {
	private ImageLoader	imageLoader = null;
	private AbstractUsersData user = null;
	private ArrayList<Images> imgs = null;
	public GridView gridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_gallery);
		Manager.setContext(this);

		imageLoader = Manager.getImageLoader();
		Bundle extras = getIntent().getExtras();
		int id;
		if (extras != null) {
			id = extras.getInt("ID");
			user = Manager.getUserById(id);
			if (user != null)
				imgs = user.getImgs();
		}
		if (imgs != null) {
			 gridView = (GridView) findViewById(R.id.gridview);
			 gridView.setColumnWidth(2);
			 GridViewAdapter pa = new GridViewAdapter(this, imgs, gridView, 2, R.layout.gridview_item);
			 
			gridView.setAdapter(pa);
			//pa.setWidth(gridView.getWidth());
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		imageLoader.clearCache();
	}
/*
	private class MyAdapter extends BaseAdapter {
		private List<Item> items = new ArrayList<Item>();
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);

			for (int i = 0; i != imgs.size(); i++)
				items.add(new Item(imgs.get(i)
						.getUrl()));
		
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int i) {
			return items.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i/* items.get(i). ;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			View v = view;
			final ImageView picture;
			final Item item = (Item) getItem(i);
			if (v == null) {
				v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
				v.setTag(R.id.picture, v.findViewById(R.id.picture));
			}

			picture = (ImageView) v.getTag(R.id.picture);
			v.setLayoutParams(new GridView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, gridView.getWidth()/2));
			picture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadPhoto(item.url);
				}
			});
			imageLoader.displayImage(item.url, picture, 0, 0, 0);
			return v;
		}

		private class Item {
			final String url;

			Item(String u) {
				this.url = u;
			}
		}
	}
	
	private void loadPhoto(String url) {


		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.fullsize_picture,
				(ViewGroup) findViewById(R.id.layout_root));

		
		ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		image.getLayoutParams().width = gridView.getWidth();
		image.getLayoutParams().height = gridView.getHeight();
		image.requestLayout();
		//image.setLayoutParams(new AlertDialog.Builder.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, gridView.getWidth()/2));

		imageLoader.displayImage(url, image, 0, 0, 1);
		//image.setImageDrawable(tempImageView.getDrawable());
		imageDialog.setView(layout);
		imageDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});

		imageDialog.create();
		imageDialog.show();
	}
	*/
}
