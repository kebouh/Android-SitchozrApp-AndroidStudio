package datas;

import android.location.Location;

import com.facebook.AccessToken;

import sdk.SDKUser;
import Abstract.AbstractUsersData;

public class DiscoveryProfile extends AbstractUsersData {

	public DiscoveryProfile(SDKUser sdkuser, AccessToken accessToken) {
		super(sdkuser, accessToken);
		LocationWraper location = new LocationWraper(Manager.getAppContext(), sdkuser.getLatitude(), sdkuser.getLongitude());
		Location l = Manager.getProfile().getLocation().getLocation();
		if (l == null)
			Manager.getProfile().getLocation().initConnection();
		location.distanceTo(l);
		this.setLocation(location);
	}
}
