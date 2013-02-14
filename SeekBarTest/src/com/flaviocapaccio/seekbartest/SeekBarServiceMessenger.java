package com.flaviocapaccio.seekbartest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

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
}
