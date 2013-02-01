package com.flaviocapaccio.seekbartest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView progressTv;
	TextView minProgressValueView;
	TextView maxProgressValueView;

	SeekBar seekBar;

	Button setMinButton;
	Button setMaxButton;

	EditText minEditText;
	EditText maxEditText;

	ImageButton leftButton;
	ImageButton rightButton;

	int minValue = 0;
	int maxValue = 300;
	int shiftProgress = 0;
	final int step = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		progressTv = (TextView) findViewById(R.id.tvProgress);

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(maxValue);

		minProgressValueView = (TextView) findViewById(R.id.minProgressValueView);
		maxProgressValueView = (TextView) findViewById(R.id.maxProgressValueView);

		setMinButton = (Button) findViewById(R.id.setMinButton);
		setMaxButton = (Button) findViewById(R.id.setMaxButton);
		leftButton = (ImageButton) findViewById(R.id.leftArrowButton);
		rightButton = (ImageButton) findViewById(R.id.rightArrowButton);

		minEditText = (EditText) findViewById(R.id.minValueInput);
		maxEditText = (EditText) findViewById(R.id.maxValueInput);

		
		//decrease minValue of seekbar according to step value
		leftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setMinValue(getMinValue() - step);
			}
		});

		//increase maxValue of seekbar according to step value
		rightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setMaxValue(getMaxValue() + step);
			}
		});


		setMinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					int min = Integer.parseInt(minEditText.getText().toString());
					minEditText.setText("");
					setMinValue(min);
				} catch (NumberFormatException e){
					Toast.makeText(getApplicationContext(), R.string.invalid_input_value, Toast.LENGTH_SHORT).show();
				}
			}
		});

		setMaxButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					int max = Integer.parseInt(maxEditText.getText().toString());
					maxEditText.setText("");
					setMaxValue(max);
				} catch (NumberFormatException e){
					Toast.makeText(getApplicationContext(), R.string.invalid_input_value, Toast.LENGTH_SHORT).show();
				}
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
				progressTv.invalidate();
				progressTv.setText("" + (getShiftProgress() + progress) );
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
//		setShiftProgress(savedInstanceState.getInt("shiftProgress"));
		setMinValue(savedInstanceState.getInt("minValue"));
		setMaxValue(savedInstanceState.getInt("maxValue"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		outState.putInt("shiftProgress", shiftProgress);
		outState.putInt("minValue", minValue);
		outState.putInt("maxValue", maxValue);
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		if(minValue>getMaxValue()){
			Toast.makeText(this, R.string.indalid_min, Toast.LENGTH_LONG).show();
			return;
		}
		int currentShiftProgress = getShiftProgress();
		if(minValue<0){
			if(minValue<getMinValue()){
				int dif = getMinValue() - minValue;
				setShiftProgress(currentShiftProgress + dif);
			} else {
				int dif = minValue - getMinValue();
				setShiftProgress(currentShiftProgress - dif);
			}
		}
 
		if(minValue<getMinValue()){
			int dif = getMinValue() - minValue;
			setShiftProgress(currentShiftProgress - dif);
		} else {
			int dif = minValue - getMinValue();
			setShiftProgress(currentShiftProgress + dif);
		}
		
		this.minValue = minValue;
		seekBar.setMax(getMaxValue() - minValue);
		seekBar.setProgress( 0 );
		progressTv.setText("" + (getShiftProgress()) );
		minProgressValueView.setText("" + getMinValue());
		minProgressValueView.setText("" + minValue);
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		if(maxValue<getMinValue()){
			Toast.makeText(this, R.string.indalid_max, Toast.LENGTH_LONG).show();
			return;
		}
		
		seekBar.setMax(maxValue -  getShiftProgress());
		this.maxValue = maxValue;
		seekBar.setProgress( 0 );
		progressTv.setText("" + (getShiftProgress()) );
		maxProgressValueView.setText("" + getMaxValue());
		
//		maxProgressValueView.setText("" + max);
	}

	public int getShiftProgress() {
		return shiftProgress;
	}

	public void setShiftProgress(int shiftProgress) {
		this.shiftProgress = shiftProgress;
	}
}