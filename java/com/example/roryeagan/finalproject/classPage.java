package com.example.roryeagan.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class classPage  extends AppCompatActivity {

    TextView className;

    TextView scheduleText;
    TextView leaveText;

    int position = 0;

    //GDirections directions = new GDirections ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_page);

        className = (TextView) findViewById(R.id.className);

        Intent in = getIntent();

        className.setText(in.getStringExtra("className"));
        position = in.getIntExtra("position", 0);


        scheduleText = (TextView) findViewById(R.id.scheduleText);
        String schedule = "Schedule:\n\n";
        String[] scheduleArray = in.getStringArrayExtra("classTimes");

        for(int i = 0; i < scheduleArray.length; i++){
            if(i == 0){
                schedule += "\t\tMonday: ";
            } else if(i == 1){
                schedule += "\t\tTuesday: ";
            } else if(i == 2){
                schedule += "\t\tWednesday: ";
            } else if(i == 3){
                schedule += "\t\tThursday: ";
            } else if(i == 4){
                schedule += "\t\tFriday: ";
            }

            if(scheduleArray[i].equals("0")){
                schedule += "None\n";
            } else{
                schedule += scheduleArray[i] + "\n";
            }
        }
        scheduleText.setText(schedule);

        DownloadTask task = new DownloadTask();
            String googleUrl = "http://maps.google.com/maps/api/directions/json?origin=";
            googleUrl += in.getDoubleExtra("Olat",0) +"," + in.getDoubleExtra("Olng",0);
            googleUrl += "&destination=" + in.getDoubleExtra("Dlat",0) +"," + in.getDoubleExtra("Dlng",0);
            googleUrl += "&mode=walking";
        String results = "";
        try {
            results = task.execute(googleUrl).get();
            //Log.i("Result: ", results);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //catch
        catch (ExecutionException e) {
            e.printStackTrace();
        }//catch

        String duration = "";
        try {
            JSONObject jsonObject = new JSONObject(results);
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            JSONObject route = routesArray.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            JSONObject durationObject = leg.getJSONObject("duration");
            duration = durationObject.getString("text");
        } catch (JSONException e) {
            Log.e("app", "JSON exception", e);
        }

        System.out.println(duration);
        int durationInt = Integer.parseInt(duration.split(" min")[0]);
        System.out.println(durationInt);

        leaveText = (TextView) findViewById(R.id.leaveText);
        String leave = "Leave At: \n\n";

        for(int i = 0; i < scheduleArray.length; i++){
            if(i == 0){
                leave += "\t\tMonday: ";
            } else if(i == 1){
                leave += "\t\tTuesday: ";
            } else if(i == 2){
                leave += "\t\tWednesday: ";
            } else if(i == 3){
                leave += "\t\tThursday: ";
            } else if(i == 4){
                leave += "\t\tFriday: ";
            }

            if(scheduleArray[i].equals("0")){
                leave += "None\n";
            } else{
                String timeString = scheduleArray[i];
                int hour = Integer.parseInt(timeString.split(":")[0]);
                String minString = timeString.split(":")[1];
                int min = 0;
                String ampm = "";
                if(minString.length() == 4){
                    min = Integer.parseInt(minString.substring(0,2));
                    ampm = minString.substring(2);
                }else{
                    min = Integer.parseInt(minString.substring(0,1));
                    ampm = minString.substring(1);
                }
                boolean flip = false;
                if(min - durationInt < 0){
                    min = 60 + (min-durationInt);
                    if(hour != 12){
                        hour --;
                    } else {
                        flip = true;
                        hour = 11;
                    }
                }else {
                    min -= durationInt;
                }

                leave +=  hour + ":";
                if(min < 10) {
                    leave += "0" + min;
                } else {
                    leave += min;
                }

                if (flip == true) {
                    leave += "am";
                } else {
                    leave += ampm;
                }
                leave += "\n";
            }
        }
        leaveText.setText(leave);

    }

    public class DownloadTask extends AsyncTask<String, Void, String> //URL, method, output
    {
        @Override
        protected String doInBackground(String... params) {

            String htmlContent = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    htmlContent += current;
                    data = reader.read();
                }
                return  htmlContent;
            }
            catch(Exception e ) {
                e.printStackTrace();
                return  "failed";
            }
        }//doInBackGround
    }//downloadTask


    public void submit(View v){
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }//submit
}