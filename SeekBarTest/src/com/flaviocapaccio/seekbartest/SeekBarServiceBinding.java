package com.flaviocapaccio.seekbartest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

public class SeekBarServiceBinding extends Service {

	private String TAG = "flavio";

	private final IBinder mBinder = new LocalBinder();

	static ICallback caller;

	public class LocalBinder extends Binder{		
		SeekBarServiceBinding getService(ICallback caller){
			SeekBarServiceBinding.caller = caller;
			return SeekBarServiceBinding.this;
		}
	}

	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public void startThread(final int progress) {

		new Thread() {

			@Override
			public void run() {
				try {
					//I'm simulating time to perform an operation.
					Thread.sleep(3000);
					String res = "LocalService message: Valore settato a " + progress + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
					if(caller!=null){
						caller.notifySettingCompleted(res);
					}
				} catch (InterruptedException e) {
					Log.e(TAG, "InterruptException in startThread", e);
				}
			}
		}.start();
	}

	public interface ICallback {
		public void notifySettingCompleted(String s);
	}
}
