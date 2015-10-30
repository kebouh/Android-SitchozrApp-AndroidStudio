package activities;

import datas.Manager;
import sources.sitchozt.R;
import Abstract.AbstractUsersData;

public class DiscoveryProfileActivity extends ProfileActivity {


	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_discovery_profile;
	}
	
	@Override
	protected AbstractUsersData getUser(int id) {
		return Manager.getDiscoveryProfileById(id);
	}
}
