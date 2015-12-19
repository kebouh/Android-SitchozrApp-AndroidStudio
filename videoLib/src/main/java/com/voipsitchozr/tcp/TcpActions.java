package com.voipsitchozr.tcp;

import com.voipsitchozr.chat.ChatView;
import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.utils.ConcurrentQueue;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by kebouh on 18/12/15.
 */
public class TcpActions {

    TcpCommand tcpCommand;
    android.os.Handler mHandler = new android.os.Handler();

    public  TcpActions(TcpCommand tcpCommand) {
        this.tcpCommand = tcpCommand;
    }

    public void addReceiveCodeActions() {

        tcpCommand.addAction("303", new TcpActionHandler() {
            @Override
            public void onActionHandler(final Object datas) {
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        System.err.println("tcpCommand: 303");

                        System.out.println("onActionHandler for code 303");
                        String message = (String) ((ArrayList)datas).get(0);
                        if (message != null && VoipManager.getInstance().chatView != null)
                        VoipManager.getInstance().chatView.addItem(message);
                    }
                });
            }
        });

        tcpCommand.addAction("304", new TcpActionHandler() {
            @Override
            public void onActionHandler(final Object datas) {
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        System.err.println("tcpCommand: 304");
                        System.out.println("onActionHandler for code 304");
                        VoipManager.getInstance().onStop();
                    }
                });
            }
        });


        tcpCommand.addAction("send", new TcpActionHandler() {
            @Override
            public void onActionHandler(final Object datas) {
                        System.err.println("tcpCommand: send");
                        // TODO Auto-generated method stub
                        System.out.println("onActionHandler for code send");
                        tcpCommand.queueSend.add("303 " + (String) datas);
                    }
        });

        tcpCommand.addAction("stop", new TcpActionHandler() {
            @Override
            public void onActionHandler(final Object datas) {
                System.err.println("tcpCommand: send");
                // TODO Auto-generated method stub
                System.out.println("onActionHandler for code send");
                tcpCommand.queueSend.add("304");
            }
        });

    }

}
