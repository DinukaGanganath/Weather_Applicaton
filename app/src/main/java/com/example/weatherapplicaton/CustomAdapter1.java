package com.example.weatherapplicaton;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter1 extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] mainStr;
    private final String[] subStr;

    public CustomAdapter1(Activity context, String[] mainStr, String[] subStr){
        super(context, R.layout.activity_main2,mainStr);

        this.context = context;
        this.mainStr = mainStr;
        this.subStr = subStr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.row_layout_2, null, true);
        TextView mainStrtxt = (TextView) rowView.findViewById(R.id.MainString);
        TextView subStrtxt = (TextView) rowView.findViewById(R.id.SubString);

        mainStrtxt.setText(mainStr[position]);
        subStrtxt.setText(subStr[position]);

        return rowView;
    }
}

