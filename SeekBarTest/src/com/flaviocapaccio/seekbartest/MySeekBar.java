package com.flaviocapaccio.seekbartest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MySeekBar extends GridLayout{

	OnLeftButtonClicked onLeftButtonClicked = null;
	OnRightButtonClicked onRightButtonClicked = null;
	OnMySeekBarChangeListener onMySeekBarChangeListener = null;

	private int minValue = 0;
	private int maxValue = 300;
	private int shiftProgress = 0;
	private int actualProgress = 0;
	private int step = 1;

	SeekBar seekBar;
	ImageButton leftButton;
	ImageButton rightButton;
	TextView progressTv;
	TextView minProgressValueView;
	TextView maxProgressValueView;

	public MySeekBar(Context context) {
		super(context);
		init(context);
	}

	public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MySeekBar(Context context, int min, int max) {
		super(context);
		if( max <= min || min>=max)
			throw new InvalidParametersException();
		init(context);
		setMinValue(min);
		setMaxValue(max);
	}

	public MySeekBar(Context context, int min, int max, int step) {
		super(context);
		if( max <= min || min>=max || step<=0 || step>=(max-min) )
			throw new InvalidParametersException();
		init(context);
		setMinValue(min);
		setMaxValue(max);
		setStep(step);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		GridLayout layout = (GridLayout)inflater.inflate(R.layout.my_seek_bar, null);
		this.addView(layout);

		progressTv = (TextView) findViewById(R.id.tvProgress);

		leftButton = (ImageButton) findViewById(R.id.leftArrowButton);
		leftButton.setOnClickListener(leftButtonListener);

		rightButton = (ImageButton) findViewById(R.id.rightArrowButton);
		rightButton.setOnClickListener(rightButtonListener);

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

		minProgressValueView = (TextView) findViewById(R.id.minProgressValueView);
		maxProgressValueView = (TextView) findViewById(R.id.maxProgressValueView);
	}


	OnClickListener leftButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(onLeftButtonClicked!=null){
				onLeftButtonClicked.onLeftButtonClicked();
			}
			if((getActualProgress() +  getShiftProgress() - step) <= minValue)	
			{
				setActualProgress(minValue - getShiftProgress());
				progressTv.setText("" + (getActualProgress()  + getShiftProgress()));
				seekBar.setProgress(getActualProgress());	
				return;
			}

			setActualProgress(getActualProgress() - step);
			progressTv.setText("" + (getActualProgress()  + getShiftProgress()));
			seekBar.setProgress(getActualProgress());	
		}
	};

	OnClickListener rightButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(onRightButtonClicked!=null){
				onRightButtonClicked.onRightButtonClicked();
			}
			
			if((getActualProgress() + getShiftProgress() + step)>=maxValue)
			{
				setActualProgress(maxValue - getShiftProgress());
				progressTv.setText("" + (getActualProgress() + getShiftProgress()));
				seekBar.setProgress(getActualProgress());
				return;
			}

			setActualProgress(getActualProgress() + step);
			progressTv.setText("" + (getActualProgress() + getShiftProgress()));
			seekBar.setProgress(getActualProgress());

		}
	};

	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if(onMySeekBarChangeListener!=null){
				onMySeekBarChangeListener.onStopTrackingTouch(seekBar);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			if(onMySeekBarChangeListener!=null){
				onMySeekBarChangeListener.onStartTrackingTouch(seekBar);
			}
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if(onMySeekBarChangeListener!=null){
				onMySeekBarChangeListener.onProgressChanged(seekBar, progress);
			}
			progressTv.setText("" + (getShiftProgress() + progress));
			setActualProgress(progress);

		}
	};

	public void setOnLeftButtonClicked(OnLeftButtonClicked onLeftButtonClicked) {
		this.onLeftButtonClicked = onLeftButtonClicked;
	}

	public void setOnRightButtonClicked(OnRightButtonClicked onRightButtonClicked) {
		this.onRightButtonClicked = onRightButtonClicked;
	}

	public void setOnMySeekBarChangeListener(OnMySeekBarChangeListener onMySeekBarChangeListener) {
		this.onMySeekBarChangeListener = onMySeekBarChangeListener;
	}

	public int getShiftProgress() {
		return shiftProgress;
	}

	public void setShiftProgress(int shiftProgress) {
		this.shiftProgress = shiftProgress;
	}


	public int getActualProgress() {
		return actualProgress;
	}


	public void setActualProgress(int actualProgress) {
		this.actualProgress = actualProgress;
	}


	public int getStep() {
		return step;
	}

	//Check if step has a valid value
	public void setStep(int step) {
		this.step = step;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		if(minValue>getMaxValue()){
			Toast toast = Toast.makeText(getContext(), R.string.indalid_min, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
			return;
		}
		int currentShiftProgress = getShiftProgress();
		if(minValue<0){
			if(minValue<getMinValue()){
				int dif = getMinValue() - minValue;
				setShiftProgress(currentShiftProgress + dif);
			} else {
				int dif = minValue - getMinValue();
				setShiftProgress(currentShiftProgress - dif);
			}
		}

		if(minValue<getMinValue()){
			int dif = getMinValue() - minValue;
			setShiftProgress(currentShiftProgress - dif);
		} else {
			int dif = minValue - getMinValue();
			setShiftProgress(currentShiftProgress + dif);
		}

		this.minValue = minValue;
		seekBar.setMax(getMaxValue() - minValue);
		seekBar.setProgress( 0 );
		progressTv.setText("" + (getShiftProgress()) );
		minProgressValueView.setText("" + getMinValue());
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		if(maxValue<getMinValue()){
			Toast toast = Toast.makeText(getContext(), R.string.indalid_max, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
			return;
		}

		seekBar.setMax(maxValue -  getShiftProgress());
		this.maxValue = maxValue;
		seekBar.setProgress( 0 );
		progressTv.setText("" + (getShiftProgress()) );
		maxProgressValueView.setText("" + getMaxValue());
	}
}
