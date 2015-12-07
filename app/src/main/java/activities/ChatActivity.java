package activities;

import datas.Images;
import interfaces.OnTaskCompleteListener;

import java.util.Date;
import java.util.List;

import managers.ImageManager;
import managers.MatchManager;
import managers.MessageManager;
import managers.UserManager;
import sdk.SDKMatch;
import sdk.SDKMessage;
import sdk.SDKPicture;
import sdk.SDKUser;
import sources.sitchozt.R;
import controllers.ChatController;
import datas.AgeCalculator;
import datas.Manager;
import datas.MatchProfile;
import adapters.ChatAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;

public class ChatActivity extends ListActivity {

	private static MatchProfile user = null;
	private static ChatAdapter chatAdapter = null;
	private static ChatController chatController = null;
	private EditText	editText = null;
	public static boolean		isActive = false;
	public static int			activeId = 0;
public static Activity activity;
	@Override
	public void onResume() {
	    super.onResume();
	    isActive = true;
		activeId = getIntent().getExtras().getInt("ID");
	    this.registerReceiver(mMessageReceiver, new IntentFilter("chat"));
		//chatController.getAllMessages(Manager.getMatchProfileById(activeId), chatAdapter);
		activity = this;
	}

	//Must unregister onPause()
	@Override
	protected void onPause() {
	    super.onPause();
	    isActive = false;
		activeId = 0;
	    this.unregisterReceiver(mMessageReceiver);
	}


	//This is the handler that will manager to process the broadcast intent
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {

	        // Extract data included in the Intent
			updateList();
	        //do other stuff here
	    }
	};

	
	// This function will create an intent. This intent must take as parameter the "unique_name" that you registered your activity with
	public static void updateMyActivity(Context context) {

		chatController.getAllMessages(activity, ChatActivity.user, chatAdapter);
		//chatAdapter = new ChatAdapter(context);
		//Intent intent = new Intent("chat");
	    //send broadcast
	    //context.sendBroadcast(intent);
	}
	/*
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
	}
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Manager.setContext(this);
		Bundle extras = getIntent().getExtras();
		int id;
		if (extras != null) {
			id = extras.getInt("ID");
			user = Manager.getMatchProfileById(id);
			if (user == null){
				startFromNotif(id, this);
			}
			else {
				startActivity(this);
			}
		}
	}

	private void startActivity(Context context){
		chatController = user.getChatController();
		chatAdapter = new ChatAdapter(context);
		chatController.getAllMessages(this, user, chatAdapter);
		setListAdapter(chatAdapter);
		this.setTitle(user.getFirstName());

		editText = new EditText(this);
		Drawable drawable = getResources().getDrawable(com.example.voipsitchozr.R.drawable.edit_text_style);
		editText.setTextColor(getResources().getColor(android.R.color.black));
		editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
		editText.setSingleLine();
		editText.setBackgroundDrawable(drawable);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (editText.getText()!= null && editText.getText().toString().length() > 0)
				sendMessage(null);
				return true;
			}
		});

		((LinearLayout)findViewById(R.id.rootChatActivity)).addView(editText);

		//editText = (EditText)findViewById(R.id.textToSend);
	}
	
	private void startFromNotif(final int targetId, final Context context){
		if (Manager.getProfile() != null && Manager.getProfile().getSdkuser() != null){
			OnTaskCompleteListener onPostReadById = new OnTaskCompleteListener() {
				@Override
				public void onCompleteListerner(Object[] result) {
					final SDKUser matchUser = (SDKUser) result[1];
					matchUser.setAge(Integer.toString(AgeCalculator.calculate(matchUser.getBirthday())));
					//Manager.getDatabase().createMatch(matchUser);
					//todo replaced match
					final MatchProfile matchProfile = new MatchProfile(matchUser, AccessToken.getCurrentAccessToken());

					OnTaskCompleteListener onPostReadPictureByUserId = new OnTaskCompleteListener() {
						@Override
						public void onCompleteListerner(Object[] result) {
							List<SDKPicture> pictures = (List<SDKPicture>) result[1];
							for (SDKPicture sdkpicture : pictures) {
								Images image = new Images(sdkpicture.getUrl(), sdkpicture.getId());
								if (sdkpicture.isProfilePicture())
									matchProfile.setProfileImage(image);
								matchProfile.addImagesToArray(image);								//Manager.getDatabase().createPicture(sdkpicture, matchUser.getId());
							}
							Manager.addMatchProfile(matchProfile);
							//Manager.getDatabase().getMatchsAndPictures();
							user = Manager.getMatchProfileById(targetId);
							startActivity(context);
						}
					};
					ImageManager.ApiReadByUserId(onPostReadPictureByUserId, matchUser);
				}
			};
			SDKUser user = new SDKUser(targetId);
			UserManager.ApiReadById(onPostReadById, user);
		}
	}
	
	public void sendMessage(View v) {
		String message = editText.getText().toString();
		SDKMessage sdkmessage = new SDKMessage(message, new Date(), user.getId());
		MessageManager.ApiCreate(null, sdkmessage);
		chatController.createItem(message, new Date(), true);
		editText.setText("");
		updateList();
	}
	
	public static void updateList() {
		chatAdapter.clear();
		chatAdapter.addAll(chatController.getListItem());
		System.out.println("NOTIFY DATA SET CHANGED");
		chatAdapter.notifyDataSetChanged();
	}
}
