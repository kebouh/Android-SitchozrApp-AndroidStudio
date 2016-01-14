package com.voipsitchozr.tcp;

import com.voipsitchozr.utils.ConcurrentQueue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TcpCommand {

    public Map<String, TcpActionHandler> actions;
    ConcurrentQueue<String> queueSend;
    ConcurrentQueue<String> queueReceive;


    public TcpCommand(ConcurrentQueue<String> queueSend, ConcurrentQueue<String> queueReceive) {
        this.queueSend = queueSend;
        this.queueReceive = queueReceive;
        actions = new HashMap<String, TcpActionHandler>();
    }

    public void addAction(String code, TcpActionHandler handler) {
        this.actions.put(code, handler);
    }

    public void getCodeAndPerformAction(String datas) {
        String code;
        String[] dataArray;

        if (datas != null && datas.startsWith("send")) {
            datas = datas.replaceFirst("send ", "");
            System.out.println("tcp send: " + datas);
            TcpActionHandler action = actions.get("send");
            if (action != null) {
                action.onActionHandler(datas);
            }
        }
        else if (datas != null && datas.startsWith("303")) {
            datas = datas.replaceFirst("303", "");
            System.out.println("tcp send: " + datas);
            TcpActionHandler action = actions.get("303");
            if (action != null) {
                action.onActionHandler(datas);
            }
        } else {
            dataArray = datas.split(" ", -1);
            code = dataArray[0];
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(dataArray));
            System.out.println("tcp getCodeAndperform " + datas);
            if (code != null) {
                list.remove(0);
                TcpActionHandler action = actions.get(code);
                if (action != null) {
                    action.onActionHandler(list);
                }
            }
        }
    }


}
