package requestApiManager;

import interfaces.IRequestProcess;

import java.util.HashMap;
import java.util.Map;


import enumerator.REQUESTENUM;


/**
 * Static class that manage the request
 *
 */
public class RequestManager {

	public static RequestManager _instance = null;
	
	
	private static Map<REQUESTENUM, IRequestProcess> _requestMap;

	/**
	 * Static method, get an unique Instance of the object
	 * @return A static instance of RequestManager
	 */
	public static RequestManager getInstance()
	{
		if (_instance == null)
			_instance = new RequestManager();
		return _instance;
	}
	
	/**
	 * Constructor of RequestManager
	 */
	private RequestManager() {
		_requestMap = new HashMap<REQUESTENUM, IRequestProcess>();
		addPerformUserInner();
		addPerformPictureInner();
		addPerformMatchInner();
		addPerformMessageInner();
		addPerformLikeInner();
		addPerformDislikeInner();
		addPerformDeviceInner();
		addPerformNotificationInner();
	}

	/**
	 * Add a request to the manager
	 * @param key the key of the request
	 * @param the value to add
	 */
	private static void addToRequestMap(REQUESTENUM key, IRequestProcess value) {
		_requestMap.put(key, value);
	}
	
	private static void addPerformNotificationInner(){
		PerformNotification pNotification = new PerformNotification();
		addToRequestMap(REQUESTENUM.DELETE_NOTIFICATION, pNotification.new PerformNotificationDelete());
		addToRequestMap(REQUESTENUM.READ_NOTIFICATION, pNotification.new PerformNotificationRead());
	}
	
	private static void addPerformDeviceInner() {
		PerformDevice	pDevice = new PerformDevice();
		addToRequestMap(REQUESTENUM.CREATE_DEVICE, pDevice.new PerformDeviceCreate());
		addToRequestMap(REQUESTENUM.DELETE_DEVICE, pDevice.new PerformDeviceDelete());
		addToRequestMap(REQUESTENUM.READ_DEVICE, pDevice.new PerformDeviceRead());
		addToRequestMap(REQUESTENUM.UPDATE_DEVICE, pDevice.new PerformDeviceUpdate());
	}
	
	private static void addPerformLikeInner(){
		PerformLike pLike = new PerformLike();
		addToRequestMap(REQUESTENUM.CREATE_LIKE, pLike.new PerformLikeCreate());
	}
	
	private static void addPerformDislikeInner(){
		PerformDislike pDislike = new PerformDislike();
		addToRequestMap(REQUESTENUM.CREATE_DISLIKE, pDislike.new PerformDislikeCreate());
	}
	
	private static void addPerformUserInner()
	{
		PerformUser pUser = new PerformUser();
		addToRequestMap(REQUESTENUM.AUTHENTICATE_USER, pUser.new PerformUserAuthenticate());
		addToRequestMap(REQUESTENUM.READ_BY_ID_USER, pUser.new PerformUserReadById());
		addToRequestMap(REQUESTENUM.UPDATE_USER, pUser.new PerformUserUpdate());
		addToRequestMap(REQUESTENUM.READ_WITH_PICTURE_SEARCH, pUser.new PerformUserReadAll());
		addToRequestMap(REQUESTENUM.CREATE_USER, pUser.new PerformUserCreate());
	}
	
	private static void addPerformMatchInner(){
		PerformMatch pMatch = new PerformMatch();
		addToRequestMap(REQUESTENUM.READ_ALL_MATCH, pMatch.new PerformMatchReadAll());
	}
	
	private static void addPerformPictureInner()
	{
		PerformPicture pPicture = new PerformPicture();
		addToRequestMap(REQUESTENUM.CREATE_PICTURE, pPicture.new PerformPictureCreate());
		addToRequestMap(REQUESTENUM.READ_BY_ID_PICTURE, pPicture.new PerformPictureRead());
		addToRequestMap(REQUESTENUM.READ_ALL_PICTURE, pPicture.new PerformPictureReadAll());
		addToRequestMap(REQUESTENUM.READ_BY_USER_ID_PICTURE, pPicture.new PerformPictureReadByUserId());
		addToRequestMap(REQUESTENUM.READ_PROFILE_PICTURE, pPicture.new PerformPictureReadProfilePicture());
		addToRequestMap(REQUESTENUM.DELETE_PICTURE, pPicture.new PerformPictureDelete());
		addToRequestMap(REQUESTENUM.UPDATE_PICTURE, pPicture.new PerformPictureUpdate());
	}
	
	private static void addPerformMessageInner()
	{
		PerformMessage pMessage = new PerformMessage();
		addToRequestMap(REQUESTENUM.CREATE_MESSAGE, pMessage.new PerformMessageCreate());
		addToRequestMap(REQUESTENUM.READ_BY_USER_ID_MESSAGE, pMessage.new PerformMessageReadByUser());
	}
	
	/* Public Functions*/
	
	public static IRequestProcess	getFunctionFromEnum(REQUESTENUM key)
	{
		//System.out.println("value enum: " + key.toString());
		return _requestMap.get(key);
	}
}
