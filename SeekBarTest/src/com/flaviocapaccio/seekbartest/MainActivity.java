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
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviocapaccio.seekbartest.MySeekBar.OnLeftButtonClicked;
import com.flaviocapaccio.seekbartest.MySeekBar.OnMySeekBarChangeListener;
import com.flaviocapaccio.seekbartest.MySeekBar.OnRightButtonClicked;
import com.flaviocapaccio.seekbartest.SeekBarServiceBinding.ICallback;
import com.flaviocapaccio.seekbartest.SeekBarServiceBinding.LocalBinder;

public class MainActivity extends Activity{

	static final int MSG_PROGRESS = 1;
	public static final int MSG_PROGRESS_EVALUATED = 2;
	private static final String STEP = "step";
	private static final String RANGE_MIN = "range_min";
	private static final String RANGE_MAX = "range_max";
	private String TAG = "seekbartest";

	TextView result_view_4_broadcast_service;
	TextView result_view_4_local_service;
	TextView result_view_4_messenger_service;
	TextView result_view_4_aidl_service;

	Button stepButton;
	Button minRangeButton;
	Button maxRangeButton;

	MySeekBar mySeekBar;

	private ResponseReceiver receiver;

	boolean localServiceBound = false;
	SeekBarServiceBinding mService;

	Intent intent_4_broadcast_service;

	IntentFilter intent_filter_from_broadcast_service;

	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		result_view_4_broadcast_service = (TextView) findViewById(R.id.result_view_4_broadcast_service);
		result_view_4_local_service = (TextView) findViewById(R.id.result_view_4_local_service);
		result_view_4_messenger_service = (TextView) findViewById(R.id.result_view_4_messenger_service);
		result_view_4_aidl_service = (TextView) findViewById(R.id.result_view_4_aidl_service);
		stepButton = (Button) findViewById(R.id.button_4_step);
		minRangeButton = (Button) findViewById(R.id.button_4_min);
		maxRangeButton = (Button) findViewById(R.id.button_4_max);


		mySeekBar = (MySeekBar) findViewById(R.id.ms);

		intent_filter_from_broadcast_service = new IntentFilter(ResponseReceiver.ACTION_RESP);
		intent_filter_from_broadcast_service.addCategory(Intent.CATEGORY_DEFAULT);	
		receiver = new ResponseReceiver();
		registerReceiver(receiver, intent_filter_from_broadcast_service);

		intent_4_broadcast_service = new Intent(getApplicationContext(), SeekBarServiceBroadcast.class);

		bindService(new Intent(this, SeekBarServiceBinding.class), mConnectionToLocalService, Context.BIND_AUTO_CREATE);

		bindService(new Intent(this, SeekBarServiceMessenger.class), mConnectionToMessengerService , Context.BIND_AUTO_CREATE);

		//for aidl
		aidlConnection = new AidlConnection();
		Intent intentToAidl = new Intent("com.flaviocapaccio.seekbartest.SeekBarServiceAidl");
		bindService(intentToAidl, aidlConnection, Context.BIND_AUTO_CREATE);


		mySeekBar.setOnLeftButtonClicked(new OnLeftButtonClicked() {
			@Override
			public void onLeftButtonClicked() {
				sendMessageToServices(mySeekBar.getProgress());
			}
		});

		mySeekBar.setOnRightButtonClicked(new OnRightButtonClicked() {

			@Override
			public void onRightButtonClicked() {
				sendMessageToServices(mySeekBar.getProgress());
			}
		});

		mySeekBar.setOnMySeekBarChangeListener(new OnMySeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				sendMessageToServices(mySeekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar) {
			}
		});

		stepButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.edit_text_4_step);
				int step = Integer.parseInt(et.getText().toString());
				try {
					mySeekBar.setStep(step);
				} catch (InvalidParametersException e) {
					Toast.makeText(getApplicationContext(), "Step non valido!", Toast.LENGTH_LONG).show();
				}
			}
		});

		minRangeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.edit_text_4_min);
				int min = Integer.parseInt(et.getText().toString());
				mySeekBar.setMinValue(min);
			}
		});

		maxRangeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.edit_text_4_max);
				int max = Integer.parseInt(et.getText().toString());
				mySeekBar.setMaxValue(max);
			}
		});

	}

	@Override
	protected void onStop() {
		unregisterReceiver(receiver);

		if (localServiceBound) {
			unbindService(mConnectionToLocalService);
			localServiceBound = false;
		}

		if(messengerServiceBound){
			unbindService(mConnectionToMessengerService);
			messengerServiceBound = false;
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		stopService(intent_4_broadcast_service);
		unbindService(aidlConnection);
		super.onDestroy();
	}

	private void sendMessageToServices(int value){
		intent_4_broadcast_service.putExtra("Value", value);
		startService(intent_4_broadcast_service);

		if (localServiceBound) {
			mService.startThread(mySeekBar.getProgress());
		}

		if(messengerServiceBound){
			sendMessageToMessengerService(mySeekBar.getProgress());
		}

		sendMessageToAidlMessenger(mySeekBar.getProgress());
	}

	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "com.flaviocapaccio.intent.action.MESSAGE_PROCESSED";
		@Override
		public void onReceive(Context context, Intent intent) {
			String text = intent.getStringExtra("Value_return");
			result_view_4_broadcast_service.setText(text);
		}
	}

	private ServiceConnection mConnectionToLocalService = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService(new Callback());
			localServiceBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			localServiceBound = false;
		}
	};

	public class Callback implements ICallback{
		public void notifySettingCompleted(final String s) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					result_view_4_local_service.setText(s);
				}
			});

		}
	}

	private void sendMessageToMessengerService(final int value) {

		Message msg = Message.obtain(null, MSG_PROGRESS, value , 0);
		msg.replyTo = replyMessenger;
		try {
			activityMessenger.send(msg);
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in sendMessageToMessengerService", e);
		}
	}

	private void sendMessageToAidlMessenger(final int progress) {
		new Thread(){
			public void run() {
				final String s;
				try {
					s = aidlService.notifySettingCompleted(progress);

					handler.post(new Runnable() {
						@Override
						public void run() {
							result_view_4_aidl_service.setText(s);
						}
					});

				} catch (RemoteException e) {
					Log.e(TAG, "RemoteException in sendMessageToAidlMessenger", e);
				}
			};
		}.start();
	}

	//I use this code to manage reply message
	final Messenger replyMessenger = new Messenger(new IncomingHandler());

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_PROGRESS_EVALUATED:
				result_view_4_messenger_service.setText(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	}

	// Most code for Communication with MessengerService (SeekBarServiceMessenger)
	Messenger activityMessenger;
	Boolean messengerServiceBound;

	private ServiceConnection mConnectionToMessengerService = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			activityMessenger = null;
			messengerServiceBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			activityMessenger = new Messenger(service);
			messengerServiceBound = true;
		}
	};


	// Most code for communication using aidl

	ISeekBarService aidlService;
	AidlConnection aidlConnection;

	public class AidlConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			aidlService = ISeekBarService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			aidlService = null;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
