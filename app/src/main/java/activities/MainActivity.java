package activities;

import Tools.Tools;
import datas.MatchProfile;
import interfaces.OnTaskCompleteListener;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import notifications.GCMRegister;

import org.json.JSONException;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.voipsitchozr.options.ConnexionOptions;

import managers.DeviceManager;
import managers.FacebookManager;
import managers.ImageManager;
import managers.MatchManager;
import managers.UserManager;
import memory.MemoryManager;
import datas.AgeCalculator;
import datas.Album;
import datas.DiscoveryProfile;
import datas.FacebookAlbum;
import datas.FacebookPhoto;
import datas.FacebookResponse;
import datas.Images;
import datas.LocationWraper;
import datas.Manager;
import datas.Profile;
import AsyncUserRequest.PerformUserLaunchAsync;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import requestApiManager.RequestManager;
import sdk.SDKDevice;
import sdk.SDKMatch;
import sdk.SDKPicture;
import sdk.SDKUser;
import service.WebSocketIntentService;
import sources.sitchozt.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends FragmentActivity {	
	LoginButton 			loginButton;
	CallbackManager 		callbackManager;
	AccessToken 			accessToken;
	SDKUser 				user;
	static int 				index = 0;
	private LocationWraper 	location = null;
	private	boolean			isFirstTime = true;
	public static GCMRegister gcm;
    private boolean launchOne = false;
    private boolean existOnApi = false;
    private boolean    isInit = false;

	private void initFacebook() {
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		setContentView(R.layout.activity_main);
		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile", "user_birthday", "user_photos");
		callbackManager = CallbackManager.Factory.create();
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {
								startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
						finish();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	private void checkLocation(Context context){
		location = new LocationWraper(context);
		if (location.isGpsActivated())
			location.initConnection();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Manager.context = this;
		//Tools.connectivityThreadLoop();
		location = new LocationWraper(getApplicationContext());
		if (!location.isGpsActivated())
			buildAlertMessageNoGps();
		else if (Tools.isNetworkAvailable())
			connect();
	}

	public void authenticate() {
		Log.e("MainActivity", "Authenticating...");
		accessToken = AccessToken.getCurrentAccessToken();
		user = new SDKUser(accessToken.getToken());
		OnTaskCompleteListener onPostUserCreate = new OnTaskCompleteListener() {
			@Override
			public void onCompleteListerner(Object[] result) {
				Log.e("MainActivity", "onPostCreate...");
				OnTaskCompleteListener onPostAuthentication = new OnTaskCompleteListener() {
					@Override
					public void onCompleteListerner(Object[] result) {
						gcm = new GCMRegister(getApplicationContext());
						Log.e("MainActivity", "onPOstAuthenticate...");
						if (!isInit) {
							Log.e("MainActivity", "initCOnf and discovery...");
							isInit = true;
                            initConfiguration();
                            initDiscovery();
                        }
					}
				};
				user = (SDKUser) result[1];
				UserManager.ApiAuthenticate(onPostAuthentication, user);
			}
		};
		UserManager.ApiCreate(onPostUserCreate, user);
		Log.e("MainActivity", "ApiCreate...");
	}

	private void connect() {
		Log.e("MainActivity", "Connecting...");
		initFacebook();
		RequestManager.getInstance();
		if (AccessToken.getCurrentAccessToken() == null) {
			loginButton.registerCallback(callbackManager,
					new FacebookCallback<LoginResult>() {
						@Override
						public void onSuccess(LoginResult loginResult) {
							authenticate();
						}

						@Override
						public void onCancel() {

						}

						@Override
						public void onError(FacebookException exception) {

						}
					});
		} else {
			authenticate();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Manager.setContext(this);
		Manager.setAppContext(getApplicationContext());
		//Tools.Tools.displayConnectionToast();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	public void getAllPicturesFromFacebook() {
		Log.e("MainActivity", "Get Pictures...");
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookPhoto> facebookResponse = new FacebookResponse<FacebookPhoto>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(), FacebookPhoto[].class));
                    Manager.getDatabase().createAlbum(1, "All");
                    for (FacebookPhoto photo : facebookResponse.getData()) {
                        SDKPicture picture = new SDKPicture(photo, index, false);
                        Manager.getDatabase().createPictureProfile(picture, 1, Manager.getProfile().getId());
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		FacebookManager.getUserPhotos(callback, Manager.getProfile().getAccessToken(), Manager.getProfile().getSdkuser().getFacebookId());
    }

    public void initConfiguration() {
        MemoryManager.init(this);
		Log.e("MainActivity", "end of Manager.init...");
        Manager.init(this);
		Log.e("MainActivity", "setcontext...");
        Manager.setContext(this);
		Log.e("MainActivity", "end of location...");
        location = new LocationWraper(this, user.getLatitude(), user.getLongitude());
		Log.e("MainActivity", "end of profile...");
        Profile profile = new Profile(user, accessToken, location);
		Log.e("MainActivity", "end of setprofikle...");
        Manager.setProfile(profile);
		Log.e("MainActivity", "checklocation...");
        checkLocation(this);
		Log.e("MainActivity", "addmatche...");
        Manager.setContext(null);
       /*if (MemoryManager.isFirstTime()) {
			Log.e("MainActivity", "isfirsttime...");

        }*/
		Log.e("MainActivity", "end of initconfiguration...");

	}

	public void initProfile() {
		Log.e("MainActivity", "Init profile...");

		addMatches();
		isFirstTime = true;
        if (MemoryManager.isFirstTime() == true)
            System.out.println("First time is true");
        else
            System.out.println("First time is false");

        if (MemoryManager.isFirstTime()) {
            getAllPicturesFromFacebook();
            getPicturesProfileFromApi();
        }
        else
            launchActivity();
	}

	public void getPicturesProfileFromApi() {
		Log.e("MainActivity", "Get profile pictures from API...");
		OnTaskCompleteListener onPostReadPicture = new OnTaskCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteListerner(Object[] result) {
				if (result[1] != null && !((List<SDKPicture>) result[1]).isEmpty()) {
                    System.out.println("Exist on API");
					isFirstTime = false;
                    existOnApi = true;
					List<SDKPicture> pictures = (List<SDKPicture>) result[1];
					for (SDKPicture sdkpicture : pictures) {
                        System.out.println("createPictureProfile()");
						Manager.getDatabase().createPictureProfile(sdkpicture, 0, Manager.getProfile().getId());
					}
				}
                if (MemoryManager.isFirstTime()) {
                    System.out.println("getAlbumsFromFacebook()");
                    Manager.getDatabase().createAlbum(0, "Defined Pictures");
                    getAlbumsFromFacebook();
                }
                if (!launchOne && existOnApi == true) {
                    launchOne = true;
                    launchActivity();
                    launchActivity();
                }
			}
		};
		if (Tools.isNetworkAvailable())
			ImageManager.ApiReadByUserId(onPostReadPicture, user);
	}

	public void getProfilePictures(final FacebookAlbum album, final Album profileAlbum) {
		Log.e("MainActivity", "Save profile picture from facebook to API...");
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookPhoto> facebookResponse = new FacebookResponse<FacebookPhoto>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(), FacebookPhoto[].class));
					//Manager.getProfile().addAlbumToList(new Album("All"));
					final Integer[] i = {0};
					final Integer[] done = {0};
					for (final FacebookPhoto photo : facebookResponse.getData()) {
						if (i[0] < 8) {
                            System.out.println("adding facebook picture profile: " + facebookResponse.getData().length);
							SDKPicture picture = new SDKPicture(photo, 0);
							picture.setDate(new Date());
							OnTaskCompleteListener listener = new OnTaskCompleteListener() {
								@Override
								public void onCompleteListerner(Object[] result) {
									System.out.println("Complete listener !");
									SDKPicture picture = (SDKPicture)result[1];
									Manager.getDatabase().createPictureProfile(picture, 0, Manager.getProfile().getId());
									done[0]++;
                                    System.out.println("done: " + done[0] + " - i: " + i[0]);
									if (done[0]+1 == 8 || done[0] == i[0]) {
                                        System.out.println("Launch activity from complete listener");
										launchActivity();
									}
								}
							};
							if (Tools.isNetworkAvailable())
								ImageManager.ApiCreate(listener, photo, i[0], i[0] == 0 ? true : false);
						}
						i[0]++;
					}
					//getPicturesProfileFromApi();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		if (Tools.isNetworkAvailable())
			FacebookManager.getPhotosByAlbum(callback, Manager.getProfile().getAccessToken(), album.getId());
	}

	public void getPictures(final FacebookAlbum album, final Album profileAlbum) {
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookPhoto> facebookResponse = new FacebookResponse<FacebookPhoto>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(), FacebookPhoto[].class));
					boolean profilePicture = true;
					//ArrayList<Images> list = new ArrayList();
					for (FacebookPhoto photo : facebookResponse.getData()) {
						SDKPicture picture;
						picture = new SDKPicture(photo, index, profilePicture);
                        Manager.getDatabase().createPictureProfile(picture, album.getId(), Manager.getProfile().getId());
						//list.add(new Images(picture.getUrl(), picture.getId(), Long.parseLong(picture.getFacebookId())));
					}
					//profileAlbum.setList(list);
					//Manager.getProfile().addAlbumToList(profileAlbum);
                } catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		if (Tools.isNetworkAvailable())
			FacebookManager.getPhotosByAlbum(callback, Manager.getProfile().getAccessToken(), album.getId());
	}

	public void getAlbumsFromFacebook() {
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookAlbum> facebookResponse = new FacebookResponse<FacebookAlbum>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(),FacebookAlbum[].class));
					for (FacebookAlbum facebookAlbum : facebookResponse.getData()) {
                        System.out.println("Album name: " + facebookAlbum.getName());
						//Album album = new Album(facebookAlbum.getName());
                        Manager.getDatabase().createAlbum(facebookAlbum.getId(), facebookAlbum.getName());
                        if (existOnApi == false && facebookAlbum.getName().equals("Profile Pictures")) {
							System.out.println("Profile Pictures");
							if (isFirstTime == true) {
								Log.e("MainActivity", "SAve new pictures on api...");
								isFirstTime = false;
								getProfilePictures(facebookAlbum, null);
							}
						}
						//else {
                            getPictures(facebookAlbum, null);
							//Manager.getProfile().addAlbumToList(album);
						//}
					}

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
		if (Tools.isNetworkAvailable())
			FacebookManager.getAlbums(callback, Manager.getProfile()
					.getAccessToken(), Manager.getProfile().getSdkuser()
					.getFacebookId());
	}

	public static void addMatches() {
		Log.e("MainActivity", "Add matches...");
		OnTaskCompleteListener onPostGetMatches = new OnTaskCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteListerner(Object[] result) {
				List<SDKMatch> matches = (List<SDKMatch>) result[1];
				OnTaskCompleteListener onPostReadById = new OnTaskCompleteListener() {
					@Override
					public void onCompleteListerner(Object[] result) {
						final SDKUser matchUser = new SDKUser((SDKUser) result[1]);
						//Manager.getDatabase().createMatch(matchUser);
						if (matchUser != null) {
							final MatchProfile matchProfile = new MatchProfile(matchUser, AccessToken.getCurrentAccessToken());
							OnTaskCompleteListener onPostReadPictureByUserId = new OnTaskCompleteListener() {
								@Override
								public void onCompleteListerner(Object[] result) {
									List<SDKPicture> pictures = (List<SDKPicture>) result[1];
									for (SDKPicture sdkpicture : pictures) {
										Images image = new Images(sdkpicture.getUrl(), sdkpicture.getId());
										if (sdkpicture.isProfilePicture() || pictures.size() == 1)
											matchProfile.setProfileImage(image);
										matchProfile.addImagesToArray(image);
										//Manager.getDatabase().createPicture(sdkpicture, matchUser.getId());
									}
									Manager.addMatchProfile(matchProfile);
									//Manager.getDatabase().getMatchsAndPictures();
									//Manager.getDatabase().createMatch(matchUser);
								}
							};
							if (Tools.isNetworkAvailable())
								ImageManager.ApiReadByUserId(onPostReadPictureByUserId, matchUser);
						}
					}
				};

				for (SDKMatch match : matches) {
					SDKUser user = new SDKUser();
					user.setId(match.getUserId());
					if (Tools.isNetworkAvailable())
						UserManager.ApiReadById(onPostReadById, user);
				}
			}
		};
		if (Tools.isNetworkAvailable())
		MatchManager.ApiReadAll(onPostGetMatches);
	}

	public void initDiscovery() {
		Log.e("MainActivity", "Add discovery...");
		final Context context = this;
		OnTaskCompleteListener onPostReadAll = new OnTaskCompleteListener() {
			@SuppressLint("UseValueOf")
			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteListerner(Object[] result) {
				List<SDKUser> users = (List<SDKUser>) result[1];
				Manager.setContext(context);
				for (SDKUser discoveryUser : users) {
					DiscoveryProfile profile = new DiscoveryProfile(discoveryUser, accessToken);
					for (SDKPicture picture : discoveryUser.getPictures()) {
						Images image = new Images(picture.getUrl(),picture.getId());
						if (picture.isProfilePicture() == true)
							profile.setProfileImage(image);
							profile.addImagesToArray(image);
					}
					Manager.addDiscoveryProfile(profile);
				}
                initProfile();
				Manager.setContext(null);
			}
		};
		if (Tools.isNetworkAvailable())
			PerformUserLaunchAsync.ReadAll(onPostReadAll, new SDKUser());
	}

	public void launchActivity() {
		Log.e("MainActivity", "Launch Activity...");
        MemoryManager.setFirstTime(false);
		Intent intent = new Intent(this, NavigationActivity.class);
		try {
			ConnexionOptions.SERVER_IP = "87.98.209.15";
			//ConnexionOptions.SERVER_IP = "172.19.133.143";
			ConnexionOptions.SERVER_PORT = 3031;
			Manager.tcpManagerSitchozr.initializeTcpConnection();
			Manager.voipManager.initializeTcpConnexion();
			Manager.tcpManagerSitchozr.addUsersActions();
			Manager.serverActions.addActions();
			Manager.tcpManagerSitchozr.connect();
			Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("connect " + Manager.getProfile().getId());
		} catch (SocketException e) {
			e.printStackTrace();
		}
		startActivity(intent);
		finish();
	}
	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		//if (Manager.tcpManagerSitchozr != null) ;
		//	Manager.tcpManagerSitchozr.disconnect();
		super.onDestroy();
	}
}
