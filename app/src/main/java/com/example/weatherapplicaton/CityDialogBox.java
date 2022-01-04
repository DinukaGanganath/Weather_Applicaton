package com.example.weatherapplicaton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CityDialogBox extends AppCompatDialogFragment {
    private EditText editCity;
    String cityName;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.city_dialog_box,null);


        builder.setView(view)
                .setTitle("City")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cityName = editCity.getText().toString().toLowerCase();
                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("c", cityName);
                        startActivity(intent2);

                    }
                });

        editCity = view.findViewById(R.id.UserCity);

        return builder.create();
    }

}
