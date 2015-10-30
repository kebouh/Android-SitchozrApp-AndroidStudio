package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKMessage;

public class PerformMessage {
	public class PerformMessageCreate implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKMessage message = (SDKMessage)obj[1];
			result = new Object[]{obj[0], message.create()};
			return result;
		}
	}
	
	public class PerformMessageReadByUser implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKMessage message = (SDKMessage)obj[1];
			int userId = (Integer)obj[2];
			result = new Object[]{obj[0], message.get(userId)};
			return result;
		}
	}
}
