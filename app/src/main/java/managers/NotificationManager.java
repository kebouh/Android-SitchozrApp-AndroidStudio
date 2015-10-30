package managers;

import interfaces.OnTaskCompleteListener;
import sdk.SDKNotification;
import AsyncUserRequest.PerformNotificationLaunchAsync;

public class NotificationManager {
	public static void	ApiDelete(OnTaskCompleteListener callback, SDKNotification notif){
		PerformNotificationLaunchAsync.delete(callback, notif);
	}
	
	public static void	ApiRead(OnTaskCompleteListener callback, SDKNotification notif){
		PerformNotificationLaunchAsync.read(callback, notif);
	}
}
