package datas;


import Tools.Tools;
import interfaces.OnTaskCompleteListener;
import java.util.HashMap;
import java.util.Map;
import database.DBDatas;
import managers.StreamingServerActions;
import managers.TcpManagerSitchozr;
import sdk.SDKUser;
import Abstract.AbstractUsersData;
import android.annotation.SuppressLint;
import android.content.Context;

import com.voipsitchozr.main.VoipManager;

import managers.UserManager;
import memory.ImageLoader;
import memory.MemoryManager;
import datas.MatchProfile;

/**
 * The static manager class
 * This class is an accesser for all important datas across the application
 *
 */
public class Manager {

	static public TcpManagerSitchozr				tcpManagerSitchozr;
	static public VoipManager 						voipManager;
	static public StreamingServerActions 			serverActions;
	static ImageLoader								imageLoader;
	static Profile									profile;
	static private Map<Integer, MatchProfile>		matchProfiles;
	static private Map<Integer, AbstractUsersData>	discoveryProfiles;
	static private DBDatas 							db;
	static public Context 							context;
	static private Context 							appContext;

	@SuppressLint("UseSparseArrays")
	public static void init(Context context)
	{
		matchProfiles = new HashMap<Integer, MatchProfile>();
		discoveryProfiles = new HashMap<Integer, AbstractUsersData>();
		imageLoader = new ImageLoader(context);
		db = new DBDatas(context);
		voipManager = new VoipManager(context);
		serverActions = new StreamingServerActions(context);
		tcpManagerSitchozr = new TcpManagerSitchozr(context);
	}
	
	
	public static DBDatas getDatabase() {
		return db;
	}
	
	public static void saveProfile(){
		OnTaskCompleteListener    onPostUpdate = new OnTaskCompleteListener() {
            @Override
            public void onCompleteListerner(Object[] result) {
            	profile.setSdkuser((SDKUser)result[1]);
            	//MemoryManager.saveProfile();
            }
        };
		if (Tools.isNetworkAvailable())
			UserManager.ApiUpdate(onPostUpdate, profile.getSdkuser());
	}
	
	public static void		giveWink(int idDest)
	{
		//call API
	}
	
	public static void		giveLike(int idDest)
	{
		//call API
	}
	
	public static ImageLoader getImageLoader()
	{
		return imageLoader;
	}
	
	public static void addMatchProfile(MatchProfile match)
	{
		matchProfiles.put(match.getId(), match);
	}
	
	public static MatchProfile getMatchProfileById(int id)
	{
		return matchProfiles.get(id);
	}
	
	public static void addDiscoveryProfile(DiscoveryProfile profile)
	{
		discoveryProfiles.put(profile.getId(), profile);
	}
	
	public static AbstractUsersData getDiscoveryProfileById(int id)
	{
		return discoveryProfiles.get(id);
	}
	
	
	public static void	deleteMatch(String id)
	{
		//call sdk delete
		matchProfiles.remove(id);
	}
	
	public static void	deleteDiscovery(int id)
	{
		//call sdk delete
		discoveryProfiles.remove(id);
	}
	
	/**
	 * @return the user
	 */
	public static Map<Integer, MatchProfile> getMatchProfiles() {
		return matchProfiles;
	}
	/**
	 * @param user the user to set
	 */
	public static void setMatchProfiles(Map<Integer, MatchProfile> match) {
		Manager.matchProfiles = match;
	}
	/**
	 * Get the profile
	 * 
	 * @return the profile
	 *
	 */
	static public Profile getProfile()
	{
		return profile;
	}
	/**
	 * set the profile
	 * 
	 * @param p the profile to set
	 *
	 */
	static public void setProfile(Profile p)
	{
		profile = p;
	}

	public static Map<Integer, AbstractUsersData> getDiscoveryProfiles() {
		return discoveryProfiles;
	}

	public static void setDiscoveryProfiles(Map<Integer, AbstractUsersData> discoveryProfiles) {
		Manager.discoveryProfiles = discoveryProfiles;
	}

	public static AbstractUsersData getUserById(Integer id) {
		if (profile.getId() == id) {
			return profile;
		}
		if (matchProfiles.containsKey(id)) {
			return matchProfiles.get(id);
		}
		return discoveryProfiles.get(id);
	}


	public static void updateProfileImages() {
		// call API
	}


	public static Context getContext() {
		return context;
	}


	public static void setContext(Context context) {
		Manager.context = context;
	}


	public static Context getAppContext() {
		return appContext;
	}


	public static void setAppContext(Context appContext) {
		Manager.appContext = appContext;
	}
	
}
