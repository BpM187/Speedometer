package com.speedometer.calculator.app.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class ASurface extends SurfaceView {
    public ASurface(Context context) {
        super(context);
    }

    public ASurface(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ASurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ASurface(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setProgress(float progress) {
        this.progress = progress;
        invalidate(); // this triggers ondraw();
    }

    public float getProgress() {
        return this.progress;

    }

    /**
     * The factor that goes from -1 <-> 0 <-> 1
     */
    private float progress = 0;

    /**
     * Values from iOS
     */
//    float lineWidth = 22.5f;
    float threshold = .05f;


    private float changeRange(float oldValue) {
        float oldRange = (160);
        float newRange = (360);
        return (((oldValue) * newRange) / oldRange);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = w;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(w / 12f, h / 12f, w - w / 12f, h - h / 12f, 46F, changeRange(progress), false, getStrokeBluePaint());
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setWillNotDraw(false);
    }

    Paint bluePaint;

    public Paint getStrokeBluePaint() {
        if (bluePaint == null) {
            bluePaint = new Paint();
            bluePaint.setColor(Color.parseColor("#019aa8"));
            bluePaint.setStyle(Paint.Style.STROKE);
            bluePaint.setStrokeWidth(18);
            bluePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
            bluePaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
            bluePaint.setPathEffect(new CornerPathEffect(10));
        }
        return bluePaint;
    }
}
