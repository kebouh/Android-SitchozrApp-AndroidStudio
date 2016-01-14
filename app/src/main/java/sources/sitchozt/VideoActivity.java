package sources.sitchozt;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.options.SelfViewOptions;
import com.voipsitchozr.utils.TaskCompleteListener;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import Tools.AlertDialogCall;
import activities.MainActivity;
import datas.Manager;
import interfaces.OnTaskCompleteListener;
import managers.LikeManager;
import sdk.SDKLike;

public class VideoActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Manager.context = this;
        if (!Tools.Tools.isNetworkAvailable())
            finish();
        CameraOptions.currentCameraId = CameraOptions.FRONT_CAMERA;
        CameraOptions.compressionQuality = 20;
        CameraOptions.queueLimit = 10;
        CameraOptions.recommendedPreviewSize = false;
        CameraOptions.width = 320;
        CameraOptions.height = 240;
        //CameraOptions.fps = 10;
        ContactViewOptions.x = 0;
        ContactViewOptions.y = 0;

        SelfViewOptions.partOfParent = 4;


        ConnexionOptions.AUDIO_PORT = 3033;
        ConnexionOptions.VIDEO_PORT = 3035;



        ContactViewOptions.width = VoipManager.widthScreen;
        //ContactViewOptions.height = VoipManager.heightScreen;

        if (Manager.voipManager != null) {
            Manager.voipManager.setAudioMode(false);
            Manager.voipManager.setVideoMode(true);
            Manager.voipManager.setChatMode(true);
            Manager.voipManager.setControllerMode(true);
        }
  /*      try {
            if (Manager.serverActions.isRoulette) {
                TaskCompleteListener complete = new TaskCompleteListener() {
                    @Override
                    public void onCompleteListerner(Object[] result) {
                        Tools.AlertDialogCall.showDialogAddMatch(VideoActivity.this, Manager.serverActions.idPeer);
                    }
                };
                Manager.voipManager.initialiaze(this, (FrameLayout) findViewById(R.id.SurfacesLayout), complete);
            }
            else
                Manager.voipManager.initialiaze(this, (FrameLayout) findViewById(R.id.SurfacesLayout), null);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Button  like = new Button(this);
        Button  disLike = new Button(this);

        disLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = Manager.serverActions.idPeer;
                if (id != null && Tools.Tools.isNetworkAvailable()) {
                    if (Manager.getDiscoveryProfileById(Integer.valueOf(id)) != null)
                        Manager.deleteDiscovery(Integer.valueOf(id));
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String id = Manager.serverActions.idPeer;
                if (id != null && Tools.Tools.isNetworkAvailable()) {
                    SDKLike like = new SDKLike();
                    like.setUserId(Integer.valueOf(id));
                    OnTaskCompleteListener callback = new OnTaskCompleteListener() {
                        @Override
                        public void onCompleteListerner(Object[] result) {
                            MainActivity.addMatches();
                        }
                    };

                    if (Tools.Tools.isNetworkAvailable()) {
                        LikeManager.ApiCreate(callback, like);
                        Manager.giveLike(Integer.valueOf(id));
                        if (Manager.getDiscoveryProfileById(Integer.valueOf(id)) != null)
                            Manager.deleteDiscovery(Integer.valueOf(id));
                    }
                }
            }
        });

        if (Manager.serverActions.isRoulette) {
            ArrayList<View> customs = new ArrayList<>();
            disLike.setBackgroundResource(R.drawable.like);
            disLike.setId(66);
            like.setId(22);
            like.setBackgroundResource(R.drawable.delete);
            customs.add(disLike);
            customs.add(like);

            try {
                Manager.voipManager.initialiaze(this, (FrameLayout) findViewById(R.id.SurfacesLayout), customs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                Manager.voipManager.initialiaze(this, (FrameLayout) findViewById(R.id.SurfacesLayout), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConnexionOptions.ID_SELF = String.valueOf(Manager.getProfile().getId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("stop");
        Manager.voipManager.onStop();
    }



    @Override
    public void onPause()
    {
        super.onPause();
        Manager.voipManager.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Manager.voipManager.onStop();
        if (Manager.serverActions.isRoulette)
            Manager.tcpManagerSitchozr.recoRoulette();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //manager.onResume();
    }
}