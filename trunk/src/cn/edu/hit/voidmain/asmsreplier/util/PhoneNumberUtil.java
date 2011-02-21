package cn.edu.hit.voidmain.asmsreplier.util;

public class PhoneNumberUtil {
	/**
	 * Removes the "+XX" prefix in the phone number
	 * @param oldNumber
	 * @return
	 */
	public static String leaveOutPrefix(String oldNumber)
	{
		String number = oldNumber;
		if(number.startsWith("+"))
		{
			number = number.substring(3);
		}
		
		return number;
	}
	
	public static String removeDashes(String oldNumber)
	{
		return oldNumber.replace("-", "");
	}
}
