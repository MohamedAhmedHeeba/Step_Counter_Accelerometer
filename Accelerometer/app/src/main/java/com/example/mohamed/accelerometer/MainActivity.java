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

public class MainActivity extends AppCompatActivity implements SensorEventListener {


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
        show = (Button) findViewById(R.id.show);

        // Initialize Accelerometer sensor
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        list = new ArrayList<Double>();

        show.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                StatisticsUtil su = new StatisticsUtil();
                double mean = su.findMean(list);
                double std = su.standardDeviation(list, mean);
                int stepsNumber = su.finAllPeaks(list, std);
                steps.setText("#Steps: " + stepsNumber);
            }

        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];
        acceleration.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
        // calculate the magnitude mag^2 = x^2 + y^2 + z^2 and add mag to the list
        // we deal with mag due to count steps in all directions as magnitude neglects directions.
        double mag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(y, 2));
        list.add(mag);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}