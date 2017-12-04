package com.omarica.sonarrobot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omarica on 11/6/17.
 */

public class SonarView extends View {
    private static final String ANGLE_TAG = "angle_tag";
    DisplayMetrics mDisplayMetrics;
    int mHeight;
    int mWidth;
    int rectHeight;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    boolean isDrawLine = false;
    boolean isDrawObject = false;
    long angle;
    long objectDistance;
    List<Long> angleList;
    List<Point> mObjectsPoints;
    Paint objectPaint;
    Paint circlePaint;
    Point p0;
    Point p180;
    Rect rect;
    Paint rectPaint;
    Paint linePaint;
    Paint bottomLinePaint;
    Point object = null;
    String direction;
    boolean isAngleCorrect = false;

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

        mObjectsPoints = new ArrayList<>();
        angleList = new ArrayList<>();
        //Object Paint
        objectPaint = new Paint();
        objectPaint.setStrokeCap(Paint.Cap.ROUND);
        objectPaint.setColor(Color.RED);
        objectPaint.setAlpha(70);
        objectPaint.setAntiAlias(true);
        objectPaint.setStrokeWidth(60);

        //Circle Paint
        circlePaint = new Paint(); // Creating a paint object for the circles
        circlePaint.setColor(Color.GREEN); // Setting the color of the paint object from Color class constants
        circlePaint.setStyle(Paint.Style.STROKE); // Setting the style of the paint, stroke to paint the circumference of the circle
        circlePaint.setStrokeWidth(8); // Width of the stroke
        circlePaint.setAntiAlias(true);
        p0 = getXYfromAngle(0, 500);
        p180 = getXYfromAngle(180, 500);

        //Rectangle
        rectHeight = (int) (0.35 * mHeight); // Rectangle height to be 40% of the screen height
        rect = new Rect(); // Creating a rectangle object
        rect.left = 0; // Setting the left edge to coordinate 0
        rect.top = 0; // Setting the top edge  to coordinate 0
        rect.right = rect.left + mWidth; // Setting the right edge
        rect.bottom = rect.top + rectHeight; // Setting the bottom edge
        rectPaint = new Paint(); // Creating a Paint object
        rectPaint.setColor(Color.BLACK); // Setting the color of the paint object from Color class constants


        //Line Paint
        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(8);
        linePaint.setAntiAlias(true);
        bottomLinePaint = new Paint();
        bottomLinePaint.setColor(Color.GREEN);
        bottomLinePaint.setStrokeWidth(16);
        bottomLinePaint.setAntiAlias(true);

        //Firebase Listeners
        //Listens to changes in the angle

        myRef.child("direction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                direction = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("angle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                angle = (long) dataSnapshot.getValue(); // Retrieve angle from database
                isDrawLine = true; // Used in onDraw()
                //postInvalidate();
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
                objectDistance = (long) dataSnapshot.getValue();
                isDrawObject = true;
                //postInvalidate();
                invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.save();
        super.onDraw(canvas);
        // Rectangle Drawing

        canvas.drawRect(rect, rectPaint); // Drawing the rectangle into the canvas by providing the rect object, and color object

        //Circle Drawing
        //Drawing circles centered at coordinate X: WidthOfRectangle/2 , Y: HeightOfRectangle
        canvas.drawCircle(mWidth / 2, rectHeight, 100, circlePaint); //Radius 100 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 200, circlePaint); //Radius 200 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 300, circlePaint); //Radius 300 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 400, circlePaint); //Radius 400 pixels
        canvas.drawCircle(mWidth / 2, rectHeight, 500, circlePaint); //Radius 500 pixels

        // Drawing a point at coordinate X: WidthOfRectangle/2 , Y: HeightOfRectangle
        canvas.drawPoint(mWidth / 2, rectHeight, circlePaint);
        // Draw Line

        canvas.drawLine(p0.x, p0.y + (rectHeight), p180.x, p180.y + (rectHeight), bottomLinePaint);

        //If true, means angle has changed and a line is to be drawn
        if (isDrawLine) {
            // check if angle is not in the list
            if (angleList.size() > 0) {
                if (direction.equals("anticlockwise"))
                    isAngleCorrect = (angle > angleList.get(angleList.size() - 1));
                else if (direction.equals("clockwise"))
                    isAngleCorrect = (angle < angleList.get(angleList.size() - 1));
            }


           /* if(angleList.size() > 1) {
                 isAngleCorrect = ( (angleList.get(angleList.size()-1) > angle)
                        && (angleList.get(angleList.size()-2) > angle) ) ||
                ( (angleList.get(angleList.size()-1) < angle)
                        && (angleList.get(angleList.size()-2) <  angle) );
            } */
           /* if(angleList.size() != 0) {
                isAngleCorrect = (angleList.get(angleList.size()-1) > angle)
                        && (angleList.get(angleList.size()-2) > angle);
            }*/


            Log.d(ANGLE_TAG, String.valueOf(isAngleCorrect));
            if (isAngleCorrect) {
                drawLine(angle, canvas, linePaint);

            }
            angleList.add(angle);


            //Add angle to the list

            isDrawLine = false;
        }


        //Loops the list of objects

        for (Point p : mObjectsPoints) { // Draws every point behind the line

            drawObject(p, canvas, objectPaint);
        }


        if (isDrawObject) {
            object = getXYfromAngle(angle, objectDistance); // gets XY coordinates of an object
            mObjectsPoints.add(object);
            drawObject(object, canvas, objectPaint); // Draws the object
            isDrawObject = false;
        }
        if (angle == 180 || angle == 0) {// Clears points at 0 and 180 degrees
            mObjectsPoints.clear();
            angleList.clear();

        }


    }

    //Draws an object
    private void drawObject(Point p, Canvas canvas, Paint paint) {

        canvas.drawPoint(p.x, p.y, paint);

    }

    //Gets the XY coordinates of an object given its distance
    private Point getXYfromAngle(long angle, long objectDistance) {
        Point point = new Point();
        double radians = Math.toRadians(angle);
        float lineX = (float) ((objectDistance) * Math.cos(radians)) + mWidth / 2;
        float lineY = (float) ((objectDistance) * Math.sin(-radians)) + rectHeight;
        point.x = (int) lineX;
        point.y = (int) lineY;
        return point;


    }

    // A function to draw a line given an angle, canvas, and paint
    private void drawLine(long angle, Canvas canvas, Paint paint) {
        double radians = Math.toRadians(angle);
        float lineX = (float) (500 * Math.cos(radians)) + mWidth / 2;
        float lineY = (float) (500 * Math.sin(-radians)) + rectHeight;

        canvas.drawLine(mWidth / 2, rectHeight, lineX, lineY, paint);
    }

}



