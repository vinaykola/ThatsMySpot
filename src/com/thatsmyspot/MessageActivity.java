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
import android.widget.TextView;

public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		TextView messageHistText = (TextView) findViewById(R.id.messageHist);

		setTitle("Messaging with: Social Computing Group");
		try {
			AssetManager am = this.getAssets();
			InputStream is = am.open("meetingHistory.txt");
			Scanner scan = new Scanner(is);
			
			while(scan.hasNext()) {
				messageHistText.append(scan.nextLine()+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Button addButton = (Button) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MessageActivity.this, MainActivity.class);
				startActivity(intent);				
			}
		});
		
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
				Intent intent = new Intent(MessageActivity.this, MainActivity.class);
				intent.putExtra("changeRequest", "Rochelle: No");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
