package cn.edu.hit.voidmain.asmsreplier.sms.outgoing;

import android.content.Context;
import android.telephony.SmsManager;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.settings.SettingsManager;

/**
 * A class that handles how to send messages
 * Implements Singleton pattern here
 * 
 * @author voidmain
 *
 */
public class SMSSender {
	private static SMSSender _instance = null;
	private static SmsManager _smsManager = null;
	
	private SMSSender()
	{
		_smsManager = SmsManager.getDefault();
	}
	
	public static SMSSender getInstance()
	{
		if(_instance == null)
		{
			_instance = new SMSSender();
		}
		return _instance;
	}
	
	/**
	 * 
	 * @param context
	 * used to init the settings manager
	 * @param target
	 * that address to send the message to
	 */
	public void sendAutoMessage(Context context, String target)
	{
		SettingsManager manager = SettingsManager.getInstance(context);
		String messageContent = manager.getAutoReplyContent();
		if(manager.shouldAddPostfix())
		{
			messageContent += context.getString(R.string.auto_reply_postfix);
		}
		_smsManager.sendTextMessage(target, null, messageContent, null, null);
	}
}
