package notifications;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import activities.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class GCMRegister {
    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    public GCMRegister(Context context){
    	this.context = context;
        if (TextUtils.isEmpty(regId)) {
            // Récupération du registerId du terminal ou enregistrement de ce dernier 
        	regId = registerGCM();
            Log.d("GCM RegId: " + regId, "ok");
        } /*else {
            Toast.makeText(context, "Déjà enregistré sur le GCM Server!", Toast.LENGTH_LONG).show();
        }*/
    }

    /*
     *  Cette méthode récupère le registerId dans les SharedPreferences via
     *  la méthode getRegistrationId(context). *
     *  S'il n'existe pas alors on enregistre le terminal via *
     *  la méthode registerInBackground() */
    public String registerGCM() {
        gcm = GoogleCloudMessaging.getInstance(context);
        regId = getRegistrationId(context);
        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
            Log.d("registerGCM - enregistrement auprès du GCM server OK - regId: " + regId, "ok");
        }/* else {
            Toast.makeText(context, "RegId existe déjà. RegId: " + regId, Toast.LENGTH_LONG).show();
        }*/
        return regId;
    }

    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("ok", "registrationId non trouvé.");
            return "";
        }
       // On peut aussi ajouter un contrôle sur la version de l'application.
        // Lors d'un changement de version d'application le register Id du terminal ne sera plus valide.
        // Ainsi, s'il existe un registerId dans les SharedPreferences, mais que la version
        // de l'application a évolué alors on retourne un registrationId="" forçant ainsi
        // l'application à enregistrer de nouveau le terminal.        
        return registrationId;
    }

    /** * Cette méthode permet l'enregistrement du terminal */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null)
                        gcm = GoogleCloudMessaging.getInstance(context);
                    regId = gcm.register("69936171765");
                    msg = "Terminal enregistré, register ID=" + regId;
                    // On enregistre le registerId dans les SharedPreferences
                    final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(REG_ID, regId);
                    editor.commit();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("Error: " + msg, "ok");
                }
                return msg;
            }
        }.execute(null, null, null);
    }
 }
