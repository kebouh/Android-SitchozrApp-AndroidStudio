package notifications;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import activities.MainActivity;
import interfaces.OnTaskCompleteListener;
import managers.DeviceManager;
import sdk.SDKDevice;

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
        OnTaskCompleteListener onPostDeviceRead = new OnTaskCompleteListener() {
            @Override
            public void onCompleteListerner(Object[] result) {
                Log.e("MainActivity", "onPostDeviceRead...");
                List<SDKDevice> device = (List<SDKDevice>)result[1];
                System.out.println("DEVICE : " + result[0]);
                if (device.size() == 0){
                    regId = registerGCM();
                    System.out.println("CREATE DEVICE : " + regId);
                    if (regId != null)
                        DeviceManager.ApiCreate(null, new SDKDevice(regId));
                } else {
                    // SAUVEGARDER LE DEVICE DANS LE PROFIL
                    System.out.println("DEVICE : " + device.get(0).getToken());
                    regId = device.get(0).getToken();
                }
            }
        };
        DeviceManager.ApiRead(onPostDeviceRead, new SDKDevice(null));
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
            //Log.d("registerGCM - enregistrement auprès du GCM server OK - regId: " + regId, "ok");
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
                    DeviceManager.ApiCreate(null, new SDKDevice(regId));
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
