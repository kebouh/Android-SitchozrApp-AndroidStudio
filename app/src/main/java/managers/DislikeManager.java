package managers;

import interfaces.OnTaskCompleteListener;
import sdk.SDKDislike;
import AsyncUserRequest.PerformDislikeLaunchAsync;

public class DislikeManager {
	public static void ApiCreate(OnTaskCompleteListener callback, SDKDislike dislike){
		PerformDislikeLaunchAsync.create(callback, dislike);
	}
}
