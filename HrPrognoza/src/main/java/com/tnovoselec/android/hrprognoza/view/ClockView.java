package com.tnovoselec.android.hrprognoza.view;

import com.tnovoselec.android.hrprognoza.model.HourlyForecast;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Tommy on 09/04/14.
 */
public class ClockView extends View {

  private static String TWELVE = "12";

  private static String THREE = "3";

  private static String SIX = "6";

  private static String NINE = "9";

  private static String FIFTEEN = "15";

  private static String EIGHTEEN = "18";

  private static String TWENTY_ONE = "21";

  private Paint borderPaint;

  private Paint indicatorPaint;

  private Paint textPaint;

  private double currentValue = Math.PI / 2;

  private float cx;

  private float cy;

  private float radiusClock;

  private float radiusIndicator;

  private HourlyForecast data;

  private int lap = 0;

  public ClockView(Context context) {
    super(context);
    init();
  }

  public ClockView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ClockView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    borderPaint.setStyle(Paint.Style.STROKE);
    indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    indicatorPaint.setStyle(Paint.Style.FILL);
    textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
    textPaint.setTextSize(60);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    radiusClock = getMeasuredWidth() / 3;
    radiusIndicator = getMeasuredWidth() / 20;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (data == null) {
      return;
    }

    // Draw main circle
    cx = (getRight() - getLeft()) / 2;
    cy = (getBottom() - getTop()) / 2;
    canvas.drawCircle(cx, cy, radiusClock, borderPaint);

    // Draw temperature text
    int index = angleToIndex(currentValue);
    String text = "" + (int) data.getForecasts().get(index).getMain().getTempMax();
    float tw = textPaint.measureText(text);
    float th = textPaint.getTextSize();
    canvas.drawText(text, cx - tw / 2, cy + th / 2, textPaint);

    // Draw indicator
    float indcx = cx + (float) (radiusClock * Math.cos(currentValue));
    float indcy = cy - (float) (radiusClock * Math.sin(currentValue));
    canvas.drawCircle(indcx, indcy, radiusIndicator, indicatorPaint);

    // Draw time indicators
    tw = textPaint.measureText(TWELVE);
    canvas.drawText(TWELVE, cx - tw / 2, cy - radiusClock - th, textPaint);
    tw = textPaint.measureText(THREE);
    canvas.drawText(THREE, cx + radiusClock + tw , cy + th / 2, textPaint);
    tw = textPaint.measureText(SIX);
    canvas.drawText(SIX, cx - tw / 2, cy + radiusClock + 2*th, textPaint);
    tw = textPaint.measureText(NINE);
    canvas.drawText(NINE, cx - radiusClock - 2*tw , cy + th / 2, textPaint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_MOVE:
        currentValue = calculateAngle(event);
        invalidate();
        break;
    }
    return true;
  }

  private double calculateAngle(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();
    double a = cy - y;
    double b = x - cx;
    double angle = Math.atan2(a, b);

    return angle;
  }

  private int angleToIndex(double angle) {
    double angleDegrees = Math.toDegrees(angle);
    if (angleDegrees < 0) {
      angleDegrees = 360 + angleDegrees;
    }
    //    angleDegrees-=90;
    int index = (int) ((359 - angleDegrees) / 360 * data.getForecasts().size());
    Log.e("Index", "index: " + index + " angle: " + angleDegrees);
    return index;
  }

  public void setData(HourlyForecast data) {
    this.data = data;
    invalidate();
  }
}
