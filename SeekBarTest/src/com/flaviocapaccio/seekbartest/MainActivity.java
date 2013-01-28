package com.flaviocapaccio.seekbartest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView minTv = null;
	TextView maxTv = null;
	TextView currentTv = null;
	int minValue = -30;
	int maxValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		minTv = (TextView) findViewById(R.id.minTv);
		maxTv = (TextView) findViewById(R.id.maxTv);
		currentTv = (TextView) findViewById(R.id.currentTv);

		minTv.setText(Integer.toString(minValue));
		maxTv.setText("270");
		currentTv.setText("0");
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setMax(270);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentTv.setText("" + (progress+minValue));
				
				seekBar.setThumb(writeOnDrawable(R.drawable.ic_launcher,"" + (progress+minValue)));
			}
		});
	}
	
	public BitmapDrawable writeOnDrawable(int drawableId, String text){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint(); 
        paint.setStyle(Style.FILL);  
        paint.setColor(Color.BLACK); 
        paint.setTextSize(20); 

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, 0, bm.getHeight()/2, paint);

        return new BitmapDrawable(bm);
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
