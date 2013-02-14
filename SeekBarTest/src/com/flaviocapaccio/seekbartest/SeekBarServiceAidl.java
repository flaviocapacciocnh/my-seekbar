package com.flaviocapaccio.seekbartest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.format.DateFormat;

public class SeekBarServiceAidl extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		return new ISeekBarService.Stub() {
			
			@Override
			public String notifySettingCompleted(int progress) throws RemoteException {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "Aidl message: Valore settato a " + progress + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
			}
		};
	}

}
