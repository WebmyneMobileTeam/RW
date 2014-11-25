package com.webmyne.rightway.Receipt_And_Feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.RatingBar;
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
import com.webmyne.rightway.Model.API;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.ResponseMessage;
import com.webmyne.rightway.MyBooking.MyBookingFragment;
import com.webmyne.rightway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReceiptAndFeedbackFragment extends Fragment {

    private ProgressDialog progressDialog;
    private TextView txtTripComplete,txtDriverRatting,txtTripPaymentType,txtTripDriverName,txtTripPickupAddress,
            txtTripDropoffAddress,txtTripDistance,txtTripDate,txtTripFare,txtTripTip,txtTripFee,txtTotalAmount,paymentType;
    private RatingBar rattings;
    private EditText txtDriverComments;
    private Trip  currentTrip;
    private ArrayList<String> dateSelectionArray=new ArrayList<String>();

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

        initView(convertView);

        return convertView;
    }

    private void initView(View convertView){

        txtTripPaymentType=(TextView)convertView.findViewById(R.id.txtTripPaymentType);
        paymentType=(TextView)convertView.findViewById(R.id.paymentType);
        txtTripDriverName=(TextView)convertView.findViewById(R.id.txtTripDriverName);
        txtTripPickupAddress=(TextView)convertView.findViewById(R.id.txtTripPickupAddress);
        txtTripDropoffAddress=(TextView)convertView.findViewById(R.id.txtTripDropoffAddress);
        txtTripDistance=(TextView)convertView.findViewById(R.id.txtTripDistance);
        txtTripDate=(TextView)convertView.findViewById(R.id.txtTripDate);
        txtTripFare=(TextView)convertView.findViewById(R.id.txtTripFare);
        txtTripTip=(TextView)convertView.findViewById(R.id.txtTripTip);
        txtTripFee=(TextView)convertView.findViewById(R.id.txtTripFee);
        txtTotalAmount=(TextView)convertView.findViewById(R.id.txtTotalAmount);
        rattings=(RatingBar)convertView.findViewById(R.id.rattings);
        txtTripComplete=(TextView)convertView.findViewById(R.id.txtTripComplete);
        txtDriverRatting=(TextView)convertView.findViewById(R.id.txtDriverRatting);
        txtDriverComments=(EditText)convertView.findViewById(R.id.txtDriverComments);

        txtTripComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()==true) {
                    completTrip();
                } else {
                    Toast.makeText(getActivity(), "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rattings.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtDriverRatting.setText(String.valueOf(rating)+"");
            }
        });

    }

    public  boolean isConnected() {

        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }

    @Override
    public void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "current_trip_details", 0);
        currentTrip=complexPreferences.getObject("current_trip_details", Trip.class);

        if(currentTrip.PaymentType !=null){
            txtTripPaymentType.setVisibility(View.VISIBLE);
            paymentType.setVisibility(View.VISIBLE);
            txtTripPaymentType.setText(currentTrip.PaymentType+"");
        } else {
            txtTripPaymentType.setVisibility(View.GONE);
            paymentType.setVisibility(View.GONE);
        }

        txtTripDriverName.setText(currentTrip.DriverName+"");
        txtTripPickupAddress.setText(currentTrip.PickupAddress+"");
        txtTripDropoffAddress.setText(currentTrip.DropOffAddress+"");
        txtTripDistance.setText(currentTrip.TripDistance+" kms");
        txtTripDate.setText(getFormatedDate(currentTrip)+"");
        txtTripFare.setText("$ "+String.format("%.2f", Double.parseDouble(currentTrip.TripDistance)*0.6214*Double.parseDouble(currentTrip.TripFare)));
        txtTripTip.setText(currentTrip.TipPercentage+" %");
        txtTripFee.setText("$ "+currentTrip.TripFee+"");
        txtTotalAmount.setText(String.format("$ %.2f", getTotal(currentTrip))+"");

    }

    public void completTrip(){

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
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
                    driverStatusObject.put("isDriverFeedbackGiven", true);
                    Log.e("driverStatusObject: ", driverStatusObject + "");

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                Reader reader = API.callWebservicePost(AppConstants.TripSuccessful, driverStatusObject.toString());
                ResponseMessage responseMessage = new GsonBuilder().create().fromJson(reader, ResponseMessage.class);
                Log.e("responseMessage:",responseMessage.Response+"");
                handlePostData();

                return null;

            }

        }.execute();

    }
    public void handlePostData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                progressDialog.dismiss();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Toast.makeText(getActivity(), "Trip completed Successfully", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = manager.beginTransaction();
                MyBookingFragment myOrdersFragment = MyBookingFragment.newInstance("", "");

                if (manager.findFragmentByTag("MyBooking") == null) {
                    ft.replace(R.id.main_content, myOrdersFragment,"MyBooking").commit();
                }

            }
        });
    }

    public String getFormatedDate(Trip currentTrip) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        float dateinFloat = Float.parseFloat(currentTrip.TripDate);
        Date date = float2Date(dateinFloat);
        return  format.format(date);
    }

    public  java.util.Date float2Date(float nbSeconds) {
        java.util.Date date_origine;
        java.util.Calendar date = java.util.Calendar.getInstance();
        java.util.Calendar origine = java.util.Calendar.getInstance();
        origine.set(1970, Calendar.JANUARY, 1);
        date_origine = origine.getTime();
        date.setTime(date_origine);
        date.add(java.util.Calendar.SECOND, (int) nbSeconds);
        return date.getTime();
    }

    public double getTotal(Trip currentTrip) {
        Double total;
        String tripFareValue=String.format("%.2f", Double.parseDouble(currentTrip.TripDistance)*0.6214*Double.parseDouble(currentTrip.TripFare));
        if(Integer.parseInt(currentTrip.TipPercentage)>0){

            Double tip=((Double.parseDouble(tripFareValue)*Double.parseDouble(currentTrip.TipPercentage))/100);
            total= Double.parseDouble(tripFareValue)+tip;
        } else {
            total=Double.parseDouble(tripFareValue);
        }
        total=total+Double.parseDouble(currentTrip.TripFee);
        return total;
    }

}
