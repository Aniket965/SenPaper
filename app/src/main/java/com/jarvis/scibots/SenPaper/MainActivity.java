package com.jarvis.scibots.SenPaper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private String TAG = "MAIN_ACTIVITY";
    private long captureTime;


    /*Declaring sensors to be used*/
    private Sensor mMagnetic;
    private Sensor mLight;
    private Sensor mAccelerations;
    private Sensor mProximity;



    /**
    * Arraylist for storing values of different sensors
    * generated in capture time
    * */
    private ArrayList<Float> LightValue;
    private ArrayList<Float> MagneticValues;
    private ArrayList<Float> AccelerationValuesX;
    private ArrayList<Float> AccelerationValuesY;
    private ArrayList<Float> ProximityValues;
    private float Max_magnitude_accelerometer;
    long lastDown;
    long keyPressedDuration ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.start, R.anim.end);
        setContentView(R.layout.activity_main);

        final TextView capture = (TextView)findViewById(R.id.CaptureButton);
        TextView androidExperiment =(TextView)findViewById(R.id.ae);

        final Typeface Pacifico= Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        androidExperiment.setTypeface(Pacifico);
        capture.setTypeface(Pacifico);
        /**
        * Checks and ask for the storage permission so that image can be saved in storage
        */
        isStoragePermissionGranted();
        
        /* 
        * Listens to the start button pressed and release 
        */
        capture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastDown = System.currentTimeMillis();
                    /*
                    * Activates the sensors when button is press down
                    */
                    mSensorManager.registerListener(MainActivity.this,mMagnetic,SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(MainActivity.this,mAccelerations,SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(MainActivity.this,mLight,SensorManager.SENSOR_DELAY_NORMAL);
                    mSensorManager.registerListener(MainActivity.this,mProximity,SensorManager.SENSOR_DELAY_NORMAL);
                   capture.setText("Stop");



                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    /*
                    * Finds the time interval for which button is presssed & 
                    * unregister the sensors 
                    */
                    keyPressedDuration = System.currentTimeMillis() - lastDown;
                    captureTime = keyPressedDuration/1000;
                    Log.d(TAG,captureTime+"");
                    if(captureTime ==  0) {
                        captureTime = 1;
                    }
                    capture.setText("Start");
                    captureTime = captureTime*3;
                    mSensorManager.unregisterListener(MainActivity.this);
                    ManipulateValues(AccelerationValuesX,AccelerationValuesY,MagneticValues,ProximityValues,LightValue);
                }
            return true;
            }
        });



        /*
        * Arraylists for storing respective data from sensors 
        */
        LightValue =  new ArrayList<>();
        MagneticValues = new ArrayList<>();
        AccelerationValuesX = new ArrayList<>();
        AccelerationValuesY = new ArrayList<>();
        ProximityValues = new ArrayList<>();


        mSensorManager = (SensorManager) getSystemService(MainActivity.this.SENSOR_SERVICE);
        /*
        * sets default sensors for device
        */
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAccelerations = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Max_magnitude_accelerometer =  mAccelerations.getMaximumRange();



    }




/*
* Manipulates the data recivied from the sensors and sends to wallpaper activity
*/

    private void ManipulateValues(ArrayList<Float> accelerationValuesX,
                                  ArrayList<Float> accelerationValuesY,
                                  ArrayList<Float> magneticValues,
                                  ArrayList<Float> proximityValues,
                                  ArrayList<Float> lightValues) {

        ArrayList<Integer> shapePostionX = new ArrayList<>();
        ArrayList<Integer> shapePostionY = new ArrayList<>();
        float averageDepth = 0;
        float AverageLight = 0 ;
        float AverageMagneticValues;
        int GroupSize = (int) (accelerationValuesX.size()/captureTime);
        averageDepth = FindAverage(proximityValues,0,proximityValues.size());
        averageDepth = (float) Math.floor(averageDepth);
        AverageLight = FindAverage(lightValues,0,lightValues.size());
        /*Gets Screen Width and height*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        /**
         * Divides the groups as same number of shapes
         * finds the postion of Respective shape
         * with respect to origin ...
         * */
        for(int i  = 0; i < captureTime ; i++){
            shapePostionX.add((int) ((width/2)+Math.floor((double)(FindAverage(accelerationValuesX,i*GroupSize,(i+1)*GroupSize )
                                /Max_magnitude_accelerometer)*10*(width/2))));
            shapePostionY.add((int) ((height/2)+ Math.floor((double)(FindAverage(accelerationValuesY,i*GroupSize,(i+1)*GroupSize )
                                /Max_magnitude_accelerometer)*10*(height/2))));
        }
        AverageMagneticValues = Math.abs(FindAverage(magneticValues,0,magneticValues.size()));
        Log.d(TAG,AverageMagneticValues + "");


        int BackgroundIndex = ManipulateBackgroundColor(AverageLight);

        Intent intent = new Intent(MainActivity.this,Wallpaper.class);
        Bundle bundle = new Bundle();
        bundle.putInt("BackgroundIndex",BackgroundIndex);
        bundle.putInt("TimeDiff",(int)captureTime);
        bundle.putInt("avgDepth",(int)averageDepth);
        bundle.putIntegerArrayList("XCordinates",shapePostionX);
        bundle.putIntegerArrayList("YCordinates",shapePostionY);
        bundle.putInt("magicColor",(int)AverageMagneticValues);
        intent.putExtras(bundle);

        if(isStoragePermissionGranted()){
        startActivity(intent);
        finish();
        }

        else {
            Toast.makeText(MainActivity.this,"Please add Permission",Toast.LENGTH_SHORT).show();
        }

    }

    private int ManipulateBackgroundColor(float averageLight) {
    if(averageLight < 5){
        Random r = new Random();
        int value = r.nextInt(19 - 14) + 14;
        Log.d(TAG,"Background Index  low light" +value);
        return value;
    }
    else if(averageLight > 40 ){
        Random r = new Random();
        int value = r.nextInt(6 - 0);
        Log.d(TAG,"Background Index  High light" +value);
        return value;
    }
    else {
        Random r = new Random();

        int value = r.nextInt(14 - 6) + 6;
        Log.d(TAG,"Background Index  Med light" +value);
        return value;

    }

    }

    private void drawWallpaper() {
    }

    /*Finds average of  list for given starting index and ending index*/
    public float FindAverage(ArrayList<Float> list,int left,int right){
        float average = 0;
        for(int i = left ; i < right; i++){
            average +=   list.get(i);
        }
        average /= (right - left);
        return average;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sense = sensorEvent.sensor;
        if(sense.getType() == Sensor.TYPE_ACCELEROMETER){
          AccelerationValuesX.add(sensorEvent.values[0]);
//            Log.d(TAG, sensorEvent.values[0]+ " ");
          AccelerationValuesY.add(sensorEvent.values[1]);

        }
        if(sense.getType() == Sensor.TYPE_MAGNETIC_FIELD){

            MagneticValues.add(sensorEvent.values[0]);


        }
        if(sense.getType() == Sensor.TYPE_LIGHT){

            LightValue.add(sensorEvent.values[0]);

        }

        if(sense.getType() == Sensor.TYPE_PROXIMITY){

            ProximityValues.add(sensorEvent.values[0]);

        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
