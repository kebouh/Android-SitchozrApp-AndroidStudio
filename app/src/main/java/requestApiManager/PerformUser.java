package requestApiManager;


//import sitchozrSDK.classes.User;

import sdk.SDKUser;
import interfaces.IRequestProcess;

public class PerformUser {

	/**
	 * That class encapsule the call of the Authenticate request to the API
	 * This class MUST be used in a thread and not on the main thread
	 * It return an Object[] containing the result of the request
	 */
	public class PerformUserAuthenticate implements IRequestProcess {

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKUser user = (SDKUser)obj[1];
			result = new Object[]{obj[0], user.authenticate()};
			return result;
		}
	}
	/**
	 * That class encapsule the call of the Create request to the API
	 * This class MUST be used in a thread and not on the main thread
	 * It return an Object[] containing the result of the request
	 */
	public class PerformUserCreate implements IRequestProcess { 

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKUser user = (SDKUser)obj[1];
			result = new Object[]{obj[0], user.create()};
			return result;
		}
	}
	
	
	/**
	 * That class encapsule the call of the ReadAll request to the API
	 * This class MUST be used in a thread and not on the main thread
	 * It return an Object[] containing the result of the request
	 */
	public class PerformUserReadAll implements IRequestProcess {

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKUser user = (SDKUser)obj[1];
			result = new Object[]{obj[0], user.getAll()};
			return result;
		}
	}

	/**
	 * That class encapsule the call of the ReadById request to the API
	 * This class MUST be used in a thread and not on the main thread
	 * It return an Object[] containing the result of the request
	 */
	public class PerformUserReadById implements IRequestProcess {

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKUser user = (SDKUser)obj[1];
			result = new Object[]{obj[0], user.getById()};
			return result;
		}
	}
	/**
	 * That class encapsule the call of the Update request to the API
	 * This class MUST be used in a thread and not on the main thread
	 * It return an Object[] containing the result of the request
	 */
	public class PerformUserUpdate implements IRequestProcess {

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKUser user = (SDKUser)obj[1];
			result = new Object[]{obj[0], user.update()};
			return result;

		}
	}
	/**
	 * That class encapsule the call of the Delete request to the API
	 * This class MUST be used in a thread and not on the main thread
	 * It return an Object[] containing the result of the request
	 */
	public class performUserDelete implements IRequestProcess {

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			//int id = Integer.parseInt(obj[1].toString());
			//result = new Object[] { obj[0], User.delete(id) };
			return result;

		}
	}
}
