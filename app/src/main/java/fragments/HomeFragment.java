package fragments;


import datas.Manager;
import sources.sitchozt.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Fragment of the home page
 *
 */
public class HomeFragment extends Fragment {


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://api.sitchozr.com");
        } catch (URISyntaxException e) {}
    }

    Emitter.Listener joinVideo = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("JOin video Listener");
            //mSocket.send(args);
        }
    };

    Emitter.Listener leaveVideo = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("leaveVideo Listener");

        }
    };

    Emitter.Listener videoCall = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("video call listener");
        }
    };

    Emitter.Listener video = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("video listener");
            JSONObject data = (JSONObject) args[0];
            String value;
            try {
                value = data.getString("video");
                System.out.println(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(HomeFragment.this.getActivity().getApplicationContext(),
                            "Connect Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Emitter emitter;
            if ((emitter = mSocket.emit("join-video", Integer.valueOf(Manager.getProfile().getId()))) == null)
                System.out.println("video emite join = null");
            else
                System.out.println("video emite join != null");
            mSocket.send(emitter);
            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(HomeFragment.this.getActivity().getApplicationContext(),
                            "Socket connected", Toast.LENGTH_LONG).show();
                    System.out.println("socket is connected");

                }
            });
        }
    };



    public HomeFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home_fragment, container, false);


        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_MESSAGE, video);

        mSocket.on("join-video", joinVideo);
        //mSocket.on("leave-video", leaveVideo);
        //mSocket.on("video-call", videoCall);

        mSocket.on("video", video);
        mSocket.connect();


        return rootView;
    }
}