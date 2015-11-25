package activities;

import android.view.View;

import animation.DropDownAnimation;
import datas.Manager;
import datas.MatchProfile;
import interfaces.OnTaskCompleteListener;
import managers.DislikeManager;
import managers.LikeManager;
import sdk.SDKDislike;
import sdk.SDKLike;
import sources.sitchozt.R;
import Abstract.AbstractUsersData;

public class DiscoveryProfileActivity extends ProfileActivity {


	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_discovery_profile;
	}

	@Override
	protected void initializeDropdown() {
		DropDownAnimation dda = new DropDownAnimation(this, R.id.discoveryRoot, R.layout.discovery_dropdown_content, 500);
		dda.initializeDropDown(null);
	}

	@Override
	protected AbstractUsersData getUser(int id) {
		return Manager.getDiscoveryProfileById(id);
	}

	public 	void	onAddMatch(View view) {
		SDKLike like = new SDKLike();
		like.setUserId(this.id);
		OnTaskCompleteListener callback = new OnTaskCompleteListener() {
			@Override
			public void onCompleteListerner(Object[] result) {
				MainActivity.addMatches();
			}
		};
		MatchProfile match = new MatchProfile();
		match.setId(this.id);
		match.setFirstName(match.getFirstName());
		match.setAge(match.getAge());
		match.setLocation(match.getLocation());
		LikeManager.ApiCreate(callback, like);
		Manager.giveLike(this.id);
		Manager.getDatabase().createMatch(match);
		Manager.deleteDiscovery(this.id);
		this.finish();
	}

	public void onDeleteMatch(View view) {
		SDKDislike dislike = new SDKDislike();
		dislike.setUserId(id);
		DislikeManager.ApiCreate(null, dislike);
		Manager.deleteDiscovery(id);
		this.finish();
	}

}
