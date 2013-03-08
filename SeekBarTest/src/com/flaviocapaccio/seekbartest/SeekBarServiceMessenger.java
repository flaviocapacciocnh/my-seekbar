package com.flaviocapaccio.seekbartest;

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

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

	final Messenger messenger = new Messenger(new IncomingHandler());

	class IncomingHandler extends Handler{
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
			try {
				Thread.sleep(3000);
				notifySettingCompleted(progress);	
			}catch(InterruptedException ex) {}
		}

		private void notifySettingCompleted(int progr) {
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