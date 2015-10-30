package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKNotification;

public class PerformNotification {
	public class PerformNotificationDelete implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKNotification notif = (SDKNotification)obj[1];
			result = new Object[]{obj[0], notif.delete()};
			return result;
		}
	}
	
	public class PerformNotificationRead implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKNotification notif = (SDKNotification)obj[1];
			result = new Object[]{obj[0], notif.read()};
			return result;
		}
	}
}
