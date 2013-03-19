package com.flaviocapaccio.seekbartest;

import com.flaviocapaccio.seekbartest.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;

public class SeekBarServiceMessenger extends Service{

	final Messenger messenger = new Messenger(new IncomingHandler());

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

	private class IncomingHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MainActivity.MSG_PROGRESS:
				HandlingMessageThread thread = new HandlingMessageThread(msg.replyTo);
				thread.setProgress(msg.arg1);
				thread.start();
			default:
				super.handleMessage(msg);
			}
		}
	}

	class HandlingMessageThread extends Thread {

		private static final String TAG = "seekbartest";
		private Messenger handler;
		private int progress;


		public HandlingMessageThread(Messenger handler) {
			this.handler = handler;
		}

		public void run() {
			runSetting(progress);	
		}

		private void runSetting(int progr) {
			try {
				//I'm simulating time to run operation
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				Log.e(TAG, "Eccezione lanciata dallo sleep!", e1);
			}
			Message msg = new Message();
			String resultTxt = "Messenger message: Valore settato a " + progr + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
			msg.obj = resultTxt;
			msg.what = MainActivity.MSG_PROGRESS_EVALUATED;
			try {
				handler.send(msg);
			} catch (RemoteException e) {
				Log.e(TAG, "RemoteException in notifySettingCompleted", e);
			}
		}

		public int getProgress() {
			return progress;
		}
		public void setProgress(int progr) {
			this.progress = progr;
		}

	}
}