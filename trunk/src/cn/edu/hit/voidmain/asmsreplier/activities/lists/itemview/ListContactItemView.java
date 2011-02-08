package cn.edu.hit.voidmain.asmsreplier.activities.lists.itemview;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ContactData;
import cn.edu.hit.voidmain.asmsreplier.provider.ContactInfoTableMetaData;

public class ListContactItemView extends TableLayout {
	private Context context;
	private TextView tvContactName;
	private TextView tvContactNumber;
	private CheckBox cbIsChecked;
	
	private ContactData data;

	public ListContactItemView(Context context) {
		super(context);
		this.context = context;
		initialize(context);
	}

	private void initialize(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.listview_contact_item_layout, null);
		tvContactName = (TextView)view.findViewById(R.id.tvContactName);
		tvContactNumber = (TextView)view.findViewById(R.id.tvContactNumber);
		cbIsChecked = (CheckBox)view.findViewById(R.id.cbIsChecked);
		
		cbIsChecked.setOnCheckedChangeListener(listener);
		addView(view);
	}
	
	private OnCheckedChangeListener listener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			data.getContactInfo().setChecked(isChecked);
			//ContactLocalHelper helper = new ContactLocalHelper(context);
			//helper.updateContact(data.getContactInfo().getContactID(), data.getContactInfo().getNumber(), );
			ContentValues values = new ContentValues();
			values.put(ContactInfoTableMetaData.SHOULD_REPLY, isChecked ? 1 : 0);
			context.getContentResolver().update(ContactInfoTableMetaData.CONTENT_URI, 
					values, 
					"contactId=? AND contactNumber=?", 
					new String[] {"" + data.getContactInfo().getContactID(), data.getContactInfo().getNumber()});
		}
		
	};
	
	public void updateView()
	{
		tvContactName.setText(data.getContactInfo().getContactName());
		tvContactNumber.setText(data.getContactInfo().getNumber());
		
		cbIsChecked.setChecked(data.getContactInfo().isChecked());
	}
	
	public ContactData getData()
	{
		return data;
	}
	
	public void setData(ContactData data)
	{
		this.data = data;
	}
}
