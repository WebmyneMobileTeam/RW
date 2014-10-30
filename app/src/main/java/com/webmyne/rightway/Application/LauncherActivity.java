package com.webmyne.rightway.Application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.webmyne.rightway.Login.RegistrationActivity;
import com.webmyne.rightway.R;

import java.io.IOException;


public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a splash screen.
     */
    public static class PlaceholderFragment extends Fragment {

        private String SENDER_ID = "APA91bF7Mu1e1M8nKWKYeOODcPJm0coqF6TEEEvl-bb6TDmEBvbYU5m9y0IE1Olnt0ZyQM4p3ccNhHx6nUUnKE91g9QXBDirFz1HxtAw-CDtufxiLPHaxRH-ll37FnnsHD5KAmhlw6QulKKzhKmZoBxzWmNdtR9qHdETiv9epIwQ5inqLqOuC5Q";

        GoogleCloudMessaging gcm;
        String regid;


       // String PROJECT_NUMBER = "92884720384";
       String PROJECT_NUMBER = "766031645889";

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_launcher, container, false);
            return rootView;
        }

        // Check Internet Connection
        public  boolean isConnected() {

            ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            return  isConnected;
        }

        @Override
        public void onResume() {
            super.onResume();

            if(isConnected()) { // Check Internet Coneection Availability

                SharedPreferences preferences = getActivity().getSharedPreferences("is_registered", MODE_PRIVATE);
                boolean isRegistered = preferences.getBoolean("registration", false);

                if (isRegistered==false) { // get GCM Id and post IMEI Number
                    getRegId();
                } else {    // show home screen
                    new CountDownTimer(2500, 1000) {
                        @Override
                        public void onFinish() {
                            try {
                                Intent i = new Intent(getActivity(), DrawerActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                    }.start();

                }
            } else {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Error");
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                alert.show();
            }
        }

        public void getRegId(){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(getActivity());
                        }
                        regid = gcm.register(PROJECT_NUMBER);

                        //..........................................

                        String msg = "";
                        try {
                            Bundle data = new Bundle();
                            data.putString("my_message", "Hello World");
                            data.putString("my_action",
                                    "com.google.android.gcm.demo.app.ECHO_NOW");

                            String id = Integer.toString(1);
                            gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                            msg = "Sent message";
                        } catch (IOException ex) {
                            msg = "Error :" + ex.getMessage();
                        }

                        //..........................................

                        Log.e("GCM ID :", regid);

                        if(regid==null || regid==""){

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Error");
                            alert.setMessage("Internal Server Error");
                            alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getRegId();
                                    dialog.dismiss();
                                }
                            });
                            alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getActivity().finish();
                                }
                            });
                            alert.show();
                        } else {
                            Intent i = new Intent(getActivity(), RegistrationActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } // end of getRegId
    } // end of fragment


}
