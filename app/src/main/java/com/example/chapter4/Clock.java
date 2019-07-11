package com.example.chapter4;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Calendar;

public class Clock extends View
{
	private Paint mPaint;
	private Calendar mTime;

	private int mRadius;
	private int mMargin;

	public Clock(Context context)
	{
		this(context, null);
	}

	public Clock(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public Clock(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();

		// get the size of screen
		Resources resources = this.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		// set the size of clock
		mRadius = width < height? width: height;
		mRadius = (int)(mRadius/2.0 * 0.7);
		mMargin = mRadius / 6;
		mTime = Calendar.getInstance();
	}

	public void updateTime(Calendar time)
	{
		// update time
		mTime = time;
		// redraw the clock
		this.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(
				mRadius * 2 + 50,
				mRadius * 2 + mMargin * 2 + 100
		);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		// center position
		int centerPoint[] = {
				getMeasuredWidth() / 2,
				getMeasuredHeight() / 2
		};
		// set paint style
		mPaint.setColor(getResources().getColor(R.color.mainColor));
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeCap(Paint.Cap.ROUND);

		drawScale(centerPoint, canvas);
		drawTexts(centerPoint, canvas);
		drawNeedles(centerPoint, canvas);
		drawCenter(centerPoint, canvas);
	}

	private void drawScale(int[] centerPoint, Canvas canvas)
	{
		// TODO: draw scale
		for(int i=0; i<60; i++)
		{
			// check whether this is a long scale
			boolean isLong = (i%5 == 0);
			// set scale width
			int scaleWidth = isLong? 10: 5;
			mPaint.setStrokeWidth(scaleWidth);

			// get start point
			float startPoint[] = {
					centerPoint[0] + mRadius * (float)Math.sin(Math.toRadians(i * 6)),
					centerPoint[1] - mRadius * (float)Math.cos(Math.toRadians(i * 6))
			};
			// get end point
			int len = isLong? 50: 40;
			float endPoint[] = {
					centerPoint[0] + (mRadius - len) * (float)Math.sin(Math.toRadians(i * 6)),
					centerPoint[1] - (mRadius - len) * (float)Math.cos(Math.toRadians(i * 6))
			};
			// draw line
			canvas.drawLine(
					startPoint[0], startPoint[1],
					endPoint[0], endPoint[1],
					mPaint
			);
		}
	}

	private void drawTexts(int[] centerPoint, Canvas canvas)
	{
		// TODO: draw numbers and AM/PM
		// set paint width
		mPaint.setStrokeWidth(5);
		mPaint.setTextSize(100);
		mPaint.setTextAlign(Paint.Align.CENTER);
		// draw numbers
		for(int i=0; i<12; i++)
		{
			// get parameters
			int margin = 130;
			int num = i + 1;
			String numText = num + "";

			// get text centerPoint
			Rect rect = new Rect();
			mPaint.getTextBounds(numText, 0, numText.length(), rect);
			float textCenter[] = {
					centerPoint[0] + (mRadius - margin) * (float)Math.sin(Math.toRadians(num * 30)),
					centerPoint[1] - (mRadius - margin) * (float)Math.cos(Math.toRadians(num * 30))
							+ rect.height() / 2f
			};

			// draw text
			canvas.drawText(
					numText,
					textCenter[0],
					textCenter[1],
					mPaint
			);
		}

		// draw AM / PM
		mPaint.setTextSize(80);
		String timeSection = mTime.get(Calendar.HOUR_OF_DAY) < 12? "AM": "PM";
		canvas.drawText(
				timeSection,
				centerPoint[0],
				centerPoint[1] + mRadius + mMargin,
				mPaint
		);
	}

	private void drawNeedles(int[] centerPoint, Canvas canvas)
	{
		// TODO: draw needles in order of: hour, minute, second
		// set parameters
		int strokeWidth[] = {
				20, 15, 5
		};
		float length[] = {
				mRadius * 0.5f, mRadius * 0.67f, mRadius * 0.88f
		};
		int hour = mTime.get(Calendar.HOUR_OF_DAY);
		int minute = mTime.get(Calendar.MINUTE);
		int second = mTime.get(Calendar.SECOND);
		double angle_rad[] = {
				Math.toRadians(((hour * 60 + minute) * 60 + second) / (60.0 * 60.0 * 12.0) * 360),
				Math.toRadians((minute * 60 + second) / (60.0 * 60.0) * 360),
				Math.toRadians(second / 60.0 * 360),
		};
		int color[] = {
				Color.WHITE, Color.WHITE, Color.RED
		};

		// draw needles
		float endPoint[] = new float[2];
		for(int i=0; i<3; i++)
		{
			// set width
			mPaint.setStrokeWidth(strokeWidth[i]);
			// set color
			mPaint.setColor(color[i]);
			// set end point
			endPoint[0] = centerPoint[0] + length[i] * (float)Math.sin(angle_rad[i]);
			endPoint[1] = centerPoint[1] - length[i] * (float)Math.cos(angle_rad[i]);
			// draw line
			canvas.drawLine(
					centerPoint[0], centerPoint[1],
					endPoint[0], endPoint[1],
					mPaint
			);
		}

		// reset the color
		mPaint.setColor(getResources().getColor(R.color.mainColor));
	}

	private void drawCenter(int[] centerPoint, Canvas canvas)
	{
		// TODO: draw center point
		// set paint width
		mPaint.setStrokeWidth(5);
		canvas.drawCircle(
				centerPoint[0], centerPoint[1], 30, mPaint
		);
		mPaint.setColor(getResources().getColor(R.color.gray));
		canvas.drawCircle(
				centerPoint[0], centerPoint[1], 22, mPaint
		);
		mPaint.setColor(getResources().getColor(R.color.mainColor));
	}

}
