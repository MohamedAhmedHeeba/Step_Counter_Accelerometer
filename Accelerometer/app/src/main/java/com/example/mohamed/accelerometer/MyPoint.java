package com.example.mohamed.accelerometer;

/**
 * Created by mohamed on 02/03/17.
 */

public class MyPoint {
    private double x;
    private double y;
    private double z;

    public MyPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
