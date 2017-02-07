package com.sdaacademy.zientara.rafal.sensorcolor;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

public class ColorConverter {
    private static final int RED = Color.rgb(200, 0, 0);
    private static final int WHITE = Color.rgb(255, 255, 255);
    private float maxValue = 0;

    public int sensor2color(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            return get3colorFrom3Dimension(event);
        } else if (mySensor.getType() == Sensor.TYPE_LIGHT) {
            return WHITE;
        } else if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            return get3colorFrom3Dimension(event);
        } else {
            Log.w(getClass().getSimpleName(), "Not supported sensor color!");
            return RED;
        }
    }

    private int get3colorFrom3Dimension(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        maxValue = Math.max(maxValue, Math.abs(x));
        maxValue = Math.max(maxValue, Math.abs(y));
        maxValue = Math.max(maxValue, Math.abs(z));
        x += maxValue;
        y += maxValue;
        z += maxValue;

        int r = (int) (255 * x / (maxValue * 2));
        int g = (int) (255 * y / (maxValue * 2));
        int b = (int) (255 * z / (maxValue * 2));
        Log.d(getClass().getSimpleName(), String.format("r = %d, g = %d, b = %d", r, g, b));
        return Color.rgb(r, g, b);
    }

    public static int getNegativeColor(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, 255 - red, 255 - green, 255 - blue);
    }
}
