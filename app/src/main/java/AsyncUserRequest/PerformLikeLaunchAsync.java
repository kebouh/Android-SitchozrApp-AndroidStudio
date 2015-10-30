package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKLike;
import enumerator.REQUESTENUM;

public class PerformLikeLaunchAsync {
static PerformLikeLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformUserLaunchAsync
	 *
	 * @return PerformUserLaunchAsync
	 */
	public	static PerformLikeLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformLikeLaunchAsync();
		return _instance;
	}
	
	public static void	create(OnTaskCompleteListener post, SDKLike like)
	{
		Object[] objTab = new Object[] { REQUESTENUM.CREATE_LIKE, like};
		new AsyncRequestCall(post).execute(objTab);
	}
}
