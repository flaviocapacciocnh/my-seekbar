package com.flaviocapaccio.seekbartest;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;

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