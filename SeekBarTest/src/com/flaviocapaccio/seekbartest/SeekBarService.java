package com.flaviocapaccio.seekbartest;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;

import com.flaviocapaccio.seekbartest.MainActivity.ResponseReceiver;

public class SeekBarService extends IntentService {

	public SeekBarService() {
		super("SeekBarService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int tmp = intent.getExtras().getInt("Value");
		
		//I'm simulating time to perform an operation.
		SystemClock.sleep(2500);
		
		String resultTxt = "Valore settato a " + tmp + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
		
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("Value_return", resultTxt);
		sendBroadcast(broadcastIntent);
	}
}
