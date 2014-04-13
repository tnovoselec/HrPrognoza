package com.tnovoselec.android.hrprognoza;

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

    private Paint borderPaint;
    private Paint indicatorPaint;
    private Paint textPaint;
    private double currentValue = 0;
    private float cx;
    private float cy;
    private float radiusClock;
    private float radiusIndicator;


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
        textPaint.setTextSize(40);
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
        cx = (getRight() - getLeft()) / 2;
        cy = (getBottom() - getTop()) / 2;
        canvas.drawCircle(cx, cy, radiusClock, borderPaint);
        String text = ""+ (int)Math.toDegrees(currentValue);
        float tw = textPaint.measureText(text );
        float th = textPaint.getTextSize();
        canvas.drawText(text, cx - tw / 2, cy + th / 2, textPaint);

        float indcx = cx +(float) (radiusClock* Math.cos(currentValue));
        float indcy = cy - (float) (radiusClock * Math.sin(currentValue));
        Log.i("Vura", "x: " + indcx + " y: " + indcy + " angle: " + currentValue);
        canvas.drawCircle(indcx, indcy, radiusIndicator, indicatorPaint);
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
        double a = cy -y;
        double b =x - cx;
        double angle = Math.atan2(a, b);

        return angle;
    }
}
