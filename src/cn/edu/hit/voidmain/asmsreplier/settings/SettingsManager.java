package cn.edu.hit.voidmain.asmsreplier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.edu.hit.voidmain.asmsreplier.R;

/**
 * Preferences for the application
 * @author voidmain
 *
 */
public class SettingsManager{
	private static Context _currentContext = null;
	private static SettingsManager _instance = null;
	
	private static String key_is_listening = "";
	private static String key_add_postfix_to_message = "";
	private static String key_auto_reply_message = "";
	private static String key_auto_reply_interval = "";
	private static String key_is_first_time_lauch = "";
	
	private static final String PREF_NAME = "cn.edu.hit.voidmain.asmsreplier_preferences";
	
	public static SettingsManager getInstance(Context context)
	{
		if(_instance == null)
		{
			_instance = new SettingsManager(context);
			key_is_listening = context.getResources().getString(R.string.key_is_listening);
			key_add_postfix_to_message = context.getResources().getString(R.string.key_add_postfix_to_message);
			key_auto_reply_message = context.getResources().getString(R.string.key_auto_reply_message);
			key_auto_reply_interval = context.getResources().getString(R.string.key_auto_reply_interval);
			key_is_first_time_lauch = context.getResources().getString(R.string.key_is_first_time_lauch);
		}
		
		return _instance;
	}
	
	private SettingsManager(Context context)
	{
		_currentContext = context;
	}
	
	private Context getCurrentContext()
	{
		return _currentContext;
	}

	public boolean getEnable() {
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		return preference.getBoolean(key_is_listening, false);
	}
	
	public boolean shouldAddPostfix()
	{
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		boolean value = preference.getBoolean(key_add_postfix_to_message, Boolean.valueOf(this.getCurrentContext().getString(R.string.default_should_add_postfix)));
		return value;
	}

	public String getAutoReplyContent() {
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		return preference.getString(key_auto_reply_message, this.getCurrentContext().getString(R.string.default_auto_reply_content));
	}
	
	public int getAutoReplyInterval() {
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String value = preference.getString(key_auto_reply_interval, this.getCurrentContext().getString(R.string.default_auto_reply_interval));
		return Integer.valueOf(value);
	}
	
	public boolean isFirstTimeLaunch()
	{
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String value = preference.getString(key_is_first_time_lauch, this.getCurrentContext().getString(R.string.default_is_frist_time));
		return Boolean.valueOf(value);
	}
	
	public void setFirstTimeLaunch(boolean isFirstTime)
	{
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(key_is_first_time_lauch, "" + isFirstTime);
		editor.commit();
	}
}
