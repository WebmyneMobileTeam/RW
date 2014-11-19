package com.webmyne.rightway.CustomComponents;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.webmyne.rightway.R;

import java.util.ArrayList;


/**
 * Created by dhruvil on 26-08-2014.
 */
public  class ListDialogDrivers extends Dialog {


    private setSelectedListner listner;
    private View convertView;
    private Context ctx;
    private TextView txtTitle;
    private ListView listDialog;
    private FrameLayout parentDialog;


    public ListDialogDrivers(Context context, int theme) {
        super(context, theme);
        this.ctx = context;
        init();

    }

    public void setSelectedListner(setSelectedListner listner){
        this.listner = listner;
    }

    public void init() {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.view_dialog,null);
        setContentView(convertView);

        txtTitle = (TextView)convertView.findViewById(R.id.titleDialog);
        listDialog = (ListView)convertView.findViewById(R.id.listDialog);
        parentDialog=(FrameLayout)convertView.findViewById(R.id.parentDialog);
        parentDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }





    public void title(String title){

        txtTitle.setText(title);

    }

    public void setItems(ArrayList items){

        TypeAdapter adapter = new TypeAdapter(ctx,android.R.layout.simple_list_item_1,items);
        listDialog.setAdapter(adapter);

    }


    public class TypeAdapter extends ArrayAdapter {

        ArrayList list;

        public TypeAdapter(Context context, int resource, ArrayList objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final TextView textView = new TextView(ctx);
            String[] splited = list.get(position).toString().split(",");
            textView.setText(splited[0]+" "+splited[1]);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 8, 10, 8);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            textView.setTextColor(Color.BLACK);


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dismiss();
                    listner.selected(list.get(position).toString());


                }
            });

            return textView;
        }
    }

   public static interface setSelectedListner{

       public void selected(String value);
   }



}
