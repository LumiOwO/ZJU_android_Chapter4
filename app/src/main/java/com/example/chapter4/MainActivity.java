package com.example.chapter4;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
	// main view
	private View mMain_View;
	// clock_plate
	private View mClock_Plate;
	private Clock mPlate;
	// clock_digit
	private View mClock_Digit;
	private TextView mHour;
	private TextView mMinute;
	private TextView mSecond;

	// show which clock
	private boolean mSwitch;	// true --> plate, false --> digit
	private static final boolean PLATE = false;
	private static final boolean DIGIT = true;

	// time
	private Calendar mTime;
	private Handler mTimeHandler;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get widgets
		mMain_View = findViewById(R.id.main_view);
		mClock_Plate = findViewById(R.id.clock_plate);
		mPlate = findViewById(R.id.plate);
		mClock_Digit = findViewById(R.id.clock_digit);
		mHour = findViewById(R.id.hour);
		mMinute = findViewById(R.id.minute);
		mSecond = findViewById(R.id.second);

		// show the plate when init
		mSwitch = PLATE;
		mClock_Plate.setAlpha(1);
		mClock_Digit.setAlpha(0);

		// click to switch clock
		mMain_View.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// switch the showing clock
				switchClock();
			}
		});
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		// update time
		mTime = Calendar.getInstance();
		final int delayTime = 1000;
		updateShowingTime();

		// create handler
		mTimeHandler = new Handler();
		mTimeHandler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				// get now time
				mTime.add(Calendar.SECOND, 1);
//				Log.d("hour", mTime.get(Calendar.HOUR_OF_DAY) + "");
//				Log.d("minute", mTime.get(Calendar.MINUTE) + "");
//				Log.d("second", mTime.get(Calendar.SECOND) + "");

				updateShowingTime();

				mTimeHandler.postDelayed(this, delayTime);
			}
		}, delayTime);
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		// remove handler
		mTimeHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		// TODO: save the info before switching orientation

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		//TODO: read the info after switching orientation

	}

	private void switchClock()
	{
		// find which to hide and which to show
		mSwitch = !mSwitch;
		View hideView =  (mSwitch)? mClock_Plate: mClock_Digit;
		View showView = (!mSwitch)? mClock_Plate: mClock_Digit;

		// create animation
		ObjectAnimator hideAnime = ObjectAnimator.ofFloat(
			hideView, "alpha", 1, 0
		);
		hideAnime.setDuration(500);
		ObjectAnimator showAnime = ObjectAnimator.ofFloat(
			showView, "alpha", 0, 1
		);
		showAnime.setDuration(500);

		// play animation
		AnimatorSet set = new AnimatorSet();
		set.playTogether(hideAnime, showAnime);
		set.start();
	}

	private void updateShowingTime()
	{
		// update digit clock time
		mHour.setText(String.format(Locale.getDefault(), "%02d", mTime.get(Calendar.HOUR_OF_DAY)));
		mMinute.setText(String.format(Locale.getDefault(), "%02d", mTime.get(Calendar.MINUTE)));
		mSecond.setText(String.format(Locale.getDefault(), "%02d", mTime.get(Calendar.SECOND)));

		// update plate clock time
		mPlate.updateTime(mTime);
	}
}
