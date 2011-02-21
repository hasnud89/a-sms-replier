package cn.edu.hit.voidmain.asmsreplier.sms.outgoing;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import cn.edu.hit.voidmain.asmsreplier.replier.manager.ReplyListManager;
import cn.edu.hit.voidmain.asmsreplier.util.PhoneNumberUtil;

/**
 * Observes the message content provider
 * When user replies to a message, remove the corresponding item in the to-reply list
 * @author voidmain
 *
 */
public class SMSOutgoingObserver extends ContentObserver {
	private Context context;
	private Cursor cursor;
	private ReplyListManager manager;

	public SMSOutgoingObserver(Context context, Handler handler) {
		super(handler);
		this.context = context;
		manager = ReplyListManager.getInstance();
	}

	/**
	 * Gets the latest message which is the latest out-message
	 */
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		cursor = context.getContentResolver().query(
				Uri.parse("content://sms/"),
				new String[] { "_id", "address", "protocol" }, null, null,
				"_id desc");
		if (cursor != null) {
			cursor.moveToFirst();
			int protocolIdx = cursor.getColumnIndexOrThrow("protocol");

			String protocol = cursor.getString(protocolIdx);
			if (protocol == null) {
				int numberIdx = cursor.getColumnIndexOrThrow("address");
				// remove the + prefix
				String number = cursor.getString(numberIdx);
				number = PhoneNumberUtil.leaveOutPrefix(number);
				number = PhoneNumberUtil.removeDashes(number);
				manager.replyProcessed(number);
			}
		}
	}
}
