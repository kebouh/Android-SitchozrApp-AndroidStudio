package managers;

import sdk.SDKMatch;
import AsyncUserRequest.PerformMatchLaunchAsync;
import interfaces.OnTaskCompleteListener;

public class MatchManager {
	public static void ApiReadAll(OnTaskCompleteListener callback){
		PerformMatchLaunchAsync.readAll(callback, new SDKMatch());
	}
}
