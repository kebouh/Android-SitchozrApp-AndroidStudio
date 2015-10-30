package datas;

import com.facebook.AccessToken;
import sdk.SDKUser;
import Abstract.AbstractUsersData;

/**
 * Encapsulation of the user Profile. The class contains most of the information
 * of the user
 */

public class Profile extends AbstractUsersData {
	private ListAlbums listAlbums;

	public Profile(SDKUser sdkuser, AccessToken accessToken, LocationWraper location) {
		super(sdkuser, accessToken);
		this.setLocation(location);
		listAlbums = new ListAlbums();
	}
	
	public void addAlbumToList(Album a)
	{
		listAlbums.addAlbumToList(a);
	}
	
	public ListAlbums	getListAlbums()
	{
		return listAlbums;
	}
}
