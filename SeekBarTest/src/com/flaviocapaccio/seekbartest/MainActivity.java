package com.flaviocapaccio.seekbartest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView progressTv = null;
	SeekBar seekBar = null;
	int minValue = 0;
	int maxValue = 300;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO elimina uno dei layout. Questo è per un test.
		setContentView(R.layout.layouttmp);

		progressTv = (TextView) findViewById(R.id.tvProgress);

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(maxValue);

		ImageButton leftButton = (ImageButton) findViewById(R.id.leftArrowButton);
		ImageButton rightButton = (ImageButton) findViewById(R.id.rightArrowButton);

		leftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//I can use a step instead of 1
				seekBar.setProgress(seekBar.getProgress() - 1);

			}
		});


		rightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//I can use a step instead of 1
				seekBar.setProgress(seekBar.getProgress() + 1);

			}
		});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progressTv.setText("" + (progress+minValue));



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
