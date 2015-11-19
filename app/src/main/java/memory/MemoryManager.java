package memory;

import datas.Manager;
import datas.Profile;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Static class that save in memory setting and preferences
 */

public class MemoryManager {

	private static Context	context;
	public static boolean firstTime = true;

	private static Profile profile;


	/**
	 * Init the memory settings
	 * 
	 * @param c the context of the activity
	 *
	 */
	public static void init(Context c)
	{
		context = c;
		profile = Manager.getProfile();
	}
	
	/**
	 * Static, save the profile to the memory
	 *
	 */
	public static	void	saveProfile()
	{
		SharedPreferences profileSettings;
	    SharedPreferences.Editor profileEditor;
	    profileSettings = context.getSharedPreferences("profile", 0);
		profileEditor = profileSettings.edit();
		profileEditor.putInt("minAge", profile.getAgeRange()[0]);
		profileEditor.putInt("maxAge", profile.getAgeRange()[1]);
		profileEditor.putInt("minDistance", profile.getDistanceRange()[0]);
		profileEditor.putInt("maxDistance", profile.getDistanceRange()[1]);
		profileEditor.putInt("genderWanted", profile.getGenderWanted());

	      
		profileEditor.putString("description", profile.getDescription());
	      
		profileEditor.commit();
	}
	
	public static boolean isFirstTime()
	{
		return firstTime;
	}
	
	public static void setFirstTime(boolean val)
	{
		firstTime = val;
		SharedPreferences profileSettings;
	    SharedPreferences.Editor profileEditor;
	    profileSettings = context.getSharedPreferences("profile", 0);
		profileEditor = profileSettings.edit();
		profileEditor.putBoolean("firstTime", val);
		profileEditor.commit();
	}
}
