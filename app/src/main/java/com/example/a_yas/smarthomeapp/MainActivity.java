package com.example.a_yas.smarthomeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String SENSOR = "Sensor Info";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("sensorData");
    private static final String FIREBASE = "Firebase";
    private static final String TEMP = "temp";
    private static final String HUE = "hue";
    private static final String GAS = "gas";
    private static final String GAS2 = "gas2";
    private static final String LPG = "lpg";
    private static final String OTHER = "other";

    //Gauges for sensors
    private GradientCircleGauge tempGauge;
    private GradientCircleGauge hueGauge;
    private GradientCircleGaugeLarge gasGuage;
    private GradientCircleGaugeLarge gas2Guage;
    private GradientCircleGaugeLarge lpgGuage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        hueGauge = (GradientCircleGauge) findViewById(R.id.hue);
        tempGauge = (GradientCircleGauge) findViewById(R.id.temp);
        gasGuage = (GradientCircleGaugeLarge) findViewById(R.id.gas);
        gas2Guage = (GradientCircleGaugeLarge) findViewById(R.id.gas2);
        lpgGuage = (GradientCircleGaugeLarge) findViewById(R.id.lpg);
        hueGauge.setGaugeLevel(0,"%");
        tempGauge.setGaugeLevel(0,"");
        gasGuage.setGaugeLevel(0,"ppm");
        gas2Guage.setGaugeLevel(0,"ppm");
        lpgGuage.setGaugeLevel(0,"ppm");
        this.getDataFromFirebaseAndUpdateInfo();

    }

    void getDataFromFirebaseAndUpdateInfo() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                Object value = dataSnapshot.getValue(Object.class);
                this.updateSensorInfo((HashMap) value);

                Log.d(FIREBASE, "Value is updated ");


            }

            public void updateSensorInfo(HashMap value) {
                if (value != null) {
                    Iterator itr = value.keySet().iterator();
                    while (itr.hasNext()) {
                        HashMap child = (HashMap) value.get(itr.next());
                        if (child != null) {
                            String sensorName = (String) child.get("sensor");
                            String timestmp = String.valueOf(child.get("time"));
                            String s_value = String.valueOf(child.get("sensor_value"));

                            switch (sensorName) {
                                case "Temperature":
                                    updateGaugeLevels(TEMP, timestmp, s_value);
                                    break;
                                case "Humidity":
                                    updateGaugeLevels(HUE, timestmp, s_value);
                                    break;
                                case "GAS":
                                    updateGaugeLevels(GAS, timestmp, s_value);
                                    break;
                                case "GAS2":
                                    updateGaugeLevels(GAS2, timestmp, s_value);
                                    break;
                                case "LPG":
                                    updateGaugeLevels(LPG, timestmp, s_value);
                                    break;
                                default:
                                    break;
                            }

                            Log.d(SENSOR, "\nSensor : " + sensorName + "\nValue : " + s_value + "\nLast Updated : " + timestmp + "\n");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(FIREBASE, "Failed to read value.", error.toException());
            }
        });
    }


    void updateGaugeLevels(String code, String time, String value) {
        switch (code) {
            case TEMP:
                tempGauge.setGaugeLevel(Integer.valueOf(value), " ");
                tempGauge.setAdditionalLabel(time);
                break;
            case HUE:
                hueGauge.setGaugeLevel(Integer.valueOf(value), "%");
                hueGauge.setAdditionalLabel(time);
                break;
            case GAS:
                gasGuage.setGaugeLevel(Integer.valueOf(value), "ppm");
                gasGuage.setAdditionalLabel(time);
                break;
            case GAS2:
                gas2Guage.setGaugeLevel(Integer.valueOf(value), "ppm");
                gas2Guage.setAdditionalLabel(time);
                break;
            case LPG:
                lpgGuage.setGaugeLevel(Integer.valueOf(value), "ppm");
                lpgGuage.setAdditionalLabel(time);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //HOLDWORK = true;
            Intent intent = new Intent(this, VoiceRecognitionActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
      //  HOLDWORK = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        HOLDWORK = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        HOLDWORK = true;
    }
}
