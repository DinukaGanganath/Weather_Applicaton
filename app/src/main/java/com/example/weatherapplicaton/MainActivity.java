package com.example.weatherapplicaton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ArrayList weatherList;
    ListView listView;
    HttpURLConnection connection;
    BufferedReader reader;
    String inputString, description, Temp, TempF;
    String urlTem;
    int Unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherList = new ArrayList<>();
        listView = findViewById(R.id.List);

        WeatherData weather = new WeatherData();
        weather.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DayWeatherShow.class);
                Bundle bundle = new Bundle();
                bundle.putString("a", inputString);
                bundle.putString("b", Integer.toString(i));
                bundle.putString("TF", Integer.toString(Unit));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.About:
                Toast.makeText(this,"This is a weather map", Toast.LENGTH_LONG).show();
                return true;
            case R.id.Settings:
                Intent settingIntent = new Intent(this, MainActivity2.class);
                startActivity(settingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class WeatherData extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            Intent intent2 = getIntent();
            String City = intent2.getStringExtra("c");

            if(City == null){
                urlTem =  "https://api.openweathermap.org/data/2.5/forecast/daily?q=colombo&cnt=7&appid=a18b978603316d47c572d98d52a420f6";
            }else{
                urlTem = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+City+"&cnt=7&appid=a18b978603316d47c572d98d52a420f6";
            }

            try {

                final String API_Link = urlTem;
                URL url = new URL(API_Link);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = null;
                try {
                    inputStream = connection.getInputStream();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Please enter valid city name...!", Toast.LENGTH_LONG).show();
                    Intent intDialog = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intDialog);
                }
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null){
                    return null;
                }
                InputStreamReader isREader = new InputStreamReader(inputStream);
                reader = new BufferedReader(isREader);

                String line;

                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length()== 0){
                    return  null;
                }
                inputString = buffer.toString();
            }  catch (IOException e) {
                Log.e("Hi","Error", e);
                return null;
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("Hi","Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject json = new JSONObject(inputString);
                JSONArray list = json.getJSONArray("list");

                for(int p = 0  ; p < list.length() ; p++){
                    JSONObject jsonObject = list.getJSONObject(p);
                    JSONObject temp = jsonObject.getJSONObject("temp");
                    JSONArray weather = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weather.getJSONObject(0);
                    description =weatherObj.getString("description");
                    String icon =weatherObj.getString("icon");
                    double temperature = temp.getDouble("day") - 273.15;
                    Temp = new DecimalFormat("0.00").format(temperature);
                    TempF = new DecimalFormat("0.00").format(convertFahrenheit(temperature));

                    HashMap<String, Object > weatherH = new HashMap<>();
                    weatherH.put("Description", description);
                    weatherH.put("Temperature", Temp);
                    weatherH.put("Day", this.getDate()[p] );
                    weatherH.put("TempF", TempF);
                    this.ImageSelection(weatherH, icon);

                    weatherList.add(weatherH);
                }
                System.out.println(weatherList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intentTem = getIntent();
            Unit = intentTem.getIntExtra("T", 0);

            if(Unit == 0) {
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this,
                        weatherList,
                        R.layout.row_layout,
                        new String[]{"Description", "Temperature", "Day", "Icon"},
                        new int[]{R.id.description, R.id.temperature, R.id.day, R.id.Image});
                listView.setAdapter(adapter);
            }else {
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this,
                        weatherList,
                        R.layout.row_layout,
                        new String[]{"Description", "TempF", "Day", "Icon"},
                        new int[]{R.id.description, R.id.temperature, R.id.day, R.id.Image});
                listView.setAdapter(adapter);
            }

        }

        public String[] getDate(){

            String[] day_list = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
            String[] Rearranged = new String[7];
            int pos = 0;
            int  start = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

            for(int y = start; y<day_list.length; y++){ Rearranged[pos] = day_list[y];pos++; }
            for(int z = 0; z < start; z++){ Rearranged[pos] = day_list[z];pos++; }

            return Rearranged;

        }

        public void ImageSelection(HashMap weatherH, String icon){
            switch (icon){
                case "01d": weatherH.put("Icon",R.drawable.i01d);break;
                case "01n": weatherH.put("Icon",R.drawable.i01n);break;
                case "02d": weatherH.put("Icon",R.drawable.i02d);break;
                case "02n": weatherH.put("Icon",R.drawable.i02n);break;
                case "03d": weatherH.put("Icon",R.drawable.i03d);break;
                case "03n": weatherH.put("Icon",R.drawable.i03n);break;
                case "04d": weatherH.put("Icon",R.drawable.i04d);break;
                case "04n": weatherH.put("Icon",R.drawable.i04n);break;
                case "09d": weatherH.put("Icon",R.drawable.i09d);break;
                case "09n": weatherH.put("Icon",R.drawable.i09n);break;
                case "10d": weatherH.put("Icon",R.drawable.i10d);break;
                case "10n": weatherH.put("Icon",R.drawable.i10n);break;
                case "11d": weatherH.put("Icon",R.drawable.i11d);break;
                case "11n": weatherH.put("Icon",R.drawable.i11n);break;
            }
        }

        public double convertFahrenheit(double cel){
            double ans = ((cel*9)/5) + 32;
            return ans;
        }

    }


}