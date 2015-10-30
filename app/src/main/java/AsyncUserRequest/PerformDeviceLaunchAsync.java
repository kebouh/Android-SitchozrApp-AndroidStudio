package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKDevice;
import enumerator.REQUESTENUM;

public class PerformDeviceLaunchAsync {
	static PerformDeviceLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformUserLaunchAsync
	 *
	 * @return PerformUserLaunchAsync
	 */
	public	static PerformDeviceLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformDeviceLaunchAsync();
		return _instance;
	}
	
	public static void	create(OnTaskCompleteListener post, SDKDevice device)
	{
		Object[] objTab = new Object[] { REQUESTENUM.CREATE_DEVICE, device};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	delete(OnTaskCompleteListener post, SDKDevice device)
	{
		Object[] objTab = new Object[] { REQUESTENUM.DELETE_DEVICE, device};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	read(OnTaskCompleteListener post, SDKDevice device)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_DEVICE, device};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	update(OnTaskCompleteListener post, SDKDevice device)
	{
		Object[] objTab = new Object[] { REQUESTENUM.UPDATE_DEVICE, device};
		new AsyncRequestCall(post).execute(objTab);
	}
}
