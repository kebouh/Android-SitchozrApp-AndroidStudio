package managers;

import interfaces.OnTaskCompleteListener;
import sdk.SDKPicture;
import sdk.SDKUser;
import AsyncUserRequest.PerformPictureLaunchAsync;
import datas.FacebookPhoto;

public class ImageManager {
	public static void ApiCreate(OnTaskCompleteListener callback, FacebookPhoto newImage, int index, boolean isProfile){
		long	id = newImage.getId();
		String 	url = newImage.getImages().get(0).getSource();
		PerformPictureLaunchAsync.create(callback, new SDKPicture(id, url, index, isProfile));
	}
	
	public static void ApiCreate(OnTaskCompleteListener callback, SDKPicture picture){
		PerformPictureLaunchAsync.create(callback, picture);
	}
	
	public static void ApiReadProfilePicture(OnTaskCompleteListener callback, SDKUser user){
		PerformPictureLaunchAsync.getProfilePicture(callback, user);
	}
	
	public static void ApiReadByUserId(OnTaskCompleteListener callback, SDKUser match){
		PerformPictureLaunchAsync.getByUserId(callback, new SDKPicture(), match);
	}
	
	public static void ApiReadAll(OnTaskCompleteListener callback){
		PerformPictureLaunchAsync.getAll(callback, new SDKPicture());
	}
	
	public static void ApiDelete(OnTaskCompleteListener callback, SDKPicture picture){
		PerformPictureLaunchAsync.delete(callback, picture);
	}
	
	public static void ApiUpdate(OnTaskCompleteListener callback, SDKPicture picture){
		PerformPictureLaunchAsync.update(callback, picture);
	}
}
