package cn.edu.hit.voidmain.asmsreplier.activities.lists.data;

import android.content.Context;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.entity.ContactInfo;

/**
 * ContactData for ContactInfoListView
 * It only contains a ContactInfo object
 * @author voidmain
 *
 */
public class ContactData {
	private ContactInfo contactInfo;
	
	public ContactData(Context context, ContactInfo contactInfo)
	{
		this.contactInfo = contactInfo;
	}
	
	public ContactInfo getContactInfo()
	{
		return contactInfo;
	}
}
