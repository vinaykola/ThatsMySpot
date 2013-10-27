package com.thatsmyspot;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ContactsActivity extends ListActivity {

	String[] contact_name = new String[100];
	String[] contact_email = new String[100];
	ArrayList<String> items = new ArrayList<String>();
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		
		//if(intent.hasExtra("location"))
		//this contains the time chosen by the user
//			String message = intent.getStringExtra(TimeActivity.EXTRA_MESSAGE);
//			String group = intent.getStringExtra(TimeActivity.EXTRA_GROUP);
//			System.out.println(message+"       "+group);
		//PASS message TO BACKEND !!
		
		//TextView textView = new TextView(this);
	    //textView.setTextSize(40);
	    //textView.setText(message);
	    //setContentView(textView);
		
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		if (cur.getCount() > 0) {
			int i=0;
			while (cur.moveToNext()) {
		    String id = cur.getString( cur.getColumnIndex(ContactsContract.Contacts._ID));
		    String name = cur.getString( cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			contact_name[i] = name;
			//textView.setText(name);
			//setContentView(textView);			
			Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null); 
			
			while (emailCur.moveToNext()) { 
			    String email = emailCur.getString(
		                      emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			    contact_email[i] = email;
			}
			emailCur.close(); 
			i++;
			}

		}
		System.out.println(Arrays.toString(contact_name));
		System.out.println(Arrays.toString(contact_email));
		filterUsers(contact_name, contact_email);
		 
		
	}

	public void filterUsers(String[] contact_name, String[] contact_email)
	{
		String[] c_name = {"Swetha", "Abc"};
		//createList(contact_name, contact_email);
		createList(c_name, contact_email);
	}
	
	public void createList(String[] contact_name, String[] contact_email)
	{
		final ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_multiple_choice, contact_name);
		setListAdapter(adapter);
		Button button = (Button) findViewById(R.id.button1);
		   button.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {

		            ListView thelistview = (ListView) findViewById(android.R.id.list);
		            SparseBooleanArray checkedItems = thelistview.getCheckedItemPositions();
		            
		            if (checkedItems != null) {
		            	for (int i=0; i<checkedItems.size(); i++) {
		            		if (checkedItems.valueAt(i)) {
		            			String item = adapter.getItem(checkedItems.keyAt(i)).toString();
		                        items.add(item);
		            		}
		            	}
		            	Toast.makeText(getBaseContext(), items.toString(), Toast.LENGTH_LONG).show();
		            	
		            }
		            else
		            {
		            	Toast.makeText(getBaseContext(), "checkedItems == null", Toast.LENGTH_LONG).show();
		            }
		            String[] selectedContacts = items.toArray(new String[items.size()]);
		            System.out.println("contacts"+Arrays.toString(selectedContacts));
		            //pass selectedContacts to back end here!!
		            Intent activityChangeIntent = new Intent(ContactsActivity.this, MessageActivity.class);

                    ContactsActivity.this.startActivity(activityChangeIntent);     
		   }
		   });
		   
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
