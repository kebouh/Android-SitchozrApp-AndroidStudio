package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKMatch;
import enumerator.REQUESTENUM;

public class PerformMatchLaunchAsync {
static PerformMatchLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformUserLaunchAsync
	 *
	 * @return PerformUserLaunchAsync
	 */
	public	static PerformMatchLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformMatchLaunchAsync();
		return _instance;
	}
	
	public static void	readAll(OnTaskCompleteListener post, SDKMatch match)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_ALL_MATCH, match};
		new AsyncRequestCall(post).execute(objTab);
	}
}
