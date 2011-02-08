package cn.edu.hit.voidmain.asmsreplier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ToReplyData;

public class SortByRespondTime implements Comparator<ToReplyData> {

	/**
	 * Used to sort the list.
	 * Sort the list by respond time desc
	 */
	@Override
	public int compare(ToReplyData first, ToReplyData second) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = sdf.parse(first.getRespondTime());
			secondDate = sdf.parse(second.getRespondTime());
		} catch (ParseException e) {
			return 0;
		}
		return secondDate.compareTo(firstDate);
	}
	
}
