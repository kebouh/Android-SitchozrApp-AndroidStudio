package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKLike;

public class PerformLike {
	public class PerformLikeCreate implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKLike like = (SDKLike)obj[1];
			result = new Object[]{obj[0], like.create()};
			return result;
		}
	}
}
