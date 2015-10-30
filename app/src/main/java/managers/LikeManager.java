package managers;

import interfaces.OnTaskCompleteListener;
import sdk.SDKLike;
import AsyncUserRequest.PerformLikeLaunchAsync;

public class LikeManager {
	public static void ApiCreate(OnTaskCompleteListener callback, SDKLike like){
		PerformLikeLaunchAsync.create(callback, like);
	}
}
