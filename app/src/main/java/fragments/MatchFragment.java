package fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import memory.ImageLoader;
import sources.sitchozt.R;
import datas.Manager;
import datas.MatchProfile;
import activities.MatchProfileActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchFragment extends Fragment {

	private View rootView = null;
	protected Context context = null;
	private GridView gridView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
	//	if (Manager.getMatchProfiles() == null)
		Manager.getDatabase().getMatchsAndPictures();
		context = this.getActivity();
		Manager.setContext(context);
		rootView = inflater.inflate(R.layout.match_fragment, container, false);
		gridView = (GridView) rootView.findViewById(R.id.gridviewmatch);
		gridView.setAdapter(new MyAdapter(this.getActivity()));
		return rootView;
	}

	@Override
	public void onStop() {
		super.onStop();
		Manager.getImageLoader().clearCache();
	}

	private class MyAdapter extends BaseAdapter {
		private ImageLoader imageLoader = new ImageLoader(context);
		private List<Item> items = new ArrayList<Item>();
		private LayoutInflater inflater;
		private int heightImageView;
		private int widthImageView;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			for (Entry<Integer, MatchProfile> entry : Manager.getMatchProfiles().entrySet()) {
				items.add(new Item(entry.getValue().getFirstName(), entry.getValue().getId(), 
						entry.getValue().getImgs().get(0).getUrl(), entry.getValue().getLocation().getDistance(), 
						entry.getValue().getAge()));
			}
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
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			View v = view;
			ImageView picture;
			TextView name;

			final Item item = (Item) getItem(i);
			if (v == null) {
				v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
				v.setTag(R.id.picture, v.findViewById(R.id.picture));	
			}
			picture = (ImageView) v.getTag(R.id.picture);
			picture.setMinimumHeight(gridView.getWidth());
			picture.setMinimumWidth(gridView.getWidth());
			
			picture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, MatchProfileActivity.class);
					intent.putExtra("ID", item.id);
					startActivity(intent);
				}
			});
			
			name = (TextView) v.findViewById(R.id.texti);
			picture.setTag(name);
			name.setText(item.name + " - " + item.distance + " km");
			imageLoader.displayImage(item.url, picture, 0, heightImageView, widthImageView);
			return v;
		}

		private class Item {
			final String name;
			final String distance;
			final String age;
			final int id;
			final String url;

			Item(String name, int id, String url, String distance, String age) {
				this.url = url;
				this.name = name;
				this.id = id;
				this.distance = distance;
				this.age = age;
			}
		}

	}
}
