package cn.edu.hit.voidmain.asmsreplier.replier.sender;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.contacts.ContactsManager;
import cn.edu.hit.voidmain.asmsreplier.notification.SMSReplierNotificationManager;
import cn.edu.hit.voidmain.asmsreplier.replier.manager.ReplyListManager;
import cn.edu.hit.voidmain.asmsreplier.sms.outgoing.SMSSender;
import cn.edu.hit.voidmain.asmsreplier.util.DateUtil;

public class AutoSenderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ReplyListManager manager = ReplyListManager.getInstance();
		ContactsManager contactManager = new ContactsManager(this);
		String targetAddress = this.getIntent().getStringExtra(ReplyListManager.TARGET_ADDRESS);
		String receiverName = this.getIntent().getStringExtra(ReplyListManager.RECEIVER_NAME);
		if(manager.stillNotProcessed(targetAddress) && contactManager.isContactChecked(targetAddress))
		{
			SMSSender sender = SMSSender.getInstance();
			sender.sendAutoMessage(this, targetAddress);
			SMSReplierNotificationManager notificationManager = new SMSReplierNotificationManager(this);
			notificationManager.makeNotification(this.getResources().getString(R.string.notification_title), 
					String.format(this.getResources().getString(R.string.notification_content_template), 
							receiverName, 
							DateUtil.getDateString(new Date())), 
					0, 
					new Intent()
					);
			// ok, reply processed, can remove it now
			manager.replyProcessed(targetAddress);
			manager.replyChanged();
		}
		
		this.finish();
	}

}
