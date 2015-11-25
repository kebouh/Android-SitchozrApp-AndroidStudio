package notifications;

import datas.Manager;
import interfaces.OnTaskCompleteListener;
import managers.ImageManager;
import managers.MatchManager;
import managers.UserManager;
import sdk.SDKMatch;
import sdk.SDKPicture;
import sdk.SDKUser;
import sources.sitchozt.R;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import activities.ChatActivity;
import activities.MainActivity;
import activities.MatchProfileActivity;
import activities.NavigationActivity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.List;

public class GCMIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);       
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } 
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());          
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendMessageNotification(extras);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /** * Cette méthode permet de créer une notification à partir 
     * d'un message passé en paramètre. 
     */
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.transparent_logo)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setContentText(msg)
        .setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Cette méthode permet à partir des informations envoyées par le serveur 
     *  de notification de créer le message et la notification à afficher sur 
     *  le terminal de l'utilisateur. * 
     *  @param extras les extras envoyés par le serveur de notification 
     */
    private void sendMessageNotification(Bundle extras) {  
        SitchozrNotification notif = extractFromExtra(extras);
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = null;
        PendingIntent contentIntent = null;
        if (notif.getType() == SitchozrNotification.NotificationType.MESSAGE){
        	if (ChatActivity.isActive == true && ChatActivity.activeId == notif.getUserId()){
        		ChatActivity.updateMyActivity(getApplicationContext());
        	}
        	else {
        		contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).putExtra("ID", notif.getUserId()), 0);
        		mBuilder = new NotificationCompat.Builder(this)
            		.setSmallIcon(R.drawable.transparent_logo)
            		.setContentTitle("New Message - Sitchozr")
            		.setStyle(new NotificationCompat.BigTextStyle().bigText(notif.getMessage().getMessage()))
            		.setContentText(notif.getMessage().getMessage())
            		.setAutoCancel(true)
            		.setDefaults(Notification.DEFAULT_SOUND);
        		mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        	}
        }
        else if (notif.getType() == SitchozrNotification.NotificationType.MATCH){    
        	contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).putExtra("ID", notif.getUserId()), 0);
        	mBuilder = new NotificationCompat.Builder(this)
            	.setSmallIcon(R.drawable.transparent_logo)
                .setContentTitle("New Match - Sitchozr")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Congratulations, you have a new match"))
                .setContentText("Congratulations, you have a new match")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);
        	mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    /** 
     * Cette méthode permet d'extraire les informations du message de la notification 
     * afin de créer un message. 
     * @param extras l'objet contenant les informations du message.
     * @return le message 
     */
    private SitchozrNotification extractFromExtra(Bundle extras) {
    	Gson gson = new Gson();
    	String jsonType = extras.getString("type");
    	SitchozrNotification notif = new SitchozrNotification();
    	notif.setUserId(Integer.parseInt(extras.getString("userId").toString()));
    	notif.setNotificationId(Integer.parseInt(extras.getString("notificationId").toString()));
    	SitchozrMessageNotification	message = null;
    	if (jsonType.equals("message")){
    		String jsonData = extras.getString("message");
    		message = gson.fromJson(jsonData, SitchozrMessageNotification.class);
    		notif.setType(SitchozrNotification.NotificationType.MESSAGE);
    	}
    	if (jsonType.equals("match")){
            // TODO récupérer les matchs
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
                                    Manager.getDatabase().getMatchsAndPictures();
                                    Manager.getDatabase().createMatch(matchUser);
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
    		notif.setType(SitchozrNotification.NotificationType.MATCH);
    	}
    	notif.setMessage(message);
    	return (notif);
    }
}
