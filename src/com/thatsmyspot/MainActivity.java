package com.thatsmyspot;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.rlayout);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		int width = rl.getWidth();
		
		TextView tv = new TextView(this);
		tv.setText(R.string.welcome);
		tv.setId(1);
		rl.addView(tv);

		try {
			File f = new File("buildings.txt");
			PrintStream output = new PrintStream(f);
			output.println("here");
			output.close();
			Scanner scan = new Scanner(f);
			
			int i=0;
			while(scan.hasNext()) {
				Button b = new Button(this);
				String str = scan.next();
				b.setText(str);
				b.setWidth(width);
				if(i>0) {
					lp.addRule(RelativeLayout.BELOW, i);
					rl.addView(b);
				}
				else {
					lp.addRule(RelativeLayout.BELOW, tv.getId());
					rl.addView(b);
				}
				i++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
