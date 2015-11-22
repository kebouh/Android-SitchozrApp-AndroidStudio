package fragments;

import datas.MatchProfile;
import interfaces.OnTaskCompleteListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import managers.DislikeManager;
import managers.LikeManager;
import memory.ImageLoader;
import sdk.SDKDislike;
import sdk.SDKLike;
import sources.sitchozt.R;
import datas.Manager;
import Abstract.AbstractUsersData;
import activities.DiscoveryProfileActivity;
import activities.MainActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoveryFragment extends Fragment {

	private View rootView = null;
	protected Context context = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = this.getActivity();
		Manager.setContext(context);
		rootView = inflater.inflate(R.layout.discovery_fragment, container, false);
		ListView gridView = (ListView) rootView.findViewById(R.id.gridviewdiscovery);
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
			for (Entry<Integer, AbstractUsersData> entry : Manager.getDiscoveryProfiles().entrySet()) {
				if (entry.getValue().getProfileImage() != null)
					items.add(new Item(entry.getValue().getFirstName(), entry.getValue().getId(), entry.getValue().getProfileImage().getUrl(), entry.getValue().getLocation().getDistance(), entry.getValue().getAge(), entry.getValue().getLocation().getCity()));
				else
					items.add(new Item(entry.getValue().getFirstName(), entry.getValue().getId(), null, entry.getValue().getLocation().getDistance(), entry.getValue().getAge(), entry.getValue().getLocation().getCity()));
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

		private void addListeners(View v, ImageView picture, ImageView delete,ImageView like, final Item item)
		{
			like.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SDKLike like = new SDKLike();
					like.setUserId(item.id);
					OnTaskCompleteListener callback = new OnTaskCompleteListener() {
						@Override
						public void onCompleteListerner(Object[] result) {
							MainActivity.addMatches();
						}
					};

					MatchProfile match = new MatchProfile();
					match.setId(item.id);
					match.setFirstName(item.name);
					match.setAge(item.age);
					match.setLocation(Manager.getDiscoveryProfileById(item.id).getLocation());
					LikeManager.ApiCreate(callback, like);
					Manager.giveLike(item.id);
					Manager.getDatabase().createMatch(match);
					Manager.deleteDiscovery(item.id);
					items.remove(item);
	                notifyDataSetChanged();
				}
			});
			
			picture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, DiscoveryProfileActivity.class);
					intent.putExtra("ID", item.id);
					startActivity(intent);
				}
			});
			
			delete.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					SDKDislike dislike = new SDKDislike();
					dislike.setUserId(item.id);
					DislikeManager.ApiCreate(null, dislike);
					System.out.println("Remove item !");
					Manager.deleteDiscovery(item.id);
					items.remove(item);
	                notifyDataSetChanged();
				}
			});
		
		}
		
		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			View v = view;
			final Item item = (Item) getItem(i);
			if (v == null) {
				v = inflater.inflate(R.layout.gridview_item_discovery, viewGroup, false);
				v.setTag(R.id.pictureDiscovery, v.findViewById(R.id.pictureDiscovery));
			}
			item.picture = (ImageView) v.getTag(R.id.pictureDiscovery);
			item.nameView = (TextView) v.findViewById(R.id.nameDiscovery);
			item.distanceView = (TextView)v.findViewById(R.id.distanceDiscovery);
			item.ageView = (TextView)v.findViewById(R.id.ageDiscovery);

			addListeners(v, item.picture, (ImageView)v.findViewById(R.id.discoveryDelete), (ImageView)v.findViewById(R.id.discoveryLike), item);

			item.nameView.setText(item.name);
			item.ageView.setText(item.age);
			item.distanceView.setText(new DecimalFormat("##.#").format(Double.parseDouble(item.distance)) + " km");
			imageLoader.displayImage(item.url, item.picture, 0, heightImageView,
					widthImageView);
			return v;
		}

		private class Item {
			final String name;
			final int id;
			final String url;
			final String distance;
			final String age;
			final String city;
			
			ImageView picture;
			TextView nameView = null;
			TextView distanceView;
			TextView ageView;

			Item(String name, int id, String url, String distance, String age, String city) {
				this.url = url;
				this.name = name;
				this.id = id;
				this.distance = distance;
				this.age = age;
				this.city = city;
			}
		}
	}
}
