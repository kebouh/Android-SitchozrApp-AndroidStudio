package datas;

import android.location.Location;

import com.facebook.AccessToken;

import controllers.ChatController;
import sdk.SDKUser;
import Abstract.AbstractUsersData;

/**
 * Encapsulation of the user Profile. The class contains most of the information
 * of the user
 */

public class MatchProfile extends AbstractUsersData {
	
	ChatController	chatController;
	
	public MatchProfile(SDKUser sdkuser, AccessToken accessToken) {
		super(sdkuser, accessToken);
		LocationWraper location = new LocationWraper(Manager.getAppContext(), sdkuser.getLatitude(), sdkuser.getLongitude());
		Location l = Manager.getProfile().getLocation().getLocation();
		if (l == null)
			Manager.getProfile().getLocation().initConnection();
		location.distanceTo(l);
		this.setLocation(location);
		chatController = new ChatController();
	}
	
	public MatchProfile() 
	{
		super();
		chatController = new ChatController();
	}

	public	ChatController getChatController()
	{
		return chatController;
	}
}
