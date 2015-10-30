package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;

import requestApiManager.AsyncRequestCall;
import sdk.SDKUser;
import enumerator.REQUESTENUM;

/**
 *  
 * Launch a thread to perform background request on User
 *
 * @param  Specify output
 */
public class PerformUserLaunchAsync {

	static PerformUserLaunchAsync	_instance = null;
	
	/**
	 * return a new instance of PerformUserLaunchAsync
	 *
	 * @return PerformUserLaunchAsync
	 */
	public	static PerformUserLaunchAsync	getInstance()
	{
		if (_instance == null)
			_instance = new PerformUserLaunchAsync();
		return _instance;
	}
	
	public static void	Authenticate(OnTaskCompleteListener post, SDKUser user)
	{
		Object[] objTab = new Object[] { REQUESTENUM.AUTHENTICATE_USER, user};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	Create(OnTaskCompleteListener post, SDKUser user)
	{
		Object[] objTab = new Object[] {REQUESTENUM.CREATE_USER, user};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void ReadAll(OnTaskCompleteListener post, SDKUser user)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_WITH_PICTURE_SEARCH, user };
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void ReadById(OnTaskCompleteListener post, SDKUser user)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_BY_ID_USER, user };
		new AsyncRequestCall(post).execute(objTab);
	}
	
	/**
	 * Launch Thread to perform the Update request
	 * 
	 * @param post Interface to execute after request
	 * @param id the user id
	 * @param isConnected tell if the user is connected
	 *
	 */
	public static void Update(OnTaskCompleteListener post, SDKUser user)
	{
		Object[] objTab = new Object[] { REQUESTENUM.UPDATE_USER, user};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	/**
	 * Launch Thread to perform the delete request
	 * 
	 * @param post Interface to execute after request
	 * @param id the user id
	 *
	 */
	public static void delete(OnTaskCompleteListener post, int id)
	{
		Object[] objTab = new Object[] { REQUESTENUM.DELETE_USER, id};
		new AsyncRequestCall(post).execute(objTab);
	}
}
