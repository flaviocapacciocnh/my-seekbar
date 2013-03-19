package com.flaviocapaccio.seekbartest;

import com.flaviocapaccio.seekbartest.ISeekBarService;
import com.flaviocapaccio.seekbartest.ISeekBarService.Stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;

public class SeekBarServiceAidl extends Service{

	private String TAG = "seekbartest";
	@Override
	public IBinder onBind(Intent arg0) {
		return new ISeekBarService.Stub() {

			@Override
			public String notifySettingCompleted(int progress) throws RemoteException {
				try {
					//I'm simulating time to perform operation
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Log.e(TAG, "RemoteException in sendMessageToMessengerService", e);
				}
				return "Aidl message: Valore settato a " + progress + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
			}
		};
	}

}
