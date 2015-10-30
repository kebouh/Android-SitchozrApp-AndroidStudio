package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKMessage;
import enumerator.REQUESTENUM;

public class PerformMessageLaunchAsync {
	static PerformPictureLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformUserLaunchAsync
	 *
	 * @return PerformUserLaunchAsync
	 */
	public	static PerformPictureLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformPictureLaunchAsync();
		return _instance;
	}
	
	public static void	create(OnTaskCompleteListener post, SDKMessage message)
	{
		Object[] objTab = new Object[] { REQUESTENUM.CREATE_MESSAGE, message};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	readByUserId(OnTaskCompleteListener post, int userId)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_BY_USER_ID_MESSAGE, new SDKMessage(), userId};
		new AsyncRequestCall(post).execute(objTab);
	}
}
