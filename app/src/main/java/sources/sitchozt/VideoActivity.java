package sources.sitchozt;

import android.app.Activity;
import android.os.Bundle;

import android.widget.FrameLayout;
import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.options.SelfViewOptions;

import java.io.IOException;
import java.net.SocketException;

import Tools.Tools;
import datas.Manager;

public class VideoActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (!Tools.isNetworkAvailable())
            finish();
        CameraOptions.currentCameraId = CameraOptions.FRONT_CAMERA;
        CameraOptions.compressionQuality = 20;
        CameraOptions.queueLimit = 10;
        CameraOptions.recommendedPreviewSize = false;
        CameraOptions.width = 320;
        CameraOptions.height = 240;
        CameraOptions.fps = 10;
        ContactViewOptions.x = 0;
        ContactViewOptions.y = 0;

        SelfViewOptions.partOfParent = 4;


        ConnexionOptions.AUDIO_PORT = 3033;
        ConnexionOptions.VIDEO_PORT = 3035;



        ContactViewOptions.width = VoipManager.widthScreen;
        ContactViewOptions.height = VoipManager.heightScreen;

        Manager.voipManager.setAudioMode(true);
        Manager.voipManager.setVideoMode(true);
        Manager.voipManager.setChatMode(true);
        Manager.voipManager.setControllerMode(true);

        try {
            Manager.voipManager.initialiaze(this, (FrameLayout) findViewById(R.id.SurfacesLayout));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConnexionOptions.ID_SELF = String.valueOf(Manager.getProfile().getId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("stop");
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
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //manager.onResume();
    }
}