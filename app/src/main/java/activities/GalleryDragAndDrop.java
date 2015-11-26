package activities;

import java.util.ArrayList;
import java.util.List;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;
import org.askerov.dynamicgrid.DynamicGridView;

import managers.ImageManager;
import memory.ImageLoader;
import sdk.SDKPicture;
import sources.sitchozt.R;
import database.DBDatas;
import datas.Images;
import datas.Manager;
import Abstract.AbstractUsersData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * The activity of the pictures gallery This activity allows the user to change
 * is profile picture and change the display order of the pictures by a drag and
 * drop
 */

@SuppressLint({ "ViewHolder", "InflateParams" })
public class GalleryDragAndDrop extends Activity {

	private static final String TAG = GalleryDragAndDrop.class.getName();
	private DBDatas db = null;
	private ImageLoader imageLoader = null;
	private AbstractUsersData user = null;
	private ArrayList<Images> imgs = null;
	public DynamicGridView gridView;
	private boolean asChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_drag_and_drop);
		Manager.setContext(this);
		this.setTitle("Gallery");
		imageLoader = Manager.getImageLoader();
		db = Manager.getDatabase();
		Bundle extras = getIntent().getExtras();
		int id;
		if (extras != null) {
			id = extras.getInt("ID");
			user = Manager.getUserById(id);
			if (user != null)
				imgs = user.getImgs();
		}
		if (imgs != null) {
			gridView = (DynamicGridView) findViewById(R.id.gridview);
			gridView.setWobbleInEditMode(false);
			gridView.setAdapter(new MyAdapter(this, imgs, 2));
			gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
				@Override
				public void onDragStarted(int position) {
					Log.d(TAG, "drag started at position " + position);
				}

				@Override
				public void onDragPositionsChanged(int oldPosition,
						int newPosition) {
					Log.d(TAG, String.format(
							"drag item position changed from %d to %d",
							oldPosition, newPosition));
					//SDKPicture picture = new SDKPicture(imgs.get(oldPosition).getId());
					//picture.setIndex(newPosition);
				}
			});

			gridView.setOnDropListener(new DynamicGridView.OnDropListener() {

				@Override
				public void onActionDrop() {
					// stop edit mode immediately after drop item
					gridView.stopEditMode();
					//imgs.clear();
					//saveItemsPosition();
					asChanged = true;
				}
			});

			gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					gridView.startEditMode(position);
					return true;
				}
			});

			gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					loadPhoto(imgs.get(position).getUrl());
				}
			});
		}
	}

	public void saveItemsPosition() {
		imgs.clear();

		for (int i = 0; i != gridView.getAdapter().getCount(); i++) {
			imgs.add((Images) gridView.getAdapter().getItem(i));
			Images img = imgs.get(i);
			SDKPicture picture;
			if (i == 0)
				picture = new SDKPicture(img.getId(), img.getFacebookId(), img.getUrl(), 0, true);
			else
				picture = new SDKPicture(img.getId(), img.getFacebookId(), img.getUrl(), i, false);
			ImageManager.ApiUpdate(null, picture);
		}



	}

	@Override
	public void onBackPressed() {
		if (imgs != null)
			imgs.clear();
		for (int i = 0; i < gridView.getAdapter().getCount(); i++) {
			imgs.add((Images) gridView.getAdapter().getItem(i));
			Images img = imgs.get(i);
			SDKPicture picture;
			if (i == 0)
				picture = new SDKPicture(img.getId(), img.getFacebookId(), img.getUrl(), 0, true);
			else
				picture = new SDKPicture(img.getId(), img.getFacebookId(), img.getUrl(), i, false);
			System.out.println("PICTURE INDEX : " + i);
			ImageManager.ApiUpdate(null, picture);
		}
		if (imgs.size() != 0) {
			Manager.getProfile().setProfileImage(imgs.get(0));
			db.changeIndexPictures(imgs);
		} else
			Manager.getProfile().setProfileImage(null);

		if (gridView.isEditMode()) {
			gridView.stopEditMode();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStop() {
		imageLoader.clearCache();
		super.onStop();
	}

	private class MyAdapter extends BaseDynamicGridAdapter {
		public MyAdapter(Context context, List<Images> items, int columnCount) {
			super(context, items, columnCount);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			CheeseViewHolder holder;
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.gridview_item_drag, null);
			holder = new CheeseViewHolder(convertView);
			convertView.setTag(holder);
			holder.position = position;
			convertView.setLayoutParams(new DynamicGridView.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, gridView
							.getWidth() / 2));
			holder.build(getItem(position));
			holder.position = position;
			return convertView;
		}

		private class CheeseViewHolder {
			public ImageView image;
			public ImageView delete;
			public int position;

			private CheeseViewHolder(View view) {
				image = (ImageView) view.findViewById(R.id.picture);
				delete = (ImageView) view.findViewById(R.id.delete_picture);
				delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (Manager.getProfile().getImgs().size() > 1)
						deletePicture(v);
						else
							Toast.makeText(getApplicationContext(), "You can not delete this picture, you must at least have one picture", Toast.LENGTH_SHORT).show();

					}
				});
			}

			void build(Object img) {
				imageLoader.displayImage(((Images) img).getUrl(), image, 0, 0,
						0);
			}

			public void deletePicture(View v) {
				getItems().remove(position);
				db.deletePicture(imgs.get(position).getId());
				SDKPicture picture = new SDKPicture(imgs.get(position).getId());
				ImageManager.ApiDelete(null, picture);
				saveItemsPosition();
				notifyDataSetChanged();
				/*} else
					Toast.makeText(getApplicationContext(),"You can not delete this picture, you must at least have one picture",Toast.LENGTH_SHORT).show();*/
			}
		}
	}

	private void loadPhoto(String url) {
		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.fullsize_picture,(ViewGroup) findViewById(R.id.layout_root));
		ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		image.getLayoutParams().width = gridView.getWidth();
		image.getLayoutParams().height = gridView.getHeight();
		image.requestLayout();
		imageLoader.displayImage(url, image, 0, 0, 1);
		imageDialog.setView(layout);
		imageDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		imageDialog.create();
		imageDialog.show();
	}
}
