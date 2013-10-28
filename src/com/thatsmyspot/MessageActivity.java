package com.thatsmyspot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MessageActivity extends Activity {

	private boolean event = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		TextView messageHistText = (TextView) findViewById(R.id.messageHist);

		setTitle(getIntent().getStringExtra("groupName"));
//-----------------------------------------------------------------------------------------------------
		//populate this try/catch with the meeting history
		try {
			AssetManager am = this.getAssets();
			InputStream is = am.open("meetingHistory.txt");
			Scanner scan = new Scanner(is);
			
			while(scan.hasNext()) {
				//only change the scan.nextLine() here! You may not need the while if you are
				//appending bunch of code with \n chars on the end.
				messageHistText.append(scan.nextLine()+"\n");
			}
			
			Intent prev = getIntent();
			if(prev.hasExtra("changedEvent")) {
				messageHistText.append(prev.getStringExtra("changeRequest"));
				event = true;
			}
			if(prev.hasExtra("newEvent")) {
				//Need to add group to conversation list
				String location = prev.getStringExtra("location");
				String time = ChangeActivity.convertTime(prev);
				String user = "Rochelle" + ": "; //Need to find a way to retrieve this, using username or actual names
				messageHistText.append(user+"<Meeting at "+location+" "+time+">\n");
				event = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageButton addButton = (ImageButton) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MessageActivity.this, LocationActivity.class);
				intent.putExtras(getIntent()); //This passes the groupName only to the next activity
				startActivity(intent);	
			}
		});
		
		if(event) {
			Button yesButton = (Button) findViewById(R.id.btnYes);
			yesButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//--------------------------------------------------------------------------------------------------------------
					//need to record in messageHistory
					TextView messageHistText = (TextView) findViewById(R.id.messageHist);
					messageHistText.append("Rochelle: Yes\n");
				}
			});
			Button noButton = (Button) findViewById(R.id.btnNo);
			noButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//---------------------------------------------------------------------------------------------------------------
					//values of location, time, groupName, and changeRequest
					Intent intent = new Intent(MessageActivity.this, ChangeActivity.class);
					Intent prev = getIntent();
					//Doing the following cause we don't want contacts to be passed, but you may want to 
					//	switch these three ifs to just intent.putExtras(prev) if you do want contacts passed.
					if(intent.hasExtra("location"))
						intent.putExtra("location", prev.getStringExtra("location")); //This adds the location
					if(intent.hasExtra("time"))
						intent.putExtra("time", prev.getStringExtra("time")); //This adds the time
					if(intent.hasExtra("groupName"))
						intent.putExtra("groupName", prev.getStringExtra("groupName")); //This adds the groupName
					//This adds a string that will be posted after changing the location and/or time
					intent.putExtra("changeRequest", "Rochelle: No");
					startActivity(intent);
				}
			});
		}
		else {
			Button yesButton = (Button) findViewById(R.id.btnYes);
			yesButton.setEnabled(false);
			Button noButton = (Button) findViewById(R.id.btnNo);
			noButton.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
