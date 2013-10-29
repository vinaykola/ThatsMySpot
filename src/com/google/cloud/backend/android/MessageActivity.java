package com.google.cloud.backend.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;

public class MessageActivity extends CloudBackendActivity {

	RelativeLayout messageHist;
	List<CloudEntity> posts = new LinkedList<CloudEntity>();
	boolean event = false;
	// TextView messageHistText;
	Spinner spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		messageHist = (RelativeLayout) findViewById(R.id.rlpMsgHist);

		setTitle(getIntent().getStringExtra("groupName"));
		getLocations();

		//		//-----------------------------------------------------------------------------------------------------
		//		//populate this try/catch with the meeting history
		//		/* try {
		//			AssetManager am = this.ggetAssets();
		//			InputStream is = am.open("meetingHistory.txt");
		//			Scanner scan = new Scanner(is);
		//
		//			while(scan.hasNext()) {
		//				//only change the scan.nextLine() here! You may not need the while if you are
		//				//appending bunch of code with \n chars on the end.
		//				messageHistText.append(scan.nextLine()+"\n");
		//			}
		//		 */
		//

		//		if(prev.hasExtra("changedEvent")) {
		//			messageHistText.append(prev.getStringExtra("changeRequest"));
		//		}
		//		String location, time, user;
		//		if(prev.hasExtra("newEvent")) {
		//			//Need to add group to conversation list
		//			location = prev.getStringExtra("location");
		//			//String time = ChangeActivity.convertTime(prev);
		//			time = prev.getStringExtra("time");
		//			user = getUsername() + ": "; 
		//			//messageHistText.append(user+"<Meeting at "+location+" "+time+">\n");
		//		}

		ImageButton addButton = (ImageButton) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MessageActivity.this, ChangeActivity.class);
				intent.putExtra("changeRequest", true);
				Intent prev = getIntent();
				if(prev.hasExtra("newEvent")) {
					intent.putExtras(prev);
				}
				startActivity(intent);	
			}
		});

		// Create the Spinner


		//addItemsOnSpinner1();
		spinner = (Spinner) findViewById(R.id.spinner);
		addListenerOnSpinnerItemSelection();

		ImageButton yesButton = (ImageButton) findViewById(R.id.btnYes);
		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent prev = getIntent();
				if(prev.hasExtra("eventID")) {
					String location = prev.getStringExtra("location");
					String time = prev.getStringExtra("time");
					String user = prev.getStringExtra("user");
					String eventID = prev.getStringExtra("eventID");
					String group = prev.getStringExtra("contacts");
					locationDecision(eventID, user, group, location, time, "yes");
				}
			}
		});
		ImageButton noButton = (ImageButton) findViewById(R.id.btnNo);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent prev = getIntent();
				if(prev.hasExtra("eventID")) {
					String location = prev.getStringExtra("location");
					String time = prev.getStringExtra("time");
					String user = prev.getStringExtra("user");
					String eventID = prev.getStringExtra("eventID");
					String group = prev.getStringExtra("contacts");
					locationDecision(eventID, user, group, location, time, "no");
				}
				//---------------------------------------------------------------------------------------------------------------
				//values of location, time, groupName, and changeRequest
				//Intent intent = new Intent(MessageActivity.this, ChangeActivity.class);
				//					Intent prev = getIntent();
				//Doing the following cause we don't want contacts to be passed, but you may want to 
				//	switch these three ifs to just intent.putExtras(prev) if you do want contacts passed.
				//					String location = "", time = "", groupName = "", user = "", eventId = "";
				//					if(prev.hasExtra("location"))
				//						location = prev.getStringExtra("location"); //This adds the location
				//					if(prev.hasExtra("time"))
				//						time = prev.getStringExtra("time"); //This adds the time
				//					if(prev.hasExtra("groupName"))
				//						groupName = prev.getStringExtra("groupName"); //This adds the groupName
				//					if(prev.hasExtra("user"))
				//						user = prev.getStringExtra("user");
				//					if(prev.hasExtra("eventId"))
				//						eventId = prev.getStringExtra("eventId");
				//					//This adds a string that will be posted after changing the location and/or time
				//					intent.putExtra("changeRequest", "Rochelle: No");
				//					startActivity(intent); */
				//					if(eventId != "")
				//						LocationDisagree(eventId, user, groupName, location, time);
				//				LocationDisagree("vinaykola@gmail.com12:12 pm","vinaykola@gmail.com", "vinaykola@gmail.com;swetha.shivakumar@gmail.com","Van Leer","12:12 pm");

			}
		});

	}



	private String getUsername(){
		AccountManager manager = AccountManager.get(this); 
		Account[] accounts = manager.getAccountsByType("com.google"); 
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			possibleEmails.add(account.name);
		}
		return possibleEmails.get(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	// add items into spinner dynamically
	public void addItemsOnSpinner1() {

		spinner = (Spinner) findViewById(R.id.spinner);
		List<String> list = new ArrayList<String>();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection() {
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			/* Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show(); */
			getLocationDetails(parent.getItemAtPosition(pos).toString());

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			getLocationDetails(parent.getItemAtPosition(0).toString());
		}

	}

	// get the selected dropdown list value
	public void addListenerOnButton() {


	}

	public void getLocations() {

		// create a response handler that will receive the query result or an error
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(List<CloudEntity> results) {
				posts = results;
				final StringBuilder sb = new StringBuilder();
				for (CloudEntity post : posts) {
					sb.append(";");
					sb.append(post.get("location"));
				}
				spinner = (Spinner) findViewById(R.id.spinner);
				List<String> list = new ArrayList<String>();

				for (CloudEntity post : posts) {
					list.add(post.get("location").toString());
				}
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapter);
			}

			@Override
			public void onError(IOException exception) {
				handleEndpointException(exception);
			}
		};

		getCloudBackend().listByProperty("Event", "eventID",F.Op.EQ,"vinaykola@gmail.com12:12 pm",Order.DESC , 1, Scope.FUTURE_AND_PAST, handler);
	}

	private void handleEndpointException(IOException e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		//next.setEnabled(true);
	}

	public void locationDecision(String eventID, String user, String group, String location, String time, String decision) {
		// create a response handler that will receive the query result or an error
		final String deci = decision;
		String status = "";
		String eventID2 = "";

		String d = "";
		if(deci.equals("yes")) {
			d = "1";
		}
		else if(deci.equals("no")) {
			d = "0";
		}
		else {
			d = "2";
		}

		CloudEntity newPost = new CloudEntity("Event");
		eventID2=eventID2.concat(eventID);
		eventID2=eventID2.concat(location);


		for (int i=0;i<group.split(";").length;i++){
			if (group.split(";")[i].equals(user)){
				status=status.concat(";"+d);
			}
		}
		status=status.substring(1,status.length());

		newPost.put("status",status);
		newPost.put("group",group);
		newPost.put("eventID",eventID);
		newPost.put("eventID2",eventID2);
		newPost.put("location",location);
		newPost.put("time",time);

		CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
			@Override
			public void onComplete(final CloudEntity result) {
				getLocationDetails(String.valueOf(spinner.getSelectedItem()));
			}

			@Override
			public void onError(final IOException exception) {
				handleEndpointException(exception);
			}
		};

		// execute the insertion with the handler
		getCloudBackend().update(newPost, handler);

		createTextFields(location, time, status, group);
	}

	public void LocationDisagree(String eventID, String UserEmailID, String group, String location, String time){      
		String status="";
		String eventID2="";

		//		String eventID1="amish1804230";
		//		String UserEmailID1="swetha";
		//		String group1="amish1804;vinaykola;swetha;rochelle.l";
		//		String location1="CoC";
		//		String time1="230";

		CloudEntity newPost = new CloudEntity("Event");

		eventID2=eventID2.concat(eventID);
		eventID2=eventID2.concat(location);

		for (int i=0;i<group.split(";").length;i++){
			if (group.split(";")[i].equals(UserEmailID)){
				status=status.concat(";1");
			}
			else{
				status=status.concat(";0"); 
			}
		}
		status=status.substring(1,status.length());

		newPost.put("status",status);
		newPost.put("group",group);
		newPost.put("eventID",eventID);
		newPost.put("eventID2",eventID2);
		newPost.put("location",location);
		newPost.put("time",time);


		CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
			@Override
			public void onComplete(final CloudEntity result) {
				getLocationDetails(String.valueOf(spinner.getSelectedItem()));
			}

			@Override
			public void onError(final IOException exception) {
				handleEndpointException(exception);
			}
		};

		// execute the insertion with the handler
		getCloudBackend().insert(newPost, handler);
	}


	public void getLocationDetails(String loc) {
		// create a response handler that will receive the query result or an error
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(List<CloudEntity> results) {
				posts = results;
				for (CloudEntity post : posts) {
					String location = post.get("location").toString();
					String time = post.get("time").toString();
					String group = post.get("group").toString();
					String status = post.get("status").toString();
					createTextFields(location, time, status, group);
				}            
			}

			@Override
			public void onError(IOException exception) {
				handleEndpointException(exception);
			}
		};

		getCloudBackend().listByProperty("Event", "eventID2",F.Op.EQ,"vinaykola@gmail.com12:12 pm" +loc,Order.DESC , 1, Scope.FUTURE_AND_PAST, handler);  
	}

	public void createTextFields(String location, String time, String status, String group) {
		RelativeLayout.LayoutParams rlpLoc = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rlpLoc.addRule(RelativeLayout.BELOW, R.id.rlpTAdd);
		TextView loc = new TextView(this);
		loc.setId(5000);
		loc.setTextSize(30);
		loc.setLayoutParams(rlpLoc);
		RelativeLayout.LayoutParams rlpTi = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rlpTi.addRule(RelativeLayout.BELOW, loc.getId());
		TextView ti = new TextView(this);
		ti.setId(5001);
		ti.setTextSize(30);
		ti.setLayoutParams(rlpTi);

		loc.setText(location);
		ti.setText(time);
		messageHist.addView(loc);
		messageHist.addView(ti);
		String[] statuses = status.split(";");
		for(int i=0; i<group.split(";").length; i++) {
			if(i==0) {
				RelativeLayout.LayoutParams rlpStat = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				rlpStat.addRule(RelativeLayout.BELOW, loc.getId()+(i+1));
				TextView stat = new TextView(this);
				stat.setId(ti.getId()+(i+1));
				stat.setLayoutParams(rlpStat);
				stat.setText(group.split(";")[i]);
				if(statuses[i] == "1") {
					Drawable img = this.getResources().getDrawable( R.drawable.green_check_icon );
					img.setBounds(new Rect(0,0,36,36));
					stat.setCompoundDrawables(null, null, img, null);
				}
				else if(statuses[i] == "0") {
					Drawable img = this.getResources().getDrawable( R.drawable.red_x_icon );
					img.setBounds(new Rect(0,0,36,36));
					stat.setCompoundDrawables(null, null, img, null);
				}
				else {
					Drawable img = this.getResources().getDrawable( R.drawable.yellow_question_mark );
					img.setBounds(new Rect(0,0,28,36));
					stat.setCompoundDrawables(null, null, img, null);
				}
				messageHist.addView(stat);
			}
		}
	}
}