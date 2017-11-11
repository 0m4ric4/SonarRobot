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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by omarica on 11/6/17.
 */

public class SonarView extends View {
    DisplayMetrics mDisplayMetrics;
    int mHeight;
    int mWidth;
    int rectHeight;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    boolean isDrawLine = false;
    long angle;
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
        //Initializer Method


        //Getting the height and width of the device
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mHeight = mDisplayMetrics.heightPixels;
        mWidth = mDisplayMetrics.widthPixels;

        mDatabase = FirebaseDatabase.getInstance(); // Firebase database object
        myRef = mDatabase.getReference(); // Firebase database reference

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //super.onDraw(canvas);
        // Rectangle Drawing
        rectHeight = (int) (0.4 * mHeight); // Rectangle height to be 40% of the screen height
        Rect rect = new Rect(); // Creating a rectangle object
        rect.left = 0; // Setting the left edge to coordinate 0
        rect.top = 0; // Setting the top edge  to coordinate 0
        rect.right = rect.left + mWidth; // Setting the right edge
        rect.bottom = rect.top + rectHeight; // Setting the bottom edge
        Paint rectPaint = new Paint(); // Creating a Paint object
        rectPaint.setColor(Color.BLACK); // Setting the color of the paint object from Color class constants
        canvas.drawRect(rect, rectPaint); // Drawing the rectangle into the canvas by providing the rect object, and color object

        //Circle Drawing
        Paint circlePaint = new Paint(); // Creating a paint object for the circles
        circlePaint.setColor(Color.GREEN); // Setting the color of the paint object from Color class constants
        circlePaint.setStyle(Paint.Style.STROKE); // Setting the style of the paint, stroke to paint the circumference of the circle
        circlePaint.setStrokeWidth(5); // Width of the stroke
        //Drawing circles centered at coordinate X: WidthOfRectangle/2 , Y: HeightOfRectangle
        canvas.drawCircle(mWidth / 2, rectHeight, 100, circlePaint); //Radius 100 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 200, circlePaint); //Radius 200 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 300, circlePaint); //Radius 300 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 400, circlePaint); //Radius 400 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 500, circlePaint); //Radius 500 pixels

        // Drawing a point at coordinate X: WidthOfRectangle/2 , Y: HeightOfRectangle
        canvas.drawPoint(mWidth / 2, rectHeight, circlePaint);
        // Draw Line
        Paint linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(4);

        //If true, means angle has changed and a line is to be drawn
        if (isDrawLine) {
            drawLine(angle, canvas, linePaint);
            unDrawLine(angle - 1, canvas);
            isDrawLine = false;
        }


        //Firebase Listeners
        //Listens to changes in the angle
        myRef.child("angle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                angle = (long) dataSnapshot.getValue(); // Retrieve angle from database
                isDrawLine = true; // Used in onDraw()
                invalidate(); // Calls onDraw() again


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Listens to changes in the distance
        myRef.child("distance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long distance = (long) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // A function to draw a line given an angle, canvas, and paint
    private void drawLine(long angle, Canvas canvas, Paint paint) {
        double radians = Math.toRadians(angle);
        float lineX = (float) (500 * Math.cos(radians)) + mWidth / 2;
        float lineY = (float) (500 * Math.sin(-radians)) + rectHeight;

        canvas.drawLine(mWidth / 2, rectHeight, lineX, lineY, paint);
    }

    // A function to un draw a line given an angle, canvas, and paint

    private void unDrawLine(long angle, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        drawLine(angle, canvas, paint);
    }


}



