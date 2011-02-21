package cn.edu.hit.voidmain.asmsreplier.sms.incoming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import cn.edu.hit.voidmain.asmsreplier.replier.manager.ReplyListManager;
import cn.edu.hit.voidmain.asmsreplier.settings.SettingsManager;
import cn.edu.hit.voidmain.asmsreplier.util.PhoneNumberUtil;

public class IncomingSMSReceiver extends BroadcastReceiver {
	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	/**
	 * When new short message comes, get the address from the message
	 * Add a new to-reply message in the ReplyListManager if user enables it in the settings manager and the contact is checked
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		SettingsManager manager = SettingsManager.getInstance(context);
		if(manager.getEnable())
		{
			ReplyListManager listManager = ReplyListManager.getInstance();
			if(intent.getAction().equals(SMS_RECEIVED))
			{
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object[] pdus = (Object[]) bundle.get("pdus");
					SmsMessage[] messages = new SmsMessage[pdus.length];
					for (int i = 0; i < pdus.length; i++)
					{
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					}
					
					for(SmsMessage message : messages)
					{
						String originAddress = message.getOriginatingAddress();
						// leave out the +86 prefix
						originAddress = PhoneNumberUtil.leaveOutPrefix(originAddress);
						originAddress = PhoneNumberUtil.removeDashes(originAddress);
						
						listManager.addNewReply(context, originAddress);
						// inform the view to change itself
						listManager.replyChanged();
					}
				}
			}
		}
	}

}
