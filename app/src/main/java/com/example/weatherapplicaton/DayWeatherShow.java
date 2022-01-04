package com.example.weatherapplicaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class DayWeatherShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_weather_show);

        int Day, Month, Year;

        Bundle bundle = getIntent().getExtras();
        String JsonStr = bundle.getString("a");
        String DayNoStr = bundle.getString("b");
        String UnitStr = bundle.getString("TF");
        int DayNo = Integer.parseInt(DayNoStr);
        int Unit = Integer.parseInt(UnitStr);

        TextView Date = (TextView) findViewById(R.id.Date1);
        TextView City = (TextView) findViewById(R.id.City1);
        TextView Description = (TextView) findViewById(R.id.Description1);
        TextView Humidity = (TextView) findViewById(R.id.Humidity1);
        TextView Tempera = (TextView) findViewById(R.id.Temperature1);
        TextView Unittxt = (TextView) findViewById(R.id.unit);
        ImageView WeatherIcon = (ImageView) findViewById(R.id.Weathericon1);

        Date current = Calendar.getInstance().getTime();
        String formatted = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current);

        String[] splitDate = formatted.split("/");

        Month = Integer.parseInt(splitDate[0].trim());
        Day = Integer.parseInt(splitDate[1].trim()) + DayNo;
        Year = Integer.parseInt(splitDate[2].trim()) + 2000;

        if ((Month == 1|Month == 3|Month == 5|Month == 7|Month == 8|Month == 10) && Day>31){
            Day -= 31;
            Month+=1;
        }else if ((Month == 4|Month == 6|Month == 9|Month == 11) && Day>30){
            Day -= 30;
            Month+=1;
        }else if (Month ==2){
            if ((((Year % 4 == 0 )&& (Year % 100 != 0)) || (Year % 400 ==0))&& (Day > 29)){
                Day-= 29;
                Month+=1;
            }else if((Year%4 != 0)||((Year %100 == 0)&&(Year%400 != 0))&&(Day > 28)){
                Day-=28;
                Month+=1;
            }
        }else if (Month ==12 && Day>31){
            Day -= 31;
            Month = 1;
            Year += 1;
        }else{}

        String Daystr = Integer.toString(Day);
        String Monstr = Integer.toString(Month);
        String Yearstr = Integer.toString(Year);


        try {
            JSONObject JsonObject = new JSONObject(JsonStr);
            JSONObject JsonCityObject = JsonObject.getJSONObject("city");
            String city = JsonCityObject.getString("name");
            String country = JsonCityObject.getString("country");
            JSONArray dayList = JsonObject.getJSONArray("list");

            JSONObject DayObj = dayList.getJSONObject(DayNo);
            JSONObject temp = DayObj.getJSONObject("temp");
            String temperature = new DecimalFormat("0.0").format(temp.getDouble("day")-273.15);
            String humidity = new DecimalFormat("0.0").format(DayObj.getDouble("humidity"));
            JSONArray weatherArr = DayObj.getJSONArray("weather");
            JSONObject weatherObj = weatherArr.getJSONObject(0);
            String description = weatherObj.getString("description");
            String icon = weatherObj.getString("icon");
            String tempF = new DecimalFormat("0.0").format(convertFahrenheit(Double.parseDouble(temperature)));

            City.setText(city +" , "+ country);
            Description.setText(description);
            Humidity.setText("Humidity : " + humidity + "%");
            Picasso.get().load("http://openweathermap.org/img/w/"+icon+".png").into(WeatherIcon);

            if (Day< 10 && Month <10) {
                Date.setText(Year + " - 0" + Month + " - 0" + Day);
            } else if(Day< 10 && Month >10){
                Date.setText(Year + " - " + Month + " - 0" + Day);
            }else if(Day> 10 && Month <10){
                Date.setText(Year + " - 0" + Month + " - " + Day);
            }else{
                Date.setText(Year + " - " + Month + " - " + Day);
            }

            if (Unit == 0){
                Tempera.setText(temperature);
                Unittxt.setText("C");
            }else{
                Tempera.setText(tempF);
                Unittxt.setText("F");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public double convertFahrenheit(double cel){
        double ans = ((cel*9)/5) + 32;
        return ans;
    }
}