package cn.edu.hit.voidmain.asmsreplier.activities.lists.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ContactData;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.itemview.ListContactItemView;

/**
 * Adapter for ContactListView
 * @author voidmain
 *
 */
public class ContactListAdapter extends BaseAdapter {
	
	private Context context;
	private List<ContactData> datas;
	
	public ContactListAdapter(Context context) {
		super();
		this.context = context;
	}

	public ContactListAdapter(Context context, List<ContactData> contactInfoData) {
		super();
		this.context = context;
		this.datas = contactInfoData;
	}
	
	public void setDatas(ArrayList<ContactData> datas)
	{
		this.datas = datas;
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(datas == null)
		{
			return null;
		}
		
		if(convertView == null)
		{
			ListContactItemView itemView = new ListContactItemView(context);
			itemView.setData(datas.get(position));
			itemView.updateView();
			convertView = itemView;
		}
		else
		{
			((ListContactItemView)convertView).setData(datas.get(position));
			((ListContactItemView)convertView).updateView();
		}
		
		return convertView;
	}

}
