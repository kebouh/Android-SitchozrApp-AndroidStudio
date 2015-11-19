package activities;

import interfaces.OnTaskCompleteListener;

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
import android.widget.Toast;
import requestApiManager.RequestManager;
import sdk.SDKDevice;
import sdk.SDKMatch;
import sdk.SDKPicture;
import sdk.SDKUser;
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
	private boolean 		existingPictures = false;
	private LocationWraper 	location = null;
	private	boolean			isFirstTime = true;

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
	//	location = new LocationWraper(context);
		if (location.isGpsActivated())
			location.initConnection();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		location = new LocationWraper(getApplicationContext());
		if (!location.isGpsActivated())
			buildAlertMessageNoGps();
		else
			connect();
	}

	public void authenticate() {
		user = new SDKUser(accessToken.getToken());
		OnTaskCompleteListener onPostUserCreate = new OnTaskCompleteListener() {
			@Override
			public void onCompleteListerner(Object[] result) {
				OnTaskCompleteListener onPostAuthentication = new OnTaskCompleteListener() {
					@Override
					public void onCompleteListerner(Object[] result) {
						GCMRegister gcm = new GCMRegister(getApplicationContext());
						DeviceManager.ApiCreate(null, new SDKDevice(gcm.getRegistrationId(getApplicationContext()), "en"));
						initProfile();
						initDiscovery();	
					}
				};
				user = (SDKUser) result[1];
				UserManager.ApiAuthenticate(onPostAuthentication, user);
			}
		};
		UserManager.ApiCreate(onPostUserCreate, user);
	}

	private void connect() {
		initFacebook();
		RequestManager.getInstance();
		if (AccessToken.getCurrentAccessToken() == null) {
			loginButton.registerCallback(callbackManager,
					new FacebookCallback<LoginResult>() {
						@Override
						public void onSuccess(LoginResult loginResult) {
							accessToken = AccessToken.getCurrentAccessToken();
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
			accessToken = AccessToken.getCurrentAccessToken();
			authenticate();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Manager.setContext(null);
		Manager.setAppContext(getApplicationContext());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	public void getAllPictures() {
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookPhoto> facebookResponse = new FacebookResponse<FacebookPhoto>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(), FacebookPhoto[].class));
					Manager.getProfile().addAlbumToList(new Album("All"));
					int i= 0;
					for (FacebookPhoto photo : facebookResponse.getData()) {
						if (i < 8) {
							SDKPicture picture = new SDKPicture(photo, 0);
							picture.setDate(new Date());
							ImageManager.ApiCreate(null, photo, i, i==0?true:false);
							Manager.getProfile().setProfileImage(new Images(photo));
						}
						i++;
					}
					getPicturesProfileFromApi();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		FacebookManager.getUserProfilePhoto(callback, Manager.getProfile().getAccessToken(), Manager.getProfile().getSdkuser().getFacebookId());
	}

	public void initProfile() {
		Manager.init(this);
		Manager.setContext(this);
		location = new LocationWraper(this, user.getLatitude(), user.getLongitude());
		Profile profile = new Profile(user, accessToken, location);
		Manager.setProfile(profile);
		System.out.println("/!\\ IF STUCK HERE, ACTIVATE GPS IN EMULATEUR /!\\");
		checkLocation(this);
		Manager.setContext(null);
		//retrieveProfileDatasFromMemory();
		if (MemoryManager.isFirstTime()) {
			addMatches();
		}
			//Manager.getDatabase().createAlbum(0, "Defined Pictures");
		getPicturesProfileFromApi();
		//} else {
		//profile.setProfileImage(profile.getProfileImage());
		launchActivity();
		//}
	}

	public void getPicturesProfileFromApi() {
		OnTaskCompleteListener onPostReadPicture = new OnTaskCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteListerner(Object[] result) {
				if (result[1] != null && !((List<SDKPicture>) result[1]).isEmpty()) {
					isFirstTime = false;
					existingPictures = true;
					List<SDKPicture> pictures = (List<SDKPicture>) result[1];
					for (SDKPicture sdkpicture : pictures) {
						Images image = new Images(sdkpicture.getUrl(), sdkpicture.getId(), Long.parseLong(sdkpicture.getFacebookId()));
						if (sdkpicture.isProfilePicture() == true)
							Manager.getProfile().setProfileImage(image);
						Manager.getProfile().addImagesToArray(image);
						Manager.getDatabase().createPictureProfile(sdkpicture, 0, Manager.getProfile().getId());
					}
					//TODO : REMETTRE LA PHOTO DE PROFIL
					Manager.getProfile().setProfileImage(Manager.getDatabase().getProfilePicture());
					//MemoryManager.setFirstTime(false);
					launchActivity();
				}
				else {
					//Toast.makeText(Manager.getAppContext(), "No photos store in the API (new user ?)", Toast.LENGTH_LONG).show();
					getAllPictures();
				}
				getAlbums();
			}

		};
		ImageManager.ApiReadByUserId(onPostReadPicture, user);
	}

	public void getProfilePictures(final FacebookAlbum album, final Album profileAlbum) {
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookPhoto> facebookResponse = new FacebookResponse<FacebookPhoto>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(), FacebookPhoto[].class));
					Manager.getProfile().addAlbumToList(new Album("All"));
					int i= 0;
					for (FacebookPhoto photo : facebookResponse.getData()) {
						if (i < 8) {
							SDKPicture picture = new SDKPicture(photo, 0);
							picture.setDate(new Date());
							ImageManager.ApiCreate(null, photo, i, i==0?true:false);
							Manager.getProfile().setProfileImage(new Images(photo));
						}
						i++;
					}
					getPicturesProfileFromApi();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
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
					ArrayList<Images> list = new ArrayList();
					for (FacebookPhoto photo : facebookResponse.getData()) {
						SDKPicture picture;
						picture = new SDKPicture(photo, index, profilePicture);
						list.add(new Images(picture.getUrl(), picture.getId(), Long.parseLong(picture.getFacebookId())));
					}
					profileAlbum.setList(list);
					Manager.getProfile().addAlbumToList(profileAlbum);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		FacebookManager.getPhotosByAlbum(callback, Manager.getProfile().getAccessToken(), album.getId());
	}

	public void getAlbums() {
		GraphRequest.Callback callback = new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse response) {
				try {
					Gson gson = new Gson();
					FacebookResponse<FacebookAlbum> facebookResponse = new FacebookResponse<FacebookAlbum>();
					facebookResponse.setData(gson.fromJson(response.getJSONObject().get("data").toString(),FacebookAlbum[].class));
					for (FacebookAlbum facebookAlbum : facebookResponse.getData()) {
						Album album = new Album(facebookAlbum.getName());
						if (album.getName().equals("Profile Pictures")) {
							if (isFirstTime == true) {
								getProfilePictures(facebookAlbum, album);
							}
						}
						else {
							Manager.getProfile().addAlbumToList(album);
							Manager.getDatabase().createAlbum(facebookAlbum.getId(), facebookAlbum.getName());
							getPictures(facebookAlbum, album);
						}
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
		FacebookManager.getAlbums(callback, Manager.getProfile()
				.getAccessToken(), Manager.getProfile().getSdkuser()
				.getFacebookId());
	}

	public static void addMatches() {
		OnTaskCompleteListener onPostGetMatches = new OnTaskCompleteListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteListerner(Object[] result) {
				List<SDKMatch> matches = (List<SDKMatch>) result[1];
				OnTaskCompleteListener onPostReadById = new OnTaskCompleteListener() {
					@Override
					public void onCompleteListerner(Object[] result) {
						final SDKUser matchUser = new SDKUser((SDKUser)result[1]);
						Manager.getDatabase().createMatch(matchUser);
						OnTaskCompleteListener onPostReadPictureByUserId = new OnTaskCompleteListener() {
							@Override
							public void onCompleteListerner(Object[] result) {
								List<SDKPicture> pictures = (List<SDKPicture>) result[1];
								for (SDKPicture sdkpicture : pictures) {
									Manager.getDatabase().createPicture(sdkpicture, matchUser.getId());
								}
								// Manager.getDatabase().getMatchsAndPictures();
								// Manager.addMatchProfile(matchProfile);
							}
						};
						ImageManager.ApiReadByUserId(onPostReadPictureByUserId, matchUser);
					}
				};
				for (SDKMatch match : matches) {
					SDKUser user = new SDKUser();
					user.setId(match.getUserId());
					UserManager.ApiReadById(onPostReadById, user);
				}
			}
		};
		MatchManager.ApiReadAll(onPostGetMatches);
	}

	public void initDiscovery() {
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
						else
							profile.addImagesToArray(image);
					}
					Manager.addDiscoveryProfile(profile);
				}
				Manager.setContext(null);
			}
		};
		PerformUserLaunchAsync.ReadAll(onPostReadAll, new SDKUser());
	}

	public void launchActivity() {
		Intent intent = new Intent(this, NavigationActivity.class);
		startActivity(intent);
		finish();
	}

	public void retrieveProfileDatasFromMemory() {
		MemoryManager.init(getApplicationContext());
		MemoryManager.getProfileFromMemory();
	}
}
