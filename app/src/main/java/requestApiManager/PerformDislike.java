package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKDislike;

public class PerformDislike {
	public class PerformDislikeCreate implements IRequestProcess { 

		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKDislike dislike = (SDKDislike)obj[1];
			result = new Object[]{obj[0], dislike.create()};
			return result;
		}
	}
}
