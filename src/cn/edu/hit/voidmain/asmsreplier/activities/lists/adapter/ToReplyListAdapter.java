package cn.edu.hit.voidmain.asmsreplier.activities.lists.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ToReplyData;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.itemview.ListToReplyItemView;

/**
 * Adapter for ToReplyListView
 * @author voidmain
 *
 */
public class ToReplyListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<ToReplyData> datas;
	
	public ToReplyListAdapter(Context context) {
		super();
		this.context = context;
	}

	public ToReplyListAdapter(Context context, ArrayList<ToReplyData> datas) {
		super();
		this.context = context;
		this.datas = datas;
	}
	
	public void setDatas(ArrayList<ToReplyData> datas)
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
			ListToReplyItemView itemView = new ListToReplyItemView(context);
			itemView.setData(datas.get(position));
			itemView.updateView();
			convertView = itemView;
		}
		else
		{
			((ListToReplyItemView)convertView).setData(datas.get(position));
			((ListToReplyItemView)convertView).updateView();
		}
		
		return convertView;
	}

}
