package cn.edu.hit.voidmain.asmsreplier.provider;


public class ContactInfoProviderMetaData {
	public static final String AUTHORITY = "cn.edu.hit.voidmain.asmsreplier.provider.ContactInfoProvider";
	public static String DATABASE_NAME = "contactRecord.db";
	public static int DATABASE_VERSION = 4;
	public static String CONTACT_INFO_TABLE_NAME = "tbContacts";
	
	private ContactInfoProviderMetaData() {}
	
	/*
	public static void init(Context context)
	{
		DATABASE_NAME = context.getResources().getString(R.string.db_name);
		DATABASE_VERSION = Integer.valueOf(context.getResources().getString(R.string.db_version));
		CONTACT_INFO_TABLE_NAME = context.getResources().getString(R.string.db_table_name_contact_info);
	}
	*/
}
