package com.omarica.sonarrobot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {
    SonarView mSonarView; // A variable to represent the SonarView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSonarView = (SonarView) findViewById(R.id.sonarView); // Initializing the SonarView object

        // Getting screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;

        mSonarView.getLayoutParams().height = (int) (0.4 * screenHeight); // Setting the SonarView's height to 40% of the screen height


    }
}
