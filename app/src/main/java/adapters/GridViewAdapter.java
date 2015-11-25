package adapters;

import interfaces.OnTaskCompleteListener;

import java.util.ArrayList;
import java.util.List;

import managers.ImageManager;
import memory.ImageLoader;
import datas.Images;
import datas.Manager;
import datas.Profile;
import sdk.SDKPicture;
import sources.sitchozt.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GridViewAdapter extends BaseAdapter {
	private List<Item> items = new ArrayList<Item>();
	private LayoutInflater inflater;
	private ArrayList<Images>	imagesList;
	private ImageLoader imageLoader = Manager.getImageLoader();
	GridView gridView;
	private int numColumns;
	private int layout;
	private Context context;
	
	public GridViewAdapter(Context context, ArrayList<Images> imagesList, GridView gridView, int numColums, int layout) {
		this.context = context;
		Manager.setContext(context);
		inflater = LayoutInflater.from(context);
		this.layout = layout;
		this.imagesList = imagesList;
		this.gridView = gridView;
		this.numColumns = numColums;
		for (int i = 0; i != imagesList.size(); i++)
			items.add(new Item(imagesList.get(i).getUrl()));
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
		return i/* items.get(i). */;
	}

	
	public SDKPicture	imagesToSdkPicture(Images img, int index)
	{
		boolean isProfile = false;
		if (index == 0)
			isProfile = true;
		
		SDKPicture pict = new SDKPicture(img.getFacebookId(), img.getUrl(), index, isProfile);
		return pict;
	}
	
	public Images	sdkPictureToImages(SDKPicture img)
	{
		Images pict = new Images(img.getUrl(), img.getId());
		pict.setFacebookId(Long.parseLong(img.getFacebookId()));
		return pict;
	}
	
	
	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		View v = view;
		
		final Item item = (Item) getItem(i);
		if (v == null) {
			v = inflater.inflate(layout, viewGroup, false);
			v.setTag(R.id.picture, v.findViewById(R.id.picture));
		}

		item.picture = (ImageView) v.getTag(R.id.picture);
		v.setLayoutParams(new GridView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, gridView.getWidth() / numColumns));
	
		if (layout == R.layout.gridview_item_album)
		{
			ImageView add = (ImageView)v.findViewById(R.id.add_picture);
			add.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					final Profile profile = Manager.getProfile();
					if (profile.getImgs().size() == 8) {
						Toast.makeText(context, "Can not add picture, defined pictures is full", 
								   Toast.LENGTH_SHORT).show();
						return;
					}
					SDKPicture picture = imagesToSdkPicture(imagesList.get(i), profile.getImgs().size());
					OnTaskCompleteListener	onPictureUpdated = new OnTaskCompleteListener(){

						@Override
						public void onCompleteListerner(Object[] result) {
							SDKPicture respPicture = (SDKPicture)result[1];
							if (respPicture != null) {
								Manager.getDatabase().createPictureProfile((SDKPicture)result[1], 0, profile.getId());
								profile.addImagesToArray(sdkPictureToImages(respPicture));
							}
						}
					};
					ImageManager.ApiCreate(onPictureUpdated, picture);
				}
			});
		}
		item.picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadPhoto(item.url, v);
			}
		});
		imageLoader.displayImage(item.url, item.picture, 0, 0, 0);
		return v;
	}

	private class Item {
		final String url;
		ImageView picture;
		Item(String u) {
			this.url = u;
		}
	}
	
	private void loadPhoto(String url, View v) {
		AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.fullsize_picture,(ViewGroup)v.findViewById(R.id.layout_root));
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
}