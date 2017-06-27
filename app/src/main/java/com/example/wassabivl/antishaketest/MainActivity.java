package com.example.wassabivl.antishaketest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final float Shake_Gravity_Threshold = 2.7f; //set a threshold limit not to activate the anitshake due to gravity
    SensorManager sensorManager;
    FileOutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //to modify the grid Layout programmatically
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setRowOrderPreserved(false);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        final float x = (int) Math.pow(event.values[0], 2);
        final float y = (int) Math.pow(event.values[1], 2);
        float z = event.values[2];

        float gx = x / SensorManager.GRAVITY_EARTH;
        float gy = y / SensorManager.GRAVITY_EARTH;
        float gz = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gx*gx+gy*gy+gz*gz);
        if (gForce>Shake_Gravity_Threshold) {
            new Thread(new Runnable() {
            @Override
            public void run() { // Handles rendering the live sensor data
                for (int i = 0; i < 1; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int l = (92-Math.round(x));
                            int u = (50-Math.round(y));
                            int r = (92+Math.round(x));
                            int d = (50+Math.round(y));
                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                           // imageView.setPadding(l,u,r,d);
                            ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
                            marginParams.setMargins(l, u, r, d);
                            ViewGroup.MarginLayoutParams lFooter = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                            lFooter.bottomMargin = d;
                            lFooter.leftMargin = l;
                            lFooter.rightMargin = r;
                            lFooter.topMargin=u;
                            imageView.setLayoutParams(lFooter);
                        }
                    });
                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        Log.e("graph1", e.toString());
                    }
                }
            }
        }).start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void button1(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("1");}
    public void button2(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("2");}
    public void button3(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("3");}
    public void button4(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("4");}
    public void button5(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("5");}
    public void button6(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("6");}
    public void button7(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("7");}
    public void button8(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("8");}
    public void buttonE(View v) throws IOException {
        //need to call to change the picture after this button is pressed
        TextView textView= (TextView) findViewById(R.id.editText);
        textView.append(" /n ");
        try {
            outputStream = openFileOutput("antishake.txt", Context.MODE_PRIVATE);
            outputStream.write(Integer.parseInt(textView.getText().toString()));
            outputStream.close();
//                File root = new File("");
//                File gpxfile = new File(root, "antishake.txt");
//                FileWriter writer = new FileWriter(gpxfile);
//                writer.append(textView.getText().toString());
//                writer.flush();
//                writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }
}
