package cn.edu.hit.voidmain.asmsreplier.pd_factory.threads;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ContactData;
import cn.edu.hit.voidmain.asmsreplier.contacts.ContactsManager;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.ContactHelper;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.entity.ContactInfo;
import cn.edu.hit.voidmain.asmsreplier.provider.ContactInfoTableMetaData;

public class RefreshContactThread extends RunningThread {
	private Context context;
	private ContactsManager manager;
	private ContactHelper contactHelper;
	private List<ContactData> contactInfoDatas;

	public RefreshContactThread(Context context,
			List<ContactData> contactInfoData) {
		this.context = context;
		this.contactInfoDatas = contactInfoData;
		manager = new ContactsManager(context);
		contactHelper = new ContactHelper(context);
	}

	@Override
	public void doWork() {
		// TODO The following part is just TOO BAD!!!!
		// Memory runs out easily...
		String[] projection = new String[] {
				ContactInfoTableMetaData.CONTACT_ID,
				ContactInfoTableMetaData.CONTACT_NUMBER, };

		// first, read the list that should be replied from contact content
		// provider
		Cursor shouldReplyCursor = context.getContentResolver().query(
				ContactInfoTableMetaData.CONTENT_URI, projection,
				"shouldReply=1", null, null);

		int idIdx = shouldReplyCursor
				.getColumnIndexOrThrow(ContactInfoTableMetaData.CONTACT_ID);
		int numberIdx = shouldReplyCursor
				.getColumnIndexOrThrow(ContactInfoTableMetaData.CONTACT_NUMBER);

		List<ContactInfo> shouldReplyList = new ArrayList<ContactInfo>();
		if (shouldReplyCursor.moveToFirst()) {
			do {
				ContactInfo contactInfo = new ContactInfo();
				contactInfo.setContactID(shouldReplyCursor.getInt(idIdx));
				contactInfo.setNumber(shouldReplyCursor.getString(numberIdx));
				shouldReplyList.add(contactInfo);
			} while (shouldReplyCursor.moveToNext());
		}

		shouldReplyCursor.close();

		// then, delete all record in the contact provider
		context.getContentResolver().delete(
				ContactInfoTableMetaData.CONTENT_URI, null, null);

		// read current contacts
		ContactHelper.doRequestUpdate();
		List<ContactInfo> currentContacts = contactHelper.getContactInfo();

		// fill into the content provider
		for (ContactInfo id : currentContacts) {
			ContentValues values = new ContentValues();
			values.put(ContactInfoTableMetaData.CONTACT_ID, id.getContactID());
			values.put(ContactInfoTableMetaData.CONTACT_NAME,
					id.getContactName());
			values.put(ContactInfoTableMetaData.CONTACT_NUMBER, id.getNumber());

			context.getContentResolver().insert(
					ContactInfoTableMetaData.CONTENT_URI, values);
		}
		
		currentContacts.clear();

		// update the contact information that should be auto-replied
		for (ContactInfo id : shouldReplyList) {
			ContentValues values = new ContentValues();
			values.put(ContactInfoTableMetaData.SHOULD_REPLY, 1);

			context.getContentResolver().update(
					ContactInfoTableMetaData.CONTENT_URI, values,
					"contactId=? AND contactNumber=?",
					new String[] { "" + id.getContactID(), id.getNumber() });
		}

		// reloads the contact info list
		contactInfoDatas.clear();
		List<ContactInfo> contactInfoData = manager.getAllContactInfo();
		for (ContactInfo contact : contactInfoData) {
			ContactData data = new ContactData(context, contact);
			contactInfoDatas.add(data);
		}
	}

}
