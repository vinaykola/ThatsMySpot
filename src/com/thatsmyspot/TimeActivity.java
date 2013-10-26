package com.thatsmyspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TimeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		
		Intent intent = getIntent();
		String location = intent.getExtras().getString("location");
		Log.d("location", location);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.timeAct);
		Button b = new Button(this);
		b.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				//calling time choosing activity and passing location
				Intent intent = new Intent(TimeActivity.this, MessageActivity.class);
				startActivity(intent);
			}
		});
		rl.addView(b);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time, menu);
		return true;
	}

}
