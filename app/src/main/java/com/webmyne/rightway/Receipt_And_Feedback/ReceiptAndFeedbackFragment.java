package com.webmyne.rightway.Receipt_And_Feedback;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.webmyne.rightway.Application.MyApplication;
import com.webmyne.rightway.Bookings.Trip;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.CustomComponents.ListDialog;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.ResponseMessage;
import com.webmyne.rightway.MyBooking.MyBookingFragment;
import com.webmyne.rightway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptAndFeedbackFragment extends Fragment {

    private ProgressDialog progressDialog;
    TextView txtTripComplete,txtDriverRatting;
    EditText txtDriverComments;
    ArrayList<String> dateSelectionArray=new ArrayList<String>();
    public static ReceiptAndFeedbackFragment newInstance(String param1, String param2) {
        ReceiptAndFeedbackFragment fragment = new ReceiptAndFeedbackFragment();

        return fragment;
    }

    public ReceiptAndFeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateSelectionArray.add("Cash");
        dateSelectionArray.add("Credit Card");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_receipt_and_feedback, container, false);
        txtTripComplete=(TextView)convertView.findViewById(R.id.txtTripComplete);
        txtDriverRatting=(TextView)convertView.findViewById(R.id.txtDriverRatting);
        txtDriverComments=(EditText)convertView.findViewById(R.id.txtDriverComments);
        txtTripComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completTrip();
            }
        });
        return convertView;
    }

    public void completTrip(){

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                JSONObject driverStatusObject = new JSONObject();
                try {

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "current_trip_details", 0);
                    Trip currentTrip=complexPreferences.getObject("current_trip_details", Trip.class);
                    driverStatusObject.put("DriverID", currentTrip.DriverID+"");
                    driverStatusObject.put("DriverComments", txtDriverComments.getText().toString()+"");
                    driverStatusObject.put("DriverRattings", txtDriverRatting.getText().toString()+"");
                    driverStatusObject.put("TripID", currentTrip.TripID+"");
                    Log.e("driverStatusObject: ", driverStatusObject + "");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.TripSuccessful, driverStatusObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jobj) {
                        String response = jobj.toString();
                        Log.e("response continue: ", response + "");
                        ResponseMessage responseMessage = new GsonBuilder().create().fromJson(response, ResponseMessage.class);
                        Log.e("Response: ",responseMessage.Response+"");


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error response: ",error+"");
                    }
                });
                MyApplication.getInstance().addToRequestQueue(req);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Toast.makeText(getActivity(), "Trip completed Successfully", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = manager.beginTransaction();
                MyBookingFragment myOrdersFragment = MyBookingFragment.newInstance("", "");
                if (manager.findFragmentByTag("MyBooking") == null) {
                    ft.replace(R.id.main_content, myOrdersFragment,"MyBooking").commit();
                }

            }
        }.execute();


    }


}
