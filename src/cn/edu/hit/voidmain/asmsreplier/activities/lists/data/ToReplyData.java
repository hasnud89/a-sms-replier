package cn.edu.hit.voidmain.asmsreplier.activities.lists.data;

import android.content.Context;
import cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.ContactHelper;

/**
 * ToReplyData
 * a String that is the target address for the message
 * a String that is the respond time
 * a ContactHelper object that is a util object
 * @author voidmain
 *
 */
public class ToReplyData {
	private String targetAddress;
	private String respondTime;
	private ContactHelper helper;
	
	public ToReplyData(Context context, String targetAddress, String time)
	{
		this.targetAddress = targetAddress;
		respondTime = time;
		helper = new ContactHelper(context);
	}
	
	public String getRespondTime()
	{
		return respondTime;
	}
	
	public String getTargetNumber()
	{
		return targetAddress;
	}
	
	public String getTargetName()
	{
		// Call the content provider to get the target name;
		return helper.getContactNameByNumber(targetAddress);
	}
}
