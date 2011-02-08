package cn.edu.hit.voidmain.asmsreplier.pd_factory.threads;

import java.util.List;

import android.content.Context;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ContactData;
import cn.edu.hit.voidmain.asmsreplier.contacts.ContactsManager;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.entity.ContactInfo;

public class LoadContactThread extends RunningThread {
	private Context context;
	private ContactsManager manager;
	private List<ContactData>contactInfoDatas;
	
	public LoadContactThread(Context context, List<ContactData> contactInfoData)
	{
		this.context = context;
		this.contactInfoDatas = contactInfoData;
	}

	@Override
	public void doWork() {
		manager = new ContactsManager(context);

		updateListViewData();
	}

	public void updateListViewData()
	{
		// this runs ONLY ONCE!!
		manager.updateContactInfo();
		contactInfoDatas.clear();
		List<ContactInfo> contactInfoData = manager.getAllContactInfo();
		for(ContactInfo contact : contactInfoData)
		{
			ContactData data = new ContactData(context, contact);
			contactInfoDatas.add(data);
		}
	}
}
