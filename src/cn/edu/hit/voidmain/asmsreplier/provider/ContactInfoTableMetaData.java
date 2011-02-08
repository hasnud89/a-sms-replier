package cn.edu.hit.voidmain.asmsreplier.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContactInfoTableMetaData implements BaseColumns
{
	private ContactInfoTableMetaData() {}
	
	public static final String TABLE_NAME = "tbContacts";
	
	// Uri and MIME type definitions
	public static final Uri CONTENT_URI = Uri.parse("content://" + ContactInfoProviderMetaData.AUTHORITY + "/contacts");
	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.asmsreplier.contacts";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.asmsreplier.contacts";
	
	public static final String DEFAULT_SORT_ORDER = "contactName COLLATE LOCALIZED ASC";
	
	// column names
	// string type
	public static final String CONTACT_ID = "contactId";
	// string type
	public static final String CONTACT_NAME = "contactName";
	// string type
	public static final String CONTACT_NUMBER = "contactNumber";
	// integer, 1 for should, 0 for shouldn't
	public static final String SHOULD_REPLY = "shouldReply";
	
	// default values
	// default id for numbers that are not in the contact list
	public static final int NOT_IN_CONTACT_LIST_ID = 0; 
	public static final String NOT_IN_CONTACT_LIST_NAME = "Unknown Contact";
	public static final int DEFAULT_SHOULD_REPLY = 0;
}