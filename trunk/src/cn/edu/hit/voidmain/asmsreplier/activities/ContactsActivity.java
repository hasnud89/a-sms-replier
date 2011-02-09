package cn.edu.hit.voidmain.asmsreplier.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.adapter.ContactListAdapter;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ContactData;
import cn.edu.hit.voidmain.asmsreplier.pd_factory.PDFactory;
import cn.edu.hit.voidmain.asmsreplier.pd_factory.threads.LoadContactThread;
import cn.edu.hit.voidmain.asmsreplier.pd_factory.threads.RefreshContactThread;
import cn.edu.hit.voidmain.asmsreplier.pd_factory.threads.RunningThread;

/**
 * ContactsActivity
 * Lists the contacts in the phone.
 * This activity only displays the data in the list view
 * As for the data, it is provided by the LoadContactThread
 * A ProgressDialog is popped up when it loads contact
 * @author voidmain
 *
 */

public class ContactsActivity extends Activity {	
	// a list view, an array that holds data, and an adapter
	// these components builds the list view
	private ListView contactListView;
	private static List<ContactData>contactInfoData;
	private ContactListAdapter contactDataAdapter;
	
	// pops up a progress dialog
	private ProgressDialog progressDialog;
	
	// inits the contactInfoData array list
	static 
	{
		contactInfoData = new ArrayList<ContactData>();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		
		contactListView = (ListView)findViewById(R.id.listview_contacts);
		
		// loads the contact information
		updateDataRoutine(new LoadContactThread(this, contactInfoData));
	}
	
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			contactDataAdapter = new ContactListAdapter(ContactsActivity.this, contactInfoData);
			contactListView.setAdapter(contactDataAdapter);
			
			if(progressDialog != null)
			{
				progressDialog.cancel();
			}
		}
		
	};

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * this is a bad idea, i need to add a "refresh" button here.
		 * only update the information when user wants to do that
		 * */
		//updateListViewData();
		//contactDatasAdapter = new ContactListAdapter(this, contactInfoDatas);
		//contactList.setAdapter(contactDatasAdapter);
	}
	
	// Add options menu support
	final int base = Menu.FIRST;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.clear();
		
		menu.add(Menu.NONE, base, 1, this.getResources().getString(R.string.menu_refresh)).setIcon(android.R.drawable.ic_menu_revert);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case base:
				updateDataRoutine(new RefreshContactThread(this, contactInfoData));
				return true;
		}
		return false;
	}
	
	private void updateDataRoutine(RunningThread thread)
	{
		thread.setHandler(handler);
		progressDialog = PDFactory.createProgressDialog(this, 
				R.string.progress_load_contact_title, 
				R.string.progress_load_contact_message, 
				R.drawable.contacts, 
				thread);
		progressDialog.show();
	}
}
