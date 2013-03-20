package com.flaviocapaccio.seekbartest;

import java.util.Random;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;

import com.flaviocapaccio.seekbartest.MainActivity.ResponseReceiver;

public class SeekBarServiceBroadcast extends IntentService {

	public SeekBarServiceBroadcast() {
		super("SeekBarServiceBroadcast");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int progress = intent.getExtras().getInt("Value");
		Random rnd = new Random(progress);
		while(true){
			SystemClock.sleep(1000);
			String resultTxt = "Broadcasted message: Valore settato a " + rnd.nextInt() + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("Value_return", resultTxt);
			sendBroadcast(broadcastIntent);	
		}	
	}
}