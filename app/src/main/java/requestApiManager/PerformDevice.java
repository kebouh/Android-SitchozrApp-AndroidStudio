package requestApiManager;

import interfaces.IRequestProcess;
import sdk.SDKDevice;

public class PerformDevice {
	public class PerformDeviceCreate implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKDevice device = (SDKDevice)obj[1];
			result = new Object[]{obj[0], device.create()};
			return result;
		}
	}
	
	public class PerformDeviceDelete implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKDevice device = (SDKDevice)obj[1];
			result = new Object[]{obj[0], device.delete()};
			return result;
		}
	}
	
	public class PerformDeviceRead implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKDevice device = (SDKDevice)obj[1];
			result = new Object[]{obj[0], device.read()};
			return result;
		}
	}
	
	public class PerformDeviceUpdate implements IRequestProcess { 
		@Override
		public Object[] performRequest(Object[] obj) {
			Object[] result = null;
			SDKDevice device = (SDKDevice)obj[1];
			result = new Object[]{obj[0], device.update()};
			return result;
		}
	}
}
