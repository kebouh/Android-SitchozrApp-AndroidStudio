package Tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import datas.Manager;

/**
 * Created by kebouh on 19/12/15.
 */
public class Tools {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Manager.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean displayConnectionToast() {
        if (!isNetworkAvailable()) {
            Toast.makeText(Manager.context, "You need to be connected to internet.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
