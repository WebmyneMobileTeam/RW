package com.webmyne.rightway.CustomComponents;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dhruvil on 28-10-2014.
 */
public class FormValidator {


    private ResultValidationListner listner;
    private View defectedView;

    public FormValidator(ResultValidationListner lis) {
        this.listner = lis;

    }


    public synchronized final FormValidator validate(ArrayList<? extends View> arrayList) {

        if (startValidating(arrayList) == true) {

            listner.complete();
        } else {

            if (defectedView != null) {

                String error = null;

                if (defectedView instanceof EditText) {
                    error = ((EditText) defectedView).getHint().toString();
                } else if (defectedView instanceof TextView) {
                    error = ((TextView) defectedView).getHint().toString();
                }

                listner.error(error);

            }


        }


        return this;
    }



    public boolean startValidating(ArrayList<? extends View> arrayList){

        boolean valid = false;

        for(int i=0;i<arrayList.size();i++){

            View v = arrayList.get(i);

            if(v instanceof EditText){

                EditText ed = (EditText)v;
                if(ed.getText().toString() !=null && ed.getText().length()>0){

                    valid = true;
                    continue;

                }else{

                    defectedView = ed;
                    valid = false;
                    return valid;
                }



            }else if(v instanceof TextView){

                TextView tv = (TextView)v;

                if(tv.getText().toString() !=null && tv.getText().length()>0){

                    valid = true;
                    continue;

                }else{


                    defectedView = tv;
                    valid = false;
                    return valid;
                }

            }



        }


        return  valid;
    }





    public static interface  ResultValidationListner{

        public void complete();
        public void error(String error);

    }


}
