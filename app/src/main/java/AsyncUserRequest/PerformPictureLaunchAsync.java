package AsyncUserRequest;

import interfaces.OnTaskCompleteListener;
import requestApiManager.AsyncRequestCall;
import sdk.SDKPicture;
import sdk.SDKUser;
import enumerator.REQUESTENUM;

public class PerformPictureLaunchAsync {
	
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
	
	public static void	create(OnTaskCompleteListener post, SDKPicture picture)
	{
		Object[] objTab = new Object[] { REQUESTENUM.CREATE_PICTURE, picture};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	getById(OnTaskCompleteListener post, SDKPicture picture)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_BY_ID_PICTURE, picture};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	getProfilePicture(OnTaskCompleteListener post, SDKUser user)
	{
		try{
			Object[] objTab = new Object[] { REQUESTENUM.READ_PROFILE_PICTURE, new SDKPicture(), user};
			new AsyncRequestCall(post).execute(objTab);
		} catch (RuntimeException e){
			System.out.println("LaunchAsync");
			throw e;
		}
	}
	
	public static void	getAll(OnTaskCompleteListener post, SDKPicture picture)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_ALL_PICTURE, picture};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	getByUserId(OnTaskCompleteListener post, SDKPicture picture, SDKUser user)
	{
		Object[] objTab = new Object[] { REQUESTENUM.READ_BY_USER_ID_PICTURE, picture, user};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	delete(OnTaskCompleteListener post, SDKPicture picture)
	{
		Object[] objTab = new Object[] { REQUESTENUM.DELETE_PICTURE, picture};
		new AsyncRequestCall(post).execute(objTab);
	}
	
	public static void	update(OnTaskCompleteListener post, SDKPicture picture)
	{
		Object[] objTab = new Object[] { REQUESTENUM.UPDATE_PICTURE, picture};
		new AsyncRequestCall(post).execute(objTab);
	}
}
