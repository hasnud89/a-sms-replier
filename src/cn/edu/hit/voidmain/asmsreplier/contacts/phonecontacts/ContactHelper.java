package cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.entity.ContactInfo;

/**
 * ContactHelper
 * this is a wrapper class that manipulates the contact content provider that is provided by Android
 * @author voidmain
 *
 */
public class ContactHelper {
	private static boolean requestUpdate;
	
	static
	{
		requestUpdate = true;
	}
	
	public static void doRequestUpdate()
	{
		requestUpdate = true;
	}
	
	private Context context;
	
	private static List<ContactInfo> contactInfo = null;

	public ContactHelper(Context context) {
		this.context = context;
	}

	/**
	 * Gets the contact name by number
	 * @param number
	 * @return
	 */
	public String getContactNameByNumber(String number) {
		String targetName = number;
		Uri lookupUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
		Cursor idCursor = context.getContentResolver().query(lookupUri, null,
				null, null, null);
		if (idCursor.moveToFirst()) {
			int nameIdx = idCursor
					.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
			targetName = idCursor.getString(nameIdx);
		}
		idCursor.close();
		return targetName;
	}

	/**
	 * Get contact id by number
	 * @param number
	 * @return
	 */
	public int getContactIdByNumber(String number) {
		String callerID = "-1";
		Uri lookupUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
		Cursor idCursor = context.getContentResolver().query(lookupUri, null,
				null, null, null);
		if (idCursor.moveToFirst()) {
			int idIdx = idCursor
					.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
			callerID = idCursor.getString(idIdx);
		}
		idCursor.close();

		return Integer.valueOf(callerID);
	}
	
	/**
	 * Gets all contact information
	 * because it takes some time to finish this task, 
	 * so only runs when contactInfo is null, which is the first time that user runs this app, 
	 * or when user REQUEST to update by calling the doRequestUpdate method
	 * @return
	 */
	public List<ContactInfo> getContactInfo() {
		if(contactInfo == null || requestUpdate)
		{
			contactInfo = new ArrayList<ContactInfo>();
			Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(contactUri, null,
					null, null,
					ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

			int idColumnIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
			int nameColumnIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);

			if (cursor.moveToFirst()) {
				do {
					String contactId = cursor.getString(idColumnIdx);
					String contactName = cursor.getString(nameColumnIdx);

					int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (phoneCount > 0) {
						Set<String> allPossibleNumbers = new HashSet<String>();
						Cursor phones = context.getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
						
						if (phones.moveToFirst()) {
							do {
								String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								allPossibleNumbers.add(phoneNumber);
							} while (phones.moveToNext());
						}
						
						// TODO consider this situation, 123 and +86123 is actually 
						// the same phone number in China.
						// Need to leave out the +86123 and store 123 only
						// How to do that fast?
						
						for(String number : allPossibleNumbers)
						{
							ContactInfo info = new ContactInfo();
							info.setContactID(Integer.valueOf(contactId));
							info.setContactName(contactName.trim());
							info.setNumber(number.trim());
							contactInfo.add(info);
						}
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			
			requestUpdate = false;
		}

		return contactInfo;
	}
}
