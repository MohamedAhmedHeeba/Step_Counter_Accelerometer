package com.example.mohamed.accelerometer;

import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

public class MainActivity extends AppCompatActivity implements SensorEventListener{


    private TextView acceleration;
    private TextView steps;
    private TextView steps2;



    private double mean;
    private SensorManager sm;
    private Sensor accelerometer;
    private boolean mInitialized; // used for initializing sensor only once

    private final float NOISE = (float) 2.0;
    private int stepsCount = 0;

    private double mLastX;
    private double mLastY;
    private double mLastZ;
    private List<Double> list;
    private Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acceleration = (TextView) findViewById(R.id.Acceleration);
        steps = (TextView) findViewById(R.id.Steps);
        steps2 = (TextView) findViewById(R.id.steps2);


        // Initialize Accelerometer sensor
        mInitialized = false;
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        this.list = new ArrayList<Double>();
        this.show = (Button)findViewById(R.id.button);
        this.show.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Do something in response to button click
                findMean();
                steps2.setText("#Steps: "+ finAllPeaks(devation()));

            }
        });
    }

    private void findMean(){
        double total = 0;

        for(int i = 0; i < this.list.size(); i++){
            total += this.list.get(i); // this is the calculation for summing up all the values

        }

        mean = total / this.list.size();


    }

    private double devation(){
        double sum = 0;
        for(int i = 0 ; i < this.list.size() ; i++){
            this.list.set(i, Math.pow(this.list.get(i) - mean, 2));
            sum += this.list.get(i);
        }
        return Math.sqrt(sum/this.list.size());


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        acceleration.setText("X: "+event.values[0]+"\nY: "+event.values[1]+"\nZ: "+event.values[2]);

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


        acceleration.setText("X: "+event.values[0]+"\nY: "+event.values[1]+"\nZ: "+event.values[2]);
        double mag = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2)
        + Math.pow(event.values[2], 2));
        list.add(mag);

    }

    private int finAllPeaks(double minPeak){
        int counter = 0;
        for(int i = 0 ; i < this.list.size() ; i++){
            if(i + 2 < this.list.size()){
                double one = this.list.get(i), two = this.list.get(i+1), three = this.list.get(i+2);
                if(one < two && two > three && two > minPeak){
                    counter++;
                }
            }
        }
        return counter;
    }
    private int findPeakUtil(int arr[], int low, int high, int n)
    {
        // Find index of middle element
        int mid = low + (high - low)/2;  /* (low + high)/2 */

        // Compare middle element with its neighbours (if neighbours
        // exist)
        if ((mid == 0 || arr[mid-1] <= arr[mid]) && (mid == n-1 ||
                arr[mid+1] <= arr[mid]))
            return mid;

            // If middle element is not peak and its left neighbor is
            // greater than it,then left half must have a peak element
        else if (mid > 0 && arr[mid-1] > arr[mid])
            return findPeakUtil(arr, low, (mid -1), n);

            // If middle element is not peak and its right neighbor
            // is greater than it, then right half must have a peak
            // element
        else return findPeakUtil(arr, (mid + 1), high, n);
    }

    // A wrapper over recursive function findPeakUtil()
    private int findPeak(int arr[], int n)
    {
        return findPeakUtil(arr, 0, n-1, n);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
