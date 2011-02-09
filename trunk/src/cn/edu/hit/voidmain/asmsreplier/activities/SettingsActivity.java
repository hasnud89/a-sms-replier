package cn.edu.hit.voidmain.asmsreplier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.ListAdapter;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.replier.service.SMSReceivedService;
import cn.edu.hit.voidmain.asmsreplier.settings.SettingsManager;

/**
 * SettingsActivity
 * uses preference framework
 * the view is configured by config_preference.xml in /res/xml folder
 * @author voidmain
 *
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.config_preferences);
		
		CheckBoxPreference preferenceStartListening = null;
		ListAdapter adapter = this.getPreferenceScreen().getRootAdapter();
		for (int idx = 0; idx < adapter.getCount(); idx++) { 
		    Object object = adapter.getItem(idx); 
		    if(object instanceof CheckBoxPreference){ 
		    	if(((CheckBoxPreference)object).getKey().equals(getResources().getString(R.string.key_is_listening)))
		    	{
		    		preferenceStartListening = (CheckBoxPreference)object;
		    	}
		    } 
		} 
		
		if(preferenceStartListening != null)
		{
			preferenceStartListening.setOnPreferenceClickListener(new OnPreferenceClickListener()
			{
	
				@Override
				public boolean onPreferenceClick(Preference pref) {
					if(pref.getKey().equals(
							SettingsActivity.this.getResources().getString(R.string.key_is_listening)))
					{
						SettingsManager manager = SettingsManager.getInstance(SettingsActivity.this);
				        if(!SMSReceivedService.isServiceRunning() && manager.getEnable())
				        {
				        	startService(new Intent(SettingsActivity.this, 
				        			SMSReceivedService.class
				        			)
				        	);
				        }
					}
					return true;
				}
				
			});
		}
	}
}
