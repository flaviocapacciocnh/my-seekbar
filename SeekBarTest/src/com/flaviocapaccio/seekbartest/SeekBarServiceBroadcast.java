package com.flaviocapaccio.seekbartest;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

import com.flaviocapaccio.seekbartest.MainActivity.ResponseReceiver;

public class SeekBarServiceBroadcast extends IntentService {

	private int count=0;

	public SeekBarServiceBroadcast() {
		super("SeekBarServiceBroadcast");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int tmp = intent.getExtras().getInt("Value");
		Log.d("flavio", "count: " + ++count);

		//I'm simulating time to perform an operation.
		SystemClock.sleep(3000);

		String resultTxt = "Broadcasted message: Valore settato a " + tmp + ".\nUltimo aggiornamento: " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis()) + ".";
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("Value_return", resultTxt);
		sendBroadcast(broadcastIntent);		
	}
}