package cn.edu.hit.voidmain.asmsreplier.replier.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import cn.edu.hit.voidmain.asmsreplier.contacts.ContactsManager;
import cn.edu.hit.voidmain.asmsreplier.replier.interfaces.IReplyChangeObserver;
import cn.edu.hit.voidmain.asmsreplier.replier.interfaces.IReplyChangeSubject;
import cn.edu.hit.voidmain.asmsreplier.settings.SettingsManager;
import cn.edu.hit.voidmain.asmsreplier.util.DateUtil;

/**
 * ReplyListManager
 * implements IReplyChangeSubject
 * @author voidmain
 *
 */
public class ReplyListManager implements IReplyChangeSubject{
	public static final String TIME_INTERVAL = "TIME_INTERVAL";
	public static final String TARGET_ADDRESS = "TARGET_ADDRESS";
	public static final String RECEIVER_NAME = "RECEIVER_NAME";
	
	private static ArrayList<IReplyChangeObserver> observers = null;
	
	// uses the target address as the key
	private static HashMap<String, String> servicesToBeProcessed = null;
	
	private static ReplyListManager _instance = null;
	
	private ContactsManager contactManager = null;
	
	public static ReplyListManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new ReplyListManager();
		}
		return _instance;
	}
	
	private ReplyListManager()
	{
		servicesToBeProcessed = new HashMap<String, String>();
		observers = new ArrayList<IReplyChangeObserver>();
	}
	
	public void addNewReply(Context context, String address)
	{
		// first check if there is an exsiting address in the to be processed queue
		// or, the sender is not checked!
		// if either is true, then DO NOT NEED TO ADD AGAIN
		contactManager = new ContactsManager(context);
		if(!servicesToBeProcessed.keySet().contains(address) && contactManager.isContactChecked(address))
		{
			// add to the list
			Date date = new Date();
			SettingsManager settingsManager = SettingsManager.getInstance(context);
			date.setMinutes(date.getMinutes() + settingsManager.getAutoReplyInterval());
			String time = DateUtil.getDateString(date);
			servicesToBeProcessed.put(address, time);
		}
	}
	
	// here, uses the address as the key
	public void replyProcessed(String address)
	{
		if(servicesToBeProcessed.keySet().contains(address))
		{
			servicesToBeProcessed.remove(address);
		}
	}
	
	// if some incoming SMS has been processed manually in time, then, no need to send automatically!
	public boolean stillNotProcessed(String address)
	{
		return servicesToBeProcessed.keySet().contains(address);
	}
	
	public HashMap<String, String> allUnprocessedReply()
	{
		return servicesToBeProcessed;
	}

	@Override
	public void addReplyObserver(IReplyChangeObserver observer) {
		if(!observers.contains(observer))
		{
			observers.add(observer);
		}
	}

	@Override
	public void removeReplyObserver(IReplyChangeObserver observer) {
		if(observers.contains(observer))
		{
			observers.remove(observer);
		}
	}

	@Override
	public void replyChanged() {
		for(IReplyChangeObserver observer : observers)
		{
			observer.replyChanged();
		}
	}
}
