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

	private boolean noevent = false;
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
				noevent = true;
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
		
		if(noevent) {
			Button yesButton = (Button) findViewById(R.id.btnYes);
			yesButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					TextView messageHistText = (TextView) findViewById(R.id.messageHist);
					messageHistText.append("Rochelle: Yes\n");
				}
			});
			Button noButton = (Button) findViewById(R.id.btnNo);
			noButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MessageActivity.this, ChangeActivity.class);
					intent.putExtras(getIntent()); //This adds the groupName
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
