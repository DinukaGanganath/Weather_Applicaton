package com.example.weatherapplicaton;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    String MainStr[] ={"City", "Temperature Unit"};
    String SubStr[] = {"Select your city", "Select your Temperature Unit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CustomAdapter1 adapter1 = new CustomAdapter1(this, MainStr,SubStr);
        ListView listView2 = (ListView) findViewById(R.id.ListView2);
        listView2.setAdapter(adapter1);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    openDialog();
                }else if(i == 1){
                    openOptionDialog();
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong Touch", Toast.LENGTH_SHORT).show();
                }
            }

            private void openOptionDialog() {
                String[] items = { "Celsius", "Fahrenheit"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Temperature Unit")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selectTemperatureUnit = items[i];
                                Toast.makeText(MainActivity2.this, selectTemperatureUnit, Toast.LENGTH_LONG).show();

                                Intent intentTem = new Intent(MainActivity2.this, MainActivity.class);
                                intentTem.putExtra("T", i);
                                startActivity(intentTem);
//                                Intent intentTem2 = new Intent(MainActivity2.this, DayWeatherShow.class);
//                                intentTem2.putExtra("TF", i);
//                                startActivity(intentTem2);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    public void openDialog(){
        CityDialogBox cityDialogBox = new CityDialogBox();
        cityDialogBox.show(getSupportFragmentManager(),"City Dialog");
    }

}
