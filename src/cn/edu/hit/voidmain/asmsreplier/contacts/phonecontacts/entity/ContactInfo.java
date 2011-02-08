package cn.edu.hit.voidmain.asmsreplier.contacts.phonecontacts.entity;

/**
 * ContactInfo
 * entity class for contacts
 * an integer that is the id of the contact
 * a String that stores the contact name
 * a String that stores the contact number
 * a boolean that indicates whether the contact is checked by the user
 * @author voidmain
 *
 */
public class ContactInfo {
	private int contactID;
	private String contactName;
	private String phoneNumber;
	private boolean isChecked;
	
	public ContactInfo()
	{
	}
	
	public int getContactID() {
		return contactID;
	}
	
	public void setContactID(int contactID) {
		this.contactID = contactID;
	}
	
	
	public String getContactName() {
		return contactName;
	}
	
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}
	
	public boolean isChecked()
	{
		return this.isChecked;
	}
	
	public String getNumber() {
		return phoneNumber;
	}
	
	public void setNumber(String number) {
		phoneNumber = number;
	}
}
