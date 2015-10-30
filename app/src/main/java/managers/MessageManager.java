package managers;

import AsyncUserRequest.PerformMessageLaunchAsync;
import sdk.SDKMessage;
import interfaces.OnTaskCompleteListener;

public class MessageManager {
	public static void ApiCreate(OnTaskCompleteListener callback, SDKMessage message){
		PerformMessageLaunchAsync.create(callback, message);
	}
	
	public static void ApiReadByUser(OnTaskCompleteListener callback, int userId){
		PerformMessageLaunchAsync.readByUserId(callback, userId);
	}
}
