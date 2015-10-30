package fragments;

import datas.Manager;
import sources.sitchozt.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * This Fragment represents the general settings of the application
 */
public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager.setContext(this.getActivity());
        // Load the preferences from an XML resource
        this.getActivity().setTheme(android.R.style.Theme_DeviceDefault);

        addPreferencesFromResource(R.xml.preference_app);

    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
	}
}
