package Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import activities.MainActivity;
import datas.Manager;
import datas.MatchProfile;
import interfaces.OnTaskCompleteListener;
import managers.LikeManager;
import memory.ImageLoader;
import sdk.SDKLike;
import sources.sitchozt.R;
import sources.sitchozt.VideoActivity;

/**
 * Created by kebouh on 19/12/15.
 */
public class AlertDialogCall {


    public static void showDialog(final Activity activity, final String id) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.call_dialog, (ViewGroup) activity.findViewById(R.id.call_dialog_layout));
        //imageLoader.displayImage(Manager.getMatchProfileById(Integer.valueOf(id)).getProfileImage().getUrl(), view, 2, 0, 0);
        //imageLoader.displayImage(Manager.getProfile().getProfileImage().getUrl(), view, 2, 0, 0);
        URL url = null;
        try {
            MatchProfile match = Manager.getMatchProfileById(Integer.valueOf(id));
            if (match != null) {
                url = new URL(match.getProfileImage().getUrl());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                BitmapDrawable ob = new BitmapDrawable(activity.getResources(), image);
                view.setBackground(ob);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                        final AlertDialog dialog = alert.create();
                        Button yes = (Button) view.findViewById(R.id.yes);
                        Button no = (Button) view.findViewById(R.id.no);
                        alert.setCancelable(true);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Tools.isNetworkAvailable())
                                    Manager.voipManager.getTcpManager().queueSend.add("302 " + id + " " + "yes");
                                //Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("302 " + id + " " + "yes");
                                dialog.cancel();
                                Intent video = new Intent(activity, VideoActivity.class);
                                Manager.serverActions.idPeer = id;
                                    activity.startActivity(video);
                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("302 " + id + " " + "no");
                                dialog.cancel();
                            }
                        });

                        new CountDownTimer(15000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                dialog.cancel();
                            }
                        }.start();

                        dialog.setView(view);

                        dialog.show();
                    }
                });

        }



    public static void showDialogContinue(final Activity activity, final String id) {
        //Manager.voipManager.taskCompleteListener = null;
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.call_dialog, (ViewGroup) activity.findViewById(R.id.call_dialog_layout));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                final AlertDialog dialog = alert.create();
                Button yes = (Button) view.findViewById(R.id.yes);
                yes.setText("Continue");
                Button no = (Button) view.findViewById(R.id.no);
                no.setText("Stop here");
                alert.setCancelable(false);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*if (Tools.isNetworkAvailable()) {
                            SDKLike like = new SDKLike();
                            like.setUserId(Integer.valueOf(id));
                            OnTaskCompleteListener callback = new OnTaskCompleteListener() {
                                @Override
                                public void onCompleteListerner(Object[] result) {
                                    MainActivity.addMatches();
                                }
                            };

                            if (Tools.isNetworkAvailable()) {
                                LikeManager.ApiCreate(callback, like);
                                Manager.giveLike(Integer.valueOf(id));
                                if (Manager.getDiscoveryProfileById(Integer.valueOf(id)) != null)
                                    Manager.deleteDiscovery(Integer.valueOf(id));
                            }
                        }*/
                        //Manager.serverActions.idPeer = null;
                        dialog.dismiss();
                        //activity.finish();
                        Manager.tcpManagerSitchozr.focusOnFragment();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Manager.serverActions.idPeer = null;
                        dialog.dismiss();
                        //activity.finish();
                        //Manager.tcpManagerSitchozr.recoRoulette();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });

    }
    }
