package cn.edu.hit.voidmain.asmsreplier.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.adapter.ToReplyListAdapter;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ToReplyData;
import cn.edu.hit.voidmain.asmsreplier.replier.interfaces.IReplyChangeObserver;
import cn.edu.hit.voidmain.asmsreplier.replier.manager.ReplyListManager;
import cn.edu.hit.voidmain.asmsreplier.util.SortByRespondTime;

/**
 * ToReplyActivity
 * displays the messages that should be replied
 * @author voidmain
 *
 */
public class ToReplyActivity extends Activity implements IReplyChangeObserver {
	public static final int MENU_DELETE = Menu.FIRST + 1;
	
	private TextView tvNoToReply;
	
	private ListView toReplyList;
	private ArrayList<ToReplyData>toReplyDatas;
	private ToReplyListAdapter toReplyDatasAdapter;
	
	private static ReplyListManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_reply);
		
		manager = ReplyListManager.getInstance();
		manager.addReplyObserver(this);
		
		tvNoToReply = (TextView)findViewById(R.id.tv_no_reply);
		toReplyList = (ListView)findViewById(R.id.listview_to_reply);
		toReplyDatas = new ArrayList<ToReplyData>();
		updateListViewData();
		toReplyDatasAdapter = new ToReplyListAdapter(this, toReplyDatas);
		toReplyList.setAdapter(toReplyDatasAdapter);
		
		this.registerForContextMenu(toReplyList);
	}
	
	public void updateListViewData()
	{
		toReplyDatas.clear();
		HashMap<String, String> toReplies = manager.allUnprocessedReply();
		for(String address : toReplies.keySet())
		{
			ToReplyData data = new ToReplyData(this, address, toReplies.get(address));
			toReplyDatas.add(data);
		}
		
		if(toReplyDatas.size() == 0)
		{
			tvNoToReply.setVisibility(View.VISIBLE);
		}
		else
		{
			tvNoToReply.setVisibility(View.GONE);
		}
		
		// order by respond time
		Collections.sort(toReplyDatas, new SortByRespondTime());
		
		toReplyDatasAdapter = new ToReplyListAdapter(this, toReplyDatas);
		toReplyList.setAdapter(toReplyDatasAdapter);
	}
	
	

	@Override
	public void replyChanged() {
		updateListViewData();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, MENU_DELETE, 0, this.getString(R.string.to_reply_delete_confirm));
		menu.setHeaderTitle(this.getString(R.string.to_reply_delete_title));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateListViewData();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch(item.getItemId()){
			case MENU_DELETE:
				int spotPosition = menuInfo.position;
				String targetAddress = toReplyDatas.get(spotPosition).getTargetNumber();
				// TODO: Convert from the target address to the phone number
				// Find a contact using a partial name match
				Uri lookupUri =	Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, targetAddress);
				Cursor idCursor = getContentResolver().query(lookupUri, null, null, null, null);
				String id = null;
				if (idCursor.moveToFirst()) {
					int idIdx = idCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
					id = idCursor.getString(idIdx);
				}
				idCursor.close();
				
				if (id != null) {
					String where = ContactsContract.Data.CONTACT_ID + " = " + id + " AND " + 
						ContactsContract.Data.MIMETYPE + " = '" +
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
						"'";
					Cursor dataCursor =	getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
					// Use the convenience properties to get the index of the columns
					int phoneIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
					if (dataCursor.moveToFirst())
					{
						do 
						{
							// Extract the phone number.
							targetAddress = dataCursor.getString(phoneIdx);
						}
						while(dataCursor.moveToNext());
					}
					dataCursor.close();
				} // else targetAddress remains unchanged
				manager.replyProcessed(targetAddress);
				
				// update the view
				updateListViewData();
				return true;
		}
		return false;
	}
}
