package managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.voipsitchozr.socket.TcpSocket;
import com.voipsitchozr.tcp.TcpActionHandler;
import com.voipsitchozr.tcp.TcpCommand;
import com.voipsitchozr.utils.ConcurrentQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import activities.NavigationActivity;
import datas.Manager;

/**
 * Created by kebouh on 12/01/16.
 */
public class TcpManagerSitchozr {

    private static String ip = "87.98.209.15";
    private static int port = 3038;
    private List rouletteUsers = null;

    private TcpSocket tcpSocket = null;
    public  TcpCommand tcpCommand = null;
    private Context context = null;
    private ProgressDialog progressDialog = null;
    public ConcurrentQueue<String> queueSend;
    ConcurrentQueue<String> queueReceive;



    private List userList = null;

    public TcpManagerSitchozr(Context context) {
        this.context = context;
        rouletteUsers = new ArrayList();

        userList = new ArrayList<>();
        queueReceive = new ConcurrentQueue<>();
        queueSend = new ConcurrentQueue<>();
        tcpCommand = new TcpCommand(queueSend, queueReceive);
    }

    public void threadPerform() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (!queueReceive.isEmpty()) {
                        tcpCommand.getCodeAndPerformAction(queueReceive.poll());
                    }
                }
            }
        }).start();

    }




    public void addUsersActions() {

        tcpCommand.addAction("online", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                ArrayList<String> list = (ArrayList<String>)datas;
                if (userList != null)
                userList.add(list.get(0));
            }
        });

        tcpCommand.addAction("offline", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                ArrayList<String> list = (ArrayList<String>)datas;
                if (userList != null)
                    userList.remove(list.get(0));
            }
        });

        tcpCommand.addAction("users-online", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                List<String> list = (ArrayList<String>)datas;
                if (list != null && list.size() > 0) {
                    userList.clear();
                    Collections.addAll(userList, list.get(0).split(","));
                    System.out.println("roulette " + userList.toString());
                }
            }
        });

        tcpCommand.addAction("join-roulette", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                final ArrayList<String> list = (ArrayList<String>)datas;
                System.out.println("roulette join-roulette received");
                if (list != null && list.size() > 0) {
                    rouletteUsers.add(list.get(0));
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("roulette go call: " + list.get(0) + "-" + list.toString());
                            Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("call " + list.get(0));
                        }
                    });
                }
            }
        });

        tcpCommand.addAction("leave-roulette", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                ArrayList<String> list = (ArrayList<String>) datas;
                System.out.println("roulette leave-roulette received");
                if (list != null && list.size() > 0) {
                    rouletteUsers.remove(list.get(0));
                }
            }
        });

        tcpCommand.addAction("users-roulette", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                List<String> list = (ArrayList<String>) datas;
                System.out.println("roulette users-roulette received");
                if (list != null && list.size() > 0) {
                    rouletteUsers.clear();
                    Collections.addAll(rouletteUsers, list.get(0).split(","));
                    System.out.println("roulette roulette" + rouletteUsers.toString());

                }
            }
        });
    }

    public void focusOnFragment() {

        Manager.serverActions.isRoulette = true;

        progressDialog = Tools.Tools.showProgressDialog(NavigationActivity.contextNav, "", "");
        System.out.println("roulette send join-roulette");
        if (!tcpSocket.channel.isConnected())
            System.out.println("roulette socket not connected");
        Manager.tcpManagerSitchozr.queueSend.add("join-roulette " + Manager.getProfile().getId());

        //Manager.tcpManagerSitchozr.queueSend.add("users-roulette");
    }

    public void unFocusFragment() {

                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Manager.serverActions.isRoulette = false;
                Manager.tcpManagerSitchozr.queueSend.add("leave-roulette " + Manager.getProfile().getId());
    }

    public void recoRoulette() {
        ((Activity) Manager.context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                unFocusFragment();
                Tools.AlertDialogCall.showDialogContinue((Activity)NavigationActivity.contextNav, null);
                //focusOnFragment();
            }
        });
    }
      public void initializeTcpConnection() {
          tcpSocket = new TcpSocket(ip, port, queueSend, queueReceive);
          try {
              tcpSocket.start();
              threadPerform();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
         public void connect() {
            queueSend.add("online " + Manager.getProfile().getId());
         }

    public void disconnect() {
                        //queueSend.add("leave-roulette " + Manager.getProfile().getId());
                        //queueSend.add("offline " + Manager.getProfile().getId());
                        userList.clear();
                        tcpSocket.interrupt();
                    }
                }
