package com.webmyne.rightway.CustomComponents;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.webmyne.rightway.R;

import java.util.ArrayList;


/**
 * Created by dhruvil on 26-08-2014.
 */
public  class CustomTimePickerDialog extends Dialog {


    private setSelectedListner listner;
    private View convertView;
    private Context ctx;
    private TextView txtTitle;
    private TextView txxTimeOk;
    private TimePicker timePicker;
    private FrameLayout parentDialog;


    public CustomTimePickerDialog(Context context, int theme) {
        super(context, theme);
        this.ctx = context;
        init();

    }

    public void setSelectedListner(setSelectedListner listner){
        this.listner = listner;
    }

    public void init() {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.custom_dialog,null);
        setContentView(convertView);

        txtTitle = (TextView)convertView.findViewById(R.id.titleDialog);
        timePicker = (TimePicker)convertView.findViewById(R.id.timePicker);
        txxTimeOk = (TextView)convertView.findViewById(R.id.txxTimeOk);

        txxTimeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateTime(timePicker.getCurrentHour(),timePicker.getCurrentMinute());


            }
        });


        parentDialog=(FrameLayout)convertView.findViewById(R.id.parentDialog);
        parentDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



    }


    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        dismiss();
        listner.selected(aTime);

    }


    public void title(String title){

        txtTitle.setText(title);

    }



   public static interface setSelectedListner{

       public void selected(String value);
   }



}
