package com.example.roryeagan.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class addClass extends AppCompatActivity {


    EditText className;

    Button inputLocation;
    String address = "";
    LatLng location;

    CheckBox mondayCheck;
    CheckBox tuesdayCheck;
    CheckBox wednesdayCheck;
    CheckBox thursdayCheck;
    CheckBox fridayCheck;

    SeekBar mondaySeek;
    SeekBar tuesdaySeek;
    SeekBar wednesdaySeek;
    SeekBar thursdaySeek;
    SeekBar fridaySeek;

    TextView mondayTime;
    TextView tuesdayTime;
    TextView wednesdayTime;
    TextView thursdayTime;
    TextView fridayTime;

    ArrayList<CheckBox> checkArray = new ArrayList<>();
    ArrayList<SeekBar> seekArray = new ArrayList<>();
    ArrayList<TextView> timeArray = new ArrayList<>();

    TextView addressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_class);

        className = (EditText) findViewById(R.id.inputClass);

        mondayCheck = (CheckBox) findViewById(R.id.mondayCheck);
        tuesdayCheck = (CheckBox) findViewById(R.id.tuesdayCheck);
        wednesdayCheck = (CheckBox) findViewById(R.id.wednesdayCheck);
        thursdayCheck = (CheckBox) findViewById(R.id.thursdayCheck);
        fridayCheck = (CheckBox) findViewById(R.id.fridayCheck);

        checkArray.add(mondayCheck);
        checkArray.add(tuesdayCheck);
        checkArray.add(wednesdayCheck);
        checkArray.add(thursdayCheck);
        checkArray.add(fridayCheck);

        mondaySeek = (SeekBar) findViewById(R.id.mondaySeek);
        tuesdaySeek = (SeekBar) findViewById(R.id.tuesdaySeek);
        wednesdaySeek = (SeekBar) findViewById(R.id.wednesdaySeek);
        thursdaySeek = (SeekBar) findViewById(R.id.thursdaySeek);
        fridaySeek = (SeekBar) findViewById(R.id.fridaySeek);

        seekArray.add(mondaySeek);
        seekArray.add(tuesdaySeek);
        seekArray.add(wednesdaySeek);
        seekArray.add(thursdaySeek);
        seekArray.add(fridaySeek);

        mondayTime = (TextView) findViewById(R.id.mondayTime);
        tuesdayTime = (TextView) findViewById(R.id.tuesdayTime);
        wednesdayTime = (TextView) findViewById(R.id.wednesdayTime);
        thursdayTime = (TextView) findViewById(R.id.thursdayTime);
        fridayTime = (TextView) findViewById(R.id.fridayTime);

        timeArray.add(mondayTime);
        timeArray.add(tuesdayTime);
        timeArray.add(wednesdayTime);
        timeArray.add(thursdayTime);
        timeArray.add(fridayTime);

        for(int i = 0; i < seekArray.size(); i++){
            timeArray.get(i).setVisibility(View.INVISIBLE);
            seekArray.get(i).setVisibility(View.INVISIBLE);
            checkArray.get(i).setOnClickListener(checkListener);
            seekArray.get(i).setOnSeekBarChangeListener(seekListener);
        }


        inputLocation = (Button) findViewById(R.id.inputLocation);
        addressText = (TextView) findViewById(R.id.address);
        //location = new LatLng(0,0);
    }//onCreate

    private View.OnClickListener checkListener = new View.OnClickListener() {
        public void onClick(View v) {
            for (int i = 0; i < checkArray.size(); i++) {
                if(checkArray.get(i).isChecked()){
                    seekArray.get(i).setVisibility(View.VISIBLE);
                    timeArray.get(i).setVisibility(View.VISIBLE);
                } else {
                    seekArray.get(i).setVisibility(View.INVISIBLE);
                    timeArray.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }//onClick
    };//View.OnClickListener

    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int hours;
            int minutes;
            String time;
            for(int i = 0; i < timeArray.size(); i++){
                hours = progress/4 + 8;
                if(hours > 12){
                    hours -= 12;
                }
                minutes = (progress % 4) * 15;
                time = "" + hours + ":" + minutes;
                if(progress >= 16){
                    time += "pm";
                } else {
                    time += "am";
                }
                if(seekBar == seekArray.get(i)){
                    timeArray.get(i).setText(time);
                }
            }
        }//onProgressChanged

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
    };//SeekBar.OnSeekBarChangeListener

    public void selectLocation(View v){
        Intent locationIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(locationIntent, 1);
    }//selectLocation

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                address = data.getStringExtra("address");
                addressText.setText(address);
                location = new LatLng(data.getDoubleExtra("lat", 0.0), data.getDoubleExtra("lng", 0.0));
            }
        }
    }//onActivityResult

    public void submit(View v){
        if(location == null){
            Toast.makeText(getApplicationContext(),"Please Select Location", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            for(int i = 0; i < checkArray.size(); i ++){
                if(checkArray.get(i).isChecked()){
                    intent.putExtra("day" + i, timeArray.get(i).getText());
                }
            }
            intent.putExtra("lat", location.latitude);
            intent.putExtra("lng", location.longitude);
            intent.putExtra("className", className.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }//submit
}

