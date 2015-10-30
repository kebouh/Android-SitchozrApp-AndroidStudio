package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKNotification;
import enumerator.REQUESTENUM;

public class PerformNotificationLaunchAsync {
	static PerformNotificationLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformNotificationLaunchAsync
	 *
	 * @return PerformNotificationLaunchAsync
	 */
	public	static PerformNotificationLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformNotificationLaunchAsync();
		return _instance;
	}
	
	public static void	delete(OnTaskCompleteListener post, SDKNotification notif)
	{
		Object[] objTab = new Object[] { REQUESTENUM.DELETE_NOTIFICATION, notif};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	read(OnTaskCompleteListener post, SDKNotification notif)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_NOTIFICATION, notif};
		new AsyncRequestCall(post).execute(objTab);
	}
}
