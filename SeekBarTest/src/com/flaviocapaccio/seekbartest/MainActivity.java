package com.flaviocapaccio.seekbartest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
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

import com.flaviocapaccio.seekbartest.SeekBarServiceBinding.LocalBinder;

public class MainActivity extends Activity implements Callback{

	private static final String MIN_VALUE = "minValue";
	private static final String MAX_VALUE = "maxValue";
	private static final String ACTUAL_PROGRESS = "actualProgress";

	TextView progressTv;
	TextView minProgressValueView;
	TextView maxProgressValueView;
	TextView result_view_4_broadcast_service;
	TextView result_view_4_local_service;

	SeekBar seekBar;

	Button setMinButton;
	Button setMaxButton;

	EditText minEditText;
	EditText maxEditText;

	ImageButton leftButton;
	ImageButton rightButton;

	boolean mBound = false;
	
	ResponseReceiver receiver;
	SeekBarServiceBinding mService;
	
	Handler handler = new Handler();

	int minValue = 0;
	int maxValue = 300;
	int shiftProgress = 0;
	int actualProgress = 0;
	final int step = 1;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		result_view_4_broadcast_service = (TextView) findViewById(R.id.result_view_4_broadcast_service);
		result_view_4_local_service = (TextView) findViewById(R.id.result_view_4_local_service);
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
		
		bindService(new Intent(this, SeekBarServiceBinding.class), mConnection, Context.BIND_AUTO_CREATE);

		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ResponseReceiver();
		registerReceiver(receiver, filter);

		//decrease minValue of seekbar according to step value and set progress to minimum value
		leftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setMinValue(getMinValue() - step);
			}
		});

		//increase maxValue of seekbar according to step value and set progress to minimum value
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
			Intent intent = new Intent(getApplicationContext(), SeekBarServiceBroadcast.class);
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				intent.putExtra("Value", (getShiftProgress() + actualProgress));
				startService(intent);

				if (mBound) {
					mService.startThread(getShiftProgress() + actualProgress);
				}
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
				actualProgress = progress;
			}
		});
	}


	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "com.flaviocapaccio.intent.action.MESSAGE_PROCESSED";
		@Override
		public void onReceive(Context context, Intent intent) {
			String text = intent.getStringExtra("Value_return");
			result_view_4_broadcast_service.setText(text);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(MIN_VALUE, minValue);
		outState.putInt(MAX_VALUE, maxValue);
		outState.putInt(ACTUAL_PROGRESS, actualProgress);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		setMinValue(savedInstanceState.getInt(MIN_VALUE));
		setMaxValue(savedInstanceState.getInt(MAX_VALUE));
		seekBar.setProgress((savedInstanceState.getInt(ACTUAL_PROGRESS)));
	}

	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService(MainActivity.this);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};


	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		if(minValue>getMaxValue()){
			Toast toast = Toast.makeText(getApplicationContext(), R.string.indalid_min, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
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

		Intent intent = new Intent(getApplicationContext(), SeekBarServiceBroadcast.class);
		intent.putExtra("Value", (getShiftProgress() + actualProgress));
		startService(intent);
		
		if (mBound) {
			mService.startThread(getShiftProgress() + actualProgress);
		}
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		if(maxValue<getMinValue()){
			Toast toast = Toast.makeText(getApplicationContext(), R.string.indalid_max, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
			return;
		}

		seekBar.setMax(maxValue -  getShiftProgress());
		this.maxValue = maxValue;
		seekBar.setProgress( 0 );
		progressTv.setText("" + (getShiftProgress()) );
		maxProgressValueView.setText("" + getMaxValue());

		Intent intent = new Intent(getApplicationContext(), SeekBarServiceBroadcast.class);
		intent.putExtra("Value", (getShiftProgress() + actualProgress));
		startService(intent);
		
		if (mBound) {
			mService.startThread(getShiftProgress() + actualProgress);
		}
	}

	public int getShiftProgress() {
		return shiftProgress;
	}

	public void setShiftProgress(int shiftProgress) {
		this.shiftProgress = shiftProgress;
	}


	public int getActualProgress() {
		return actualProgress;
	}


	public void setActualProgress(int actualProgress) {
		this.actualProgress = actualProgress;
	}


	@Override
	public void notifySettingCompleted(final String s) {
		
		handler.post(new Runnable() {

			@Override
			public void run() {
				result_view_4_local_service.setText(s);
			}
		});
	}
}