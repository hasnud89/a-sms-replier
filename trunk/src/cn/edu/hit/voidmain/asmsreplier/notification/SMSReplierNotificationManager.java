package cn.edu.hit.voidmain.asmsreplier.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cn.edu.hit.voidmain.asmsreplier.R;

/**
 * Manages notification stuff
 * @author voidmain
 *
 */
public class SMSReplierNotificationManager {
	// TODO the manipulation of notification id needs to be changed
	private static int NOTIFICATION_ID = 2;
	private static final int MAX_NOTIFICATION_ID = 1000;
	private static final int INIT_NOTIFICATION_ID = 2;
	public static final int ON_GOING_NOTIFICATION_ID = 1;

	private NotificationManager notificationManager = null;
	private Context context;
	
	public SMSReplierNotificationManager(Context context)
	{
		// get the NotificationManager
		notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.context = context;
	}
	
	public void makeNotification(String title, String content, int extraNotificationFlag, Intent intent)
	{
		int notificationId = 0;
		int notificationIcon = 0;
		if((extraNotificationFlag & Notification.FLAG_ONGOING_EVENT) != 0)
		{
			notificationIcon = R.drawable.icon;
			notificationId = ON_GOING_NOTIFICATION_ID;
		}
		else
		{
			notificationIcon = R.drawable.message_sent;
			notificationId = NOTIFICATION_ID;
			if(++NOTIFICATION_ID > MAX_NOTIFICATION_ID)
			{
				NOTIFICATION_ID = INIT_NOTIFICATION_ID;
			}
		}
		
		Notification newNotification = new Notification(notificationIcon, content, System.currentTimeMillis());
		newNotification.flags = newNotification.flags | extraNotificationFlag;
		
		// start the passer activity to cancel the notification
		PendingIntent correspondingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		newNotification.setLatestEventInfo(context, title, content, correspondingIntent);
		
		notificationManager.notify(notificationId, newNotification);
	}
	
	public void cancelNotification(int notificationId)
	{
		notificationManager.cancel(notificationId);
	}
}
