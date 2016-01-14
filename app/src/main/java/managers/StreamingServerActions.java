package managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.tcp.TcpActionHandler;
import com.voipsitchozr.tcp.TcpCommand;
import com.voipsitchozr.tcp.TcpManager;

import java.util.ArrayList;

import Tools.AlertDialogCall;
import Tools.Tools;
import datas.Manager;
import fragments.HomeFragment;
import sources.sitchozt.VideoActivity;
import android.os.CountDownTimer;


/**
 * Created by kebouh on 18/12/15.
 */
public class StreamingServerActions {

    ProgressDialog callDialog = null;
    Context     context = null;
    Boolean isWaiting = false;
    public boolean isRoulette = false;
    public String idPeer = null;

    public StreamingServerActions(Context context) {
        this.context = context;
    }

    public void addActions() {
        final TcpManager tcpManager = Manager.voipManager.getTcpManager();
        final TcpCommand tcpCommand = tcpManager.getTcpCommand();

        tcpCommand.addAction("connect", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                Integer id = Integer.valueOf((String)(((ArrayList)datas).get(0)));
                if (id != null) {
                    tcpManager.queueSend.add("300 " + id);
                }
            }
        });

        tcpCommand.addAction("call", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                if (datas == null)
                    return;
                String name = "";
                if (((ArrayList)datas).size() >= 2 && ((ArrayList)datas).get(1) != null)
                   name = (String) ((ArrayList)datas).get(1);
              if (!isRoulette) {
                    callDialog = Tools.showProgressDialog(Manager.context, "Calling " + name, "Calling... ");
                    isWaiting = true;
                  final CountDownTimer timer = new CountDownTimer(20000, 1000) {
                      public void onTick(long millisUntilFinished) {
                          if (callDialog != null && callDialog.isShowing())
                              callDialog.setMessage(String.valueOf(millisUntilFinished / 1000));
                      }
                      public void onFinish() {
                          isWaiting = false;
                          if (callDialog.isShowing())
                              callDialog.dismiss();
                          callDialog = null;
                      }
                  };
                  callDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                      @Override
                      public void onDismiss(DialogInterface dialog) {
                        if (timer != null)
                          timer.cancel();
                      }
                  });
                    timer.start();
                }
                if (((ArrayList)datas).get(0) != null) {
                    isWaiting = true;
                    tcpManager.queueSend.add("301 " + ((ArrayList)datas).get(0));
                }
            }
        });

        tcpCommand.addAction("302", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                ArrayList<String> list = (ArrayList<String>)datas;
                if (list != null && list.size() == 1 && isWaiting == true) {
                    isWaiting = false;
                    if (callDialog != null)
                        callDialog.dismiss();
                    if (list.get(0).contains("yes")) {
                        Intent video = new Intent(context, VideoActivity.class);
                        context.startActivity(video);
                    }
                }
            }
        });

        tcpCommand.addAction("finish", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                tcpManager.queueSend.add("304");
            }
        });

        tcpCommand.addAction("301", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                final String id = (String)((ArrayList)datas).get(0);
                if (!Manager.voipManager.isInCall && !isWaiting) {
                    if (!isRoulette)
                        AlertDialogCall.showDialog((Activity) Manager.context, id);
                    else if (!Manager.voipManager.isInCall){
                        Manager.voipManager.getTcpManager().queueSend.add("302 " + id + " " + "yes");
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Manager.voipManager.isInCall = true;
                                Intent video = new Intent(context, VideoActivity.class);
                                idPeer = id;
                                context.startActivity(video);
                            }
                        });

                    }
                }
                else {
                    Manager.voipManager.getTcpManager().queueSend.add("302 " + id + " " + "no");
                    idPeer = null;
                }
            }
        });

    }
}
