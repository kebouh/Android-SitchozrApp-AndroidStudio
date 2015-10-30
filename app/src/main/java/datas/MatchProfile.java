package datas;

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
