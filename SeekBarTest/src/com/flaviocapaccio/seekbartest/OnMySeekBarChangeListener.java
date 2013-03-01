package com.flaviocapaccio.seekbartest;

import android.widget.SeekBar;

public interface OnMySeekBarChangeListener {
	public abstract void onStopTrackingTouch(SeekBar seekBar);
	public abstract void onStartTrackingTouch(SeekBar seekBar);
	public abstract void onProgressChanged(SeekBar seekBar, int progress);
}
