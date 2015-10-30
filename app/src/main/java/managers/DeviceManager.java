package managers;

import AsyncUserRequest.PerformDeviceLaunchAsync;
import interfaces.OnTaskCompleteListener;
import sdk.SDKDevice;

public class DeviceManager {
	public static void	ApiCreate(OnTaskCompleteListener callback, SDKDevice device){
		PerformDeviceLaunchAsync.create(callback, device);
	}
	
	public static void	ApiDelete(OnTaskCompleteListener callback, SDKDevice device){
		PerformDeviceLaunchAsync.delete(callback, device);
	}
	
	public static void	ApiRead(OnTaskCompleteListener callback, SDKDevice device){
		PerformDeviceLaunchAsync.read(callback, device);
	}
	
	public static void	ApiUpdate(OnTaskCompleteListener callback, SDKDevice device){
		PerformDeviceLaunchAsync.update(callback, device);
	}
}
