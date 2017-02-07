package com.sdaacademy.zientara.rafal.drugaaplikacja;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdaacademy.zientara.rafal.sensorcolor.ColorConverter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagneticField;
    private Sensor mLight;

    private ColorConverter accelerometerColorConverter = new ColorConverter();
    private ColorConverter magneticFieldColorConverter = new ColorConverter();
    private ColorConverter lightColorConverter = new ColorConverter();

    @BindView(R.id.parametresText)
    public TextView parametersText;

    @BindView(R.id.accelerometerText)
    public TextView accelerometerText;

    @BindView(R.id.magneticFieldText)
    public TextView magneticFieldText;

    @BindView(R.id.lightText)
    public TextView lightText;

    @BindView(R.id.activity_main)
    public LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        parametersText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccelerometer != null)
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (mMagneticField != null)
            mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        if (mLight != null)
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleAccelerometer(event);
        } else if (mySensor.getType() == Sensor.TYPE_LIGHT) {
            handleLightSensor(event);
        } else if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            handleMagneticFieldSensor(event);
        }
    }

    private void handleMagneticFieldSensor(SensorEvent event) {
        float field = event.values[0];
        magneticFieldText.setText(String.format("%s\n %f", getString(R.string.magnetic_field), field));

        int color = magneticFieldColorConverter.sensor2color(event);
        magneticFieldText.setBackgroundColor(color);
        magneticFieldText.setTextColor(ColorConverter.getNegativeColor(color));
    }

    private void handleLightSensor(SensorEvent event) {
        float light = event.values[0];
        lightText.setText(String.format("%s\n %f lx", getString(R.string.light_sensor), light));
        lightText.setBackgroundColor(lightColorConverter.sensor2color(event));
    }

    private void handleAccelerometer(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        String s = String.format("x = %f, y = %f, z = %f", x, y, z);
        Log.d(getClass().getSimpleName(), s);
        Log.d("LOL", s);
        parametersText.setText(s);
        root.setBackgroundColor(accelerometerColorConverter.sensor2color(event));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
