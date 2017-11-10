package com.omarica.sonarrobot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by omarica on 11/6/17.
 */

public class SonarView extends View {
    private static final int SIDE_LENGTH = 100;
    DisplayMetrics mDisplayMetrics;
    int mHeight;
    int mWidth;
    public SonarView(Context context) {
        super(context);
        init(null);
    }

    public SonarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SonarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SonarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set) {
         mDisplayMetrics= new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        mHeight= mDisplayMetrics.heightPixels;
        mWidth = mDisplayMetrics.widthPixels;

    }

    @Override
    protected void onDraw(Canvas canvas) {
       // super.onDraw(canvas);
        int rectHeight = (int) (0.4*mHeight);
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = rect.left + mWidth;
        rect.bottom = rect.top +rectHeight;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        Paint paintTwo = new Paint();
        paintTwo.setColor(Color.RED);
        paintTwo.setStyle(Paint.Style.STROKE);
        paintTwo.setStrokeWidth(4);
        canvas.drawRect(rect,paint);
        canvas.drawPoint(mWidth/2,rectHeight,paintTwo);
        canvas.drawCircle(mWidth/2,rectHeight,100,paintTwo);
        canvas.drawCircle(mWidth/2,rectHeight,200,paintTwo);
        canvas.drawCircle(mWidth/2,rectHeight,300,paintTwo);
        canvas.drawCircle(mWidth/2,rectHeight,400,paintTwo);
        canvas.drawCircle(mWidth/2,rectHeight,500,paintTwo);

    }
}
