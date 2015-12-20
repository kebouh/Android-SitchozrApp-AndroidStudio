package Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import datas.Manager;
import sources.sitchozt.R;
import sources.sitchozt.VideoActivity;

/**
 * Created by kebouh on 19/12/15.
 */
public class Tools {

    static Thread      connectivityThread = null;

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Manager.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean displayConnectionToast() {
        if (!isNetworkAvailable()) {
            Toast.makeText(Manager.context, "You need to be connected to internet.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public static void         connectivityThreadLoop() {
        //connectivityThread = new Thread(new Runnable() {
            //@Override



                new CountDownTimer(5000, 1000) {
                    //public void run() {
                    AlertDialog.Builder alert;
                    AlertDialog dialog;
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if (!isNetworkAvailable())
                        {
                            if (dialog == null || !dialog.isShowing()){
                            ((Activity) Manager.context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("Run Show");
                                    alert = new AlertDialog.Builder((Manager.context));
                                    dialog= alert.create();
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    TextView text = new TextView(Manager.context);
                                    text.setText("You need to be connected to internet..");
                                    dialog.setView(text);
                                    if (!dialog.isShowing())
                                    dialog.show();
                                }
                            });
                        }
                        }
                        else {
                            if (dialog != null && dialog.isShowing()) {
                                ((Activity) Manager.context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Run dismiss");
                                        dialog.dismiss();
                                        dialog = null;
                                        alert = null;
                                    }
                                });
                            }
                        }
                        this.start();
                    }
                }.start();
            }
       // });
        //connectivityThread.start();
    //}
}
