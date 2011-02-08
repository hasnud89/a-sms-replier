package cn.edu.hit.voidmain.asmsreplier.replier.service;

import java.util.Date;
import java.util.HashMap;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.activities.HomeActivity;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.ContactHelper;
import cn.edu.hit.voidmain.asmsreplier.notification.SMSReplierNotificationManager;
import cn.edu.hit.voidmain.asmsreplier.replier.manager.ReplyListManager;
import cn.edu.hit.voidmain.asmsreplier.replier.sender.AutoSenderActivity;
import cn.edu.hit.voidmain.asmsreplier.settings.SettingsManager;
import cn.edu.hit.voidmain.asmsreplier.sms.incoming.IncomingSMSReceiver;
import cn.edu.hit.voidmain.asmsreplier.sms.outgoing.SMSOutgoingObserver;
import cn.edu.hit.voidmain.asmsreplier.util.DateUtil;

/**
 * SMSReceivedService
 * this is the timer of the application
 * @author voidmain
 *
 */
public class SMSReceivedService extends Service {
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	private static boolean runningService = false;
	private ContactHelper helper;
	
	public static boolean isServiceRunning()
	{
		return runningService;
	}
	
	public static void setServiceRunningState(boolean newState)
	{
		runningService = newState;
	}
	
	private SMSReplierNotificationManager notifier;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		// contact helper
		helper = new ContactHelper(this);
		
		// register sms observer
		getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new SMSOutgoingObserver(this, new Handler()));
		
		final SettingsManager settingsManager = SettingsManager.getInstance(this);
		runningService = settingsManager.getEnable();
		
		IntentFilter filter = new IntentFilter(IncomingSMSReceiver.SMS_RECEIVED);
        BroadcastReceiver receiver = new IncomingSMSReceiver();
        registerReceiver(receiver, filter);
        
        notifier = new SMSReplierNotificationManager(SMSReceivedService.this);
		
		// The service is started now, it needs to start counting
		// start a timer thread here
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
				// Start listening, should inform the user!
				notifier.makeNotification(SMSReceivedService.this.getResources().getString(R.string.start_listening_notification_title), 
						SMSReceivedService.this.getResources().getString(R.string.start_listening_notification_content),
						Notification.FLAG_ONGOING_EVENT, 
						new Intent(SMSReceivedService.this, HomeActivity.class).
						setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
				);
				
				try {
					while(runningService)
					{
						Thread.sleep(1000); // checks every second
						Date currentDate = new Date();
						// Checks if new short message should be send
						ReplyListManager manager = ReplyListManager.getInstance();
						HashMap<String, String> replies = manager.allUnprocessedReply();
						for(String targetAddress : replies.keySet())
						{
							Date targetDate = DateUtil.convertString2Date(replies.get(targetAddress));
							if(targetDate != null && (currentDate.equals(targetDate) || currentDate.after(targetDate)))
							{
								Intent intent = new Intent();
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(SMSReceivedService.this, AutoSenderActivity.class);
								intent.putExtra(ReplyListManager.RECEIVER_NAME, helper.getContactNameByNumber(targetAddress));
								intent.putExtra(ReplyListManager.TARGET_ADDRESS, targetAddress);
								startActivity(intent);
							}
						}
						
						runningService = settingsManager.getEnable();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				finally
				{
					notifier.cancelNotification(SMSReplierNotificationManager.ON_GOING_NOTIFICATION_ID);
				}
			}
		};
		
		t.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
