package managers;

import AsyncUserRequest.PerformUserLaunchAsync;
import interfaces.OnTaskCompleteListener;
import sdk.SDKUser;

public class UserManager {
	public static void ApiCreate(OnTaskCompleteListener callback, SDKUser user){
		PerformUserLaunchAsync.Create(callback, user);
	}
	
	public static void ApiAuthenticate(OnTaskCompleteListener callback, SDKUser user){
		PerformUserLaunchAsync.Authenticate(callback, user);
	}
	
	public static void ApiUpdate(OnTaskCompleteListener callback, SDKUser user){
		PerformUserLaunchAsync.Update(callback, user);
	}
	
	public static void ApiReadById(OnTaskCompleteListener callback, SDKUser user){
		PerformUserLaunchAsync.ReadById(callback, user);
	}
}
