package cn.edu.hit.voidmain.asmsreplier.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.edu.hit.voidmain.asmsreplier.replier.service.SMSReceivedService;
import cn.edu.hit.voidmain.asmsreplier.settings.SettingsManager;

/**
 * starts the service when boot is completed
 * @author voidmain
 *
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SettingsManager manager = SettingsManager.getInstance(context);
        if(!SMSReceivedService.isServiceRunning() && manager.getEnable())
        {
        	context.startService(new Intent(context, SMSReceivedService.class));
        }
	}

}
