package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKMatch;

public class PerformMatch {
	public class PerformMatchReadAll implements IRequestProcess {
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKMatch match = (SDKMatch)obj[1];
			result = new Object[]{obj[0], match.getByToken()};
			return result;
		}
	}
}
