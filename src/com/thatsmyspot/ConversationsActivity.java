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
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConversationsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversations);

		try {
			AssetManager as = this.getAssets();
			InputStream is = as.open("conversations.txt");
			Scanner scan = new Scanner(is);
			//accessing relative layout defined in xml
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.conversationLayout);
			
			ImageButton button = (ImageButton) findViewById(R.id.btnAdd);
			button.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ConversationsActivity.this, LocationActivity.class);
					//There is no groupName here and therefore nothing is sent!
					startActivity(intent);
				}
			});

			int i=0;
			while(scan.hasNext()) {
				//creating buttons dynamically
				TextView t = new TextView(this);
				final String groupName = scan.nextLine();
				t.setText(groupName);
				t.setBackgroundResource(R.drawable.back);
				t.setClickable(true);
				t.setId(4000 + i);
				t.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						//calling time choosing activity and passing location
						Intent intent = new Intent(ConversationsActivity.this, MessageActivity.class);
						intent.putExtra("groupName", groupName); //adds groupName to be passed through
						startActivity(intent);
					}
				});

				if (i == 0) {
					RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//setting first button below text view
					rlpB.addRule(RelativeLayout.BELOW, R.id.rlpCAdd);
					t.setLayoutParams(rlpB);
					rl.addView(t);
				} else {
					RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					//setting buttons to come one after another
					rlpB.addRule(RelativeLayout.BELOW, t.getId() - 1);
					t.setLayoutParams(rlpB);
					rl.addView(t);
				}
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversations, menu);
		return true;
	}

}
