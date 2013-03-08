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
import android.widget.SeekBar;
import android.widget.TextView;

import com.flaviocapaccio.seekbartest.MySeekBar.OnLeftButtonClicked;
import com.flaviocapaccio.seekbartest.MySeekBar.OnMySeekBarChangeListener;
import com.flaviocapaccio.seekbartest.MySeekBar.OnRightButtonClicked;
import com.flaviocapaccio.seekbartest.SeekBarServiceBinding.ICallback;
import com.flaviocapaccio.seekbartest.SeekBarServiceBinding.LocalBinder;

public class MainActivity extends Activity{

	static final int MSG_PROGRESS = 1;
	public static final int MSG_PROGRESS_EVALUATED = 2;
	private String TAG = "seekbartest";

	TextView result_view_4_broadcast_service;
	TextView result_view_4_local_service;
	TextView result_view_4_messenger_service;
	TextView result_view_4_aidl_service;

	MySeekBar mySeekBar;	

	private ResponseReceiver receiver;

	boolean mBound = false;
	SeekBarServiceBinding mService;

	Intent intent_4_broadcast_service;

	IntentFilter intent_filter_from_broadcast_service;

	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		intent_filter_from_broadcast_service = new IntentFilter(ResponseReceiver.ACTION_RESP);
		intent_filter_from_broadcast_service.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ResponseReceiver();
		registerReceiver(receiver, intent_filter_from_broadcast_service);

		bindService(new Intent(this, SeekBarServiceBinding.class), mConnection, Context.BIND_AUTO_CREATE);

		intent_4_broadcast_service = new Intent(getApplicationContext(), SeekBarServiceBroadcast.class);

		result_view_4_broadcast_service = (TextView) findViewById(R.id.result_view_4_broadcast_service);
		result_view_4_local_service = (TextView) findViewById(R.id.result_view_4_local_service);
		result_view_4_messenger_service = (TextView) findViewById(R.id.result_view_4_messenger_service);
		result_view_4_aidl_service = (TextView) findViewById(R.id.result_view_4_aidl_service);


		mySeekBar = (MySeekBar) findViewById(R.id.ms);

		bindService(new Intent(this, SeekBarServiceMessenger.class), mConnectionToMessengerService , Context.BIND_AUTO_CREATE);

		//for aidl
		aidlConnection = new AidlConnection();
		Intent intentToAidl = new Intent("com.flaviocapaccio.seekbartest.SeekBarServiceAidl");
		bindService(intentToAidl, aidlConnection, Context.BIND_AUTO_CREATE);
		

		mySeekBar.setOnLeftButtonClicked(new OnLeftButtonClicked() {
			@Override
			public void onLeftButtonClicked() {
				Log.d("flavio", "Sei in onleftclick del main");
				sendMessageToServices(mySeekBar.getProgress());
			}
		});

		mySeekBar.setOnRightButtonClicked(new OnRightButtonClicked() {

			@Override
			public void onRightButtonClicked() {
				Log.d("flavio", "Sei in onrightclick del main");
				sendMessageToServices(mySeekBar.getProgress());
			}
		});

		mySeekBar.setOnMySeekBarChangeListener(new OnMySeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d("flavio", "Main activity stopped seekbar");
				sendMessageToServices(mySeekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d("flavio", "Main activity started seekbar");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar) {
				Log.d("flavio", "Main activity progress changed: ");
				sendMessageToServices(mySeekBar.getProgress());
			}
		});

	}

	@Override
	protected void onStop() {
		unregisterReceiver(receiver);
		
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}

		if(serviceBound){
			unbindService(mConnectionToMessengerService);
			serviceBound = false;
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

		if (mBound) {
			mService.startThread(mySeekBar.getProgress());
		}

		if(serviceBound){
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

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService(new Callback());
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


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
		Thread thread = new Thread() {

			public void run() {

				Message msg = Message.obtain(null, MSG_PROGRESS, value , 0);
				msg.replyTo = replyMessenger;
				try {
					activityMessenger.send(msg);
				} catch (RemoteException e) {
					Log.e(TAG, "RemoteException in sendMessageToMessengerService", e);
				}
			}
		};
		thread.start();
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
	Boolean serviceBound;

	private ServiceConnection mConnectionToMessengerService = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			activityMessenger = null;
			serviceBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			activityMessenger = new Messenger(service);
			serviceBound = true;

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

}
