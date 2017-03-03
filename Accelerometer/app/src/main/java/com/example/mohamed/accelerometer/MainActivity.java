package com.example.mohamed.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{


    private TextView acceleration;
    private TextView steps;
    private Button show;

    private SensorManager sm;
    private Sensor accelerometer;

    private List<Double> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acceleration = (TextView) findViewById(R.id.Acceleration);
        steps = (TextView) findViewById(R.id.Steps);
        show = (Button)findViewById(R.id.show);

        // Initialize Accelerometer sensor
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        list = new ArrayList<Double>();


        show.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                StatisticsUtil su = new StatisticsUtil();
                double mean = su.findMean(list);
                double std = su.standardDeviation(list, mean);
                int stepsNumber = su.finAllPeaks(list, std);
                steps.setText("#Steps: "+stepsNumber);
            }

        });
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];
        acceleration.setText("X: "+x+"\nY: "+y+"\nZ: "+z);
        double mag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)+Math.pow(y, 2));
        list.add(mag);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

/**
 * another method to count steps dynamically.
 * */
/*
 // event object contains values of acceleration, read those
        double x;
        double y;
        double z;

        final double alpha = 0.8; // constant for our filter below

        double[] gravity = {0,0,0};

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        x = event.values[0] - gravity[0];
        y = event.values[1] - gravity[1];
        z = event.values[2] - gravity[2];

        if (!mInitialized) {
            // sensor is used for the first time, initialize the last read values
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            // sensor is already initialized, and we have previously read values.
            // take difference of past and current values and decide which
            // axis acceleration was detected by comparing values

            double deltaX = Math.abs(mLastX - x);
            double deltaY = Math.abs(mLastY - y);
            double deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE)
                deltaX = (float) 0.0;
            if (deltaY < NOISE)
                deltaY = (float) 0.0;
            if (deltaZ < NOISE)
                deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            if (deltaX > deltaY) {
                // Horizontal shake
                // do something here if you like


            } else if (deltaY > deltaX) {
                // Vertical shake
                // do something here if you like
                stepsCount = stepsCount + 1;
                if (stepsCount > 0) {steps.setText(String.valueOf(stepsCount));}

            } else if ((deltaZ > deltaX) && (deltaZ > deltaY)) {
                // Z shake
                stepsCount = stepsCount + 1;
                if (stepsCount > 0) {steps.setText(String.valueOf(stepsCount));}


                // Just for indication purpose, I have added vibrate function
                // whenever our count moves past multiple of 10
                //if ((stepsCount % 10) == 0) {Util.Vibrate(this, 100);}
            } else {
                // no shake detected
            }
        }
*/