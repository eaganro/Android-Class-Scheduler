package com.example.roryeagan.finalproject;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ListView classList;

    ArrayList<String> classArray = new ArrayList<>();
    ArrayList<LatLng> locationArray = new ArrayList<>();
    ArrayList<String[]> timeArray = new ArrayList<>();
    //ArrayList<Boolean> alarmStatus = new ArrayList<>();
    //ArrayList<Integer> alarmBefore = new ArrayList<>();
    //ArrayList<PendingIntent[]> alarms = new ArrayList<>();

    ArrayAdapter<String> adapter;

    Button addButton;

    LatLng homeLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classList = (ListView) findViewById(R.id.classList);

        addButton = (Button) findViewById(R.id.addButton);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classArray);
        classList.setAdapter(adapter);


        classList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Intent classPage = new Intent(getApplicationContext(), classPage.class);
                classPage.putExtra("className", classArray.get(position));
                classPage.putExtra("classTimes", timeArray.get(position));
                classPage.putExtra("Dlat", locationArray.get(position).latitude);
                classPage.putExtra("Dlng", locationArray.get(position).longitude);
                classPage.putExtra("Olat", homeLocation.latitude);
                classPage.putExtra("Olng", homeLocation.longitude);
                classPage.putExtra("position", position);
                startActivityForResult(classPage, 2);
            }
        });

    }


    public void addClass(View v){
        if(homeLocation == null){
            Toast.makeText(getApplicationContext(),"Please Select Home Location", Toast.LENGTH_LONG).show();
        } else {
            Intent classIntent = new Intent(getApplicationContext(), addClass.class);
            startActivityForResult(classIntent, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                classArray.add(data.getStringExtra("className"));
                String[] classTimes = new String[5];
                for(int i = 0; i < 5; i++){
                    if(data.getStringExtra("day" + i) != null){
                        classTimes[i] = data.getStringExtra("day" + i);
                    } else {
                        classTimes[i] = "0";
                    }
                }
                timeArray.add(classTimes);
                LatLng newLoc = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lng", 0));
                locationArray.add(newLoc);
                adapter.notifyDataSetChanged();
                //alarmStatus.add(false);
                //alarmBefore.add(0);
                //PendingIntent[] intents = new PendingIntent[5];
                //alarms.add(intents);
;            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                //alarmStatus.set(data.getIntExtra("position", 0), data.getBooleanExtra("alarm", false));
                //alarmBefore.set(data.getIntExtra("position", 0), data.getIntExtra("time", 0));
                //if(data.getBooleanExtra("alarm", false)){
                //    setAlarm(data.getIntExtra("position", 0));
                //}
            }
        }
        if (requestCode == 3) {
            if(resultCode == RESULT_OK){
                homeLocation = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lng", 0));
            }
        }
    }

    public void setAlarm(int position){
        PendingIntent[] classAlarms = new PendingIntent[5];
        for(int i = 0; i < 5; i++){
            if(timeArray.get(position)[i] != "0"){

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                int hour = Integer.parseInt(timeArray.get(position)[i].split(":")[0]);
                //int minute = Integer.parseInt(timeArray.get(position)[i].split(":")[1]);

                calendar.set(Calendar.DAY_OF_WEEK, calendar.THURSDAY);
                //calendar.set(Calendar.HOUR_OF_DAY, hour);
                //calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 5);

                System.out.println("works");
            }
        }
        //alarms.set(position, classAlarms);
    }

    public void setHomeLocation(View v){
        Intent locationIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(locationIntent, 3);
    }
}
