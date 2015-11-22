package sources.sitchozt;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.CameraOptions;
import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.options.ContactViewOptions;
import com.voipsitchozr.options.SelfViewOptions;

import java.io.IOException;

public class VideoActivity extends Activity {

    VoipManager     manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle extras = getIntent().getExtras();
        int contactId = 0;
        int userId = 0;
        if (extras != null) {
            contactId = extras.getInt("ID_CONTACT");
            userId = extras.getInt("ID_USER");
        }
        else
        System.out.println("EXTRAS == NULL");

        System.out.println("contact: " + contactId);
        System.out.println("contact user: " + userId);

        CameraOptions.currentCameraId = CameraOptions.FRONT_CAMERA;
        CameraOptions.compressionQuality = 80;
        CameraOptions.queueLimit = 10;
        CameraOptions.recommendedPreviewSize = false;
        CameraOptions.width = 640;
        CameraOptions.height = 480;
        CameraOptions.fps = 10;
        ContactViewOptions.x = 0;
        ContactViewOptions.y = 0;
        SelfViewOptions.partOfParent = 4;

        //ConnexionOptions.SERVER_IP = "192.168.1.37";
        ConnexionOptions.SERVER_IP = "87.98.209.15";
        ConnexionOptions.SERVER_PORT = 3031;
        ConnexionOptions.ID_SELF = String.valueOf(userId);
        ConnexionOptions.ID_CONTACT = String.valueOf(contactId);

        manager = new VoipManager(this, (FrameLayout) findViewById(R.id.SurfacesLayout));

        ContactViewOptions.width = VoipManager.widthScreen;
        ContactViewOptions.height = VoipManager.heightScreen;

        //System.out.println(manager.getCamera().getListPreviewSizes().toString());

        try {
            manager.initialiazeViews();
            manager.initializeConnexion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        manager.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        manager.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //manager.onResume();
    }
}