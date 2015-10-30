package managers;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

public class FacebookManager {
	
	public static void getUserProfilePhoto(GraphRequest.Callback callback, AccessToken accessToken, String facebookId){
		GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/"+ facebookId + "/photo?fields={id, height, icon, images, picture}", callback);
    	request.executeAsync();
	}
	
	public static void getUserPhotos(GraphRequest.Callback callback, AccessToken accessToken, String facebookId){
		GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/"+ facebookId + "/photos", callback);
		Bundle parameters = new Bundle();
		parameters.putString("fields", "id, height, icon, images, pictures");
		request.setParameters(parameters);
    	request.executeAsync();
	}
	
	public static void getAlbums(GraphRequest.Callback callback, AccessToken accessToken, String facebookId){
		GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/"+ facebookId + "/albums", callback);
    	request.executeAsync();
	}
	
	public static void getPhotosByAlbum(GraphRequest.Callback callback, AccessToken accessToken, Long albumId){
		GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/" + albumId + "/photos", callback);
		Bundle parameters = new Bundle();
		parameters.putString("fields", "id, height, icon, images, pictures");
		request.setParameters(parameters);
    	request.executeAsync();
	}
}
