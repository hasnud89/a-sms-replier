package cn.edu.hit.voidmain.asmsreplier.contacts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.ContactHelper;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.entity.ContactInfo;
import cn.edu.hit.voidmain.asmsreplier.provider.ContactInfoTableMetaData;

/**
 * ContactsManager
 * this manager manipulates the content provider
 * it also uses ContactHelper as a util
 * ContactHelper is a wrapper class for contacts in the phone
 * @author voidmain
 *
 */
public class ContactsManager {
	private Context context;
	private ContactHelper contactHelper;
	
	public ContactsManager(Context context)
	{
		this.context = context;
		contactHelper = new ContactHelper(context);
	}
	
	public boolean isContactChecked(String number)
	{
		int contactID = contactHelper.getContactIdByNumber(number);
		return isContactChecked(contactID, number);
	}
	
	/**
	 * Checks if the contact is checked by user, which means we should auto-reply to that
	 * @param contactID
	 * @param number
	 * @return
	 */
	public boolean isContactChecked(int contactID, String number)
	{
		boolean shouldReply = false;
		String[] projection = new String[]{
				ContactInfoTableMetaData.SHOULD_REPLY 
		};
		Cursor cur = context.getContentResolver().query(ContactInfoTableMetaData.CONTENT_URI, 
				projection, 
				// this is a bug by Android
				// we can not use '%?%' in 'like' statement
				// I have to put number in the where statement
				"contactId=? AND contactNumber like '%" + number + "'", 
				new String[] {"" + contactID}, 
				null);
		
		int replyIdx = cur.getColumnIndexOrThrow(ContactInfoTableMetaData.SHOULD_REPLY);
		
		if(cur.moveToFirst())
		{
			do
			{
				shouldReply = Integer.valueOf(cur.getInt(replyIdx)) != 0;
			}
			while(cur.moveToNext());
		}
		
		cur.close();
		
		return shouldReply;
	}
	
	public List<ContactInfo> getAllContactInfo()
	{
		/*
		List<ContactInfo> contacts = contactHelper.getContactInfo();
		dbHelper.fillCheck(contacts);
		return contacts;
		*/
		String[] projection = new String[]{
				ContactInfoTableMetaData.CONTACT_ID, 
				ContactInfoTableMetaData.CONTACT_NAME, 
				ContactInfoTableMetaData.CONTACT_NUMBER, 
				ContactInfoTableMetaData.SHOULD_REPLY 
		};
		
		List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		Cursor cur = context.getContentResolver().query(ContactInfoTableMetaData.CONTENT_URI, projection, null, null, null);
		
		// idx
		int idIdx = cur.getColumnIndexOrThrow(ContactInfoTableMetaData.CONTACT_ID);
		int nameIdx = cur.getColumnIndexOrThrow(ContactInfoTableMetaData.CONTACT_NAME);
		int numberIdx = cur.getColumnIndexOrThrow(ContactInfoTableMetaData.CONTACT_NUMBER);
		int replyIdx = cur.getColumnIndexOrThrow(ContactInfoTableMetaData.SHOULD_REPLY);
		
		if(cur.moveToFirst())
		{
			do
			{
				ContactInfo info = new ContactInfo();
				info.setContactID(cur.getInt(idIdx));
				info.setContactName(cur.getString(nameIdx));
				info.setNumber(cur.getString(numberIdx));
				info.setChecked(Integer.valueOf(cur.getInt(replyIdx)) != 0);
				contacts.add(info);
			}
			while(cur.moveToNext());
		}
		
		cur.close();
		
		return contacts;
	}
	
	public void updateContactInfo()
	{
		// Only loads the contact information for the first time
		// after that, the contact information is updated by ContactObserver
		String[] projection = new String[]{
				ContactInfoTableMetaData.CONTACT_ID, 
		};
		
		Cursor cur = context.getContentResolver().query(ContactInfoTableMetaData.CONTENT_URI, projection, null, null, null);
		if(cur.getCount() == 0)
		{
			List<ContactInfo> contactList = contactHelper.getContactInfo();
			for(ContactInfo id : contactList)
			{
				ContentValues values = new ContentValues();
				values.put(ContactInfoTableMetaData.CONTACT_ID, id.getContactID());
				values.put(ContactInfoTableMetaData.CONTACT_NAME, id.getContactName());
				values.put(ContactInfoTableMetaData.CONTACT_NUMBER, id.getNumber());
				
				context.getContentResolver().insert(ContactInfoTableMetaData.CONTENT_URI, values);
			}
		}
		
		cur.close();
	}
}
