package com.flaviocapaccio.seekbartest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = new LinearLayout(this);
		MySeekBar mySeekBar = new MySeekBar(this, 0, 300, 1);
		layout.setGravity(Gravity.CENTER);
		layout.addView(mySeekBar);
		setContentView(layout);

		mySeekBar.setOnLeftButtonClicked(new OnLeftButtonClicked() {
			@Override
			public void onLeftButtonClicked() {
				Log.d("flavio", "Sei in onleftclick del main");
			}
		});

		mySeekBar.setOnRightButtonClicked(new OnRightButtonClicked() {

			@Override
			public void onRightButtonClicked() {
				Log.d("flavio", "Sei in onrightclick del main");
			}
		});

		mySeekBar.setOnMySeekBarChangeListener(new OnMySeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d("flavio", "stopped seekbar");

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d("flavio", "started seekbar");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress) {
				Log.d("flavio", "Main activity progress changed: " + progress);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
