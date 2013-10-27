package com.thatsmyspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);

		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
		Intent intent = getIntent();
		if(intent.hasExtra("time")) {
			String[] time = intent.getStringExtra("time").split(":");
			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minute);

			EditText group = (EditText) findViewById(R.id.groupName);
			group.setEnabled(false);
			TextView groupT = new TextView(this);
			groupT.setText(intent.getStringExtra("groupName"));
			groupT.setLayoutParams(group.getLayoutParams());
		}

		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		String time = hour+":"+minute;
		Log.d("Time", time);
		EditText et = (EditText) findViewById(R.id.groupName);
		String groupName = et.getText().toString();

		Intent i = new Intent(this, ContactsActivity.class);
		if(getIntent().hasExtra("location"))
			intent.putExtra("location", getIntent().getExtras());
		intent.putExtra("time", time);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time, menu);
		return true;
	}
}
