package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKDislike;
import enumerator.REQUESTENUM;

public class PerformDislikeLaunchAsync {
	static PerformDislikeLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformUserLaunchAsync
	 *
	 * @return PerformUserLaunchAsync
	 */
	public	static PerformDislikeLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformDislikeLaunchAsync();
		return _instance;
	}
	
	public static void	create(OnTaskCompleteListener post, SDKDislike dislike)
	{
		Object[] objTab = new Object[] { REQUESTENUM.CREATE_DISLIKE, dislike};
		new AsyncRequestCall(post).execute(objTab);
	}
}
