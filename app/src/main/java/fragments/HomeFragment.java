package fragments;


import datas.Manager;
import managers.TcpManagerSitchozr;
import sources.sitchozt.R;
import sources.sitchozt.VideoActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.tcp.TcpActionHandler;
import com.voipsitchozr.tcp.TcpCommand;
import com.voipsitchozr.tcp.TcpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment of the home page
 *
 */
public class HomeFragment extends Fragment {

    private View rootView = null;
    private TcpCommand  tcpCommand = null;
    private List rouletteUsers = null;
    static TextView        text = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_home_fragment, container, false);
        text = (TextView) rootView.findViewById(R.id.txtLabel);
        //text.setText("hello");
        rouletteUsers = new ArrayList();
        tcpCommand = Manager.tcpManagerSitchozr.tcpCommand;
        
        return rootView;
    }

    public static void apendText(String str) {
        text.setText(text.getText().toString() + "\n" + str);
    }
}