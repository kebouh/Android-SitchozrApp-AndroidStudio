package managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.tcp.TcpActionHandler;
import com.voipsitchozr.tcp.TcpCommand;
import com.voipsitchozr.tcp.TcpManager;

import java.util.ArrayList;

import Tools.AlertDialogCall;
import datas.Manager;
import sources.sitchozt.VideoActivity;

/**
 * Created by kebouh on 18/12/15.
 */
public class StreamingServerActions {

    Context     context = null;
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

                Integer id = Integer.valueOf((String)(((ArrayList)datas).get(0)));
                System.err.println("tcp Command: call " + id);
                if (id != null) {
                    tcpManager.queueSend.add("301 " + id);
                }
            }
        });

        tcpCommand.addAction("302", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                ArrayList<String> list = (ArrayList<String>)datas;
                System.err.println("tcpCommand: answer");
                System.out.println("tcp arrayList: " + datas + " " + list.size());
                if (list != null && list.size() == 1) {
                    //tcpManager.queueSend.add("302 " + list.get(0) + " " + list.get(1));
                    if (list.get(0).contains("yes")) {
                        System.out.println("tcp Start video activity !");
                        Intent video = new Intent(context, VideoActivity.class);
                        context.startActivity(video);
                    }
                    else {
                        System.out.println("tcp no yes");
                    }
                }
                else
                    System.err.println("tcpCommand: answer action Error in arrayList");
            }
        });


        tcpCommand.addAction("answer", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                ArrayList<String> list = (ArrayList<String>)datas;
                System.err.println("tcpCommand: answer");
                System.out.println("tcp arrayList: " + datas + " " + list.size());
                if (list != null && list.size() == 1) {
                    tcpManager.queueSend.add("302 " + list.get(0) + " " + list.get(1));
                    if (list.get(0).contains("yes")) {
                        System.out.println("tcp ANSWER YES !");
                        //Intent video = new Intent(context, VideoActivity.class);
                        //context.startActivity(video);
                    }
                    else {
                        System.out.println("tcp no yes");
                    }
                }
                else
                    System.err.println("tcp Command: answer action Error in arrayList");
            }
        });


        tcpCommand.addAction("finish", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                System.err.println("tcpCommand: finish");

                tcpManager.queueSend.add("304");
            }
        });

        tcpCommand.addAction("301", new TcpActionHandler() {
            @Override
            public void onActionHandler(Object datas) {
                final String id = (String)((ArrayList)datas).get(0);

                System.out.println("tcpCommand: received call from" + id);
                //tcpManager.queueSend.add("302 " + id + " " + "yes");

                        AlertDialogCall.showDialog((Activity) Manager.context, id);

               // tcpCommand.getCodeAndPerformAction();
            }
        });

    }

}
