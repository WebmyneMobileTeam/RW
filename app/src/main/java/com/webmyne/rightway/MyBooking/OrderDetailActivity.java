package com.webmyne.rightway.MyBooking;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.webmyne.rightway.Application.BaseActivity;
import com.webmyne.rightway.Application.MyApplication;
import com.webmyne.rightway.Bookings.Trip;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.CustomComponents.CustomTypeface;
import com.webmyne.rightway.Model.ResponseMessage;
import com.webmyne.rightway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class OrderDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        txtHeader.setText("TRIP DETAILS");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return CustomTypeface.getInstance().createView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a order detail view.
     */

    public static class PlaceholderFragment extends Fragment {

        ProgressDialog progressDialog;
        Trip currentTrip;

        TextView currentTripDriverName, currentTripPickup, currentTripDropoff, currentTripPickupNote, currentTripDate, currentTripTime,
                currentTripDistance, txtTripStatus, currentTripPaymentType, currentTripFare, currentTripTip, currentTripFee,txtTotalAmount,
                txtCancelTrip,paymentType;
        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
            txtCancelTrip=(TextView)rootView.findViewById(R.id.txtCancelTrip);

                txtCancelTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cancelTrip();
                }
            });
            currentTripDriverName=(TextView)rootView.findViewById(R.id.currentTripDriverName);
            currentTripPickup=(TextView)rootView.findViewById(R.id.currentTripPickup);
            currentTripDropoff=(TextView)rootView.findViewById(R.id.currentTripDropoff);
            currentTripPickupNote=(TextView)rootView.findViewById(R.id.currentTripPickupNote);
            currentTripDate=(TextView)rootView.findViewById(R.id.currentTripDate);
            currentTripTime=(TextView)rootView.findViewById(R.id.currentTripTime);
            currentTripDistance=(TextView)rootView.findViewById(R.id.currentTripDistance);
            txtTripStatus=(TextView)rootView.findViewById(R.id.txtTripStatus);

            currentTripFare=(TextView)rootView.findViewById(R.id.currentTripFare);
            currentTripTip=(TextView)rootView.findViewById(R.id.currentTripTip);
            currentTripFee=(TextView)rootView.findViewById(R.id.currentTripFee);
            txtTotalAmount=(TextView)rootView.findViewById(R.id.txtTotalAmount);
            currentTripPaymentType=(TextView)rootView.findViewById(R.id.currentTripPaymentType);
            paymentType=(TextView)rootView.findViewById(R.id.paymentType);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "current_trip_details", 0);
            currentTrip=complexPreferences.getObject("current_trip_details", Trip.class);
            currentTripDriverName.setText(currentTrip.DriverName);
            currentTripPickup.setText(currentTrip.PickupAddress);
            currentTripDropoff.setText(currentTrip.DropOffAddress);
            currentTripPickupNote.setText(currentTrip.PickupNote);
            currentTripDate.setText(getFormatedDate());
            currentTripTime.setText(currentTrip.PickupTime);
            currentTripDistance.setText(currentTrip.TripDistance+" kms");
            if(currentTrip.PaymentType !=null) {
                currentTripPaymentType.setVisibility(View.VISIBLE);
                paymentType.setVisibility(View.VISIBLE);
                currentTripPaymentType.setText(currentTrip.PaymentType);
            } else {
                currentTripPaymentType.setVisibility(View.GONE);
                paymentType.setVisibility(View.GONE);
            }
            currentTripFare.setText("$ "+String.format("%.2f", Double.parseDouble(currentTrip.TripDistance)*0.6214*Double.parseDouble(currentTrip.TripFare)));
            currentTripTip.setText(currentTrip.TipPercentage+" %");
            currentTripFee.setText("$ "+currentTrip.TripFee);
            txtTotalAmount.setText(String.format("$ %.2f", getTotal(currentTrip))+"");
            txtTripStatus.setText(currentTrip.TripStatus);
            if(currentTrip.TripStatus.equalsIgnoreCase(AppConstants.tripInProgressStatus)){
                txtCancelTrip.setVisibility(View.VISIBLE);
            } else {
                txtCancelTrip.setVisibility(View.GONE);
            }
        }

        public String getFormatedDate() {

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            float dateinFloat = Float.parseFloat(currentTrip.TripDate);
            Date date = float2Date(dateinFloat);
            return  format.format(date);
        }
        public static java.util.Date float2Date(float nbSeconds) {
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
        public void cancelTrip() {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject tripObject = new JSONObject();
            try {
                tripObject.put("TripID",currentTrip.TripID+"");
                tripObject.put("DriverNotificationID",currentTrip.DriverNotificationID+"");
                tripObject.put("DriverID", currentTrip.DriverID+"");
                tripObject.put("TripStatus", "Canceled By Customer");
                Log.e("tripObject: ",tripObject+"");
            }catch(JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.CancelTrip, tripObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    String response = jobj.toString();
                    ResponseMessage responseMessage = new GsonBuilder().create().fromJson(response, ResponseMessage.class);
                    Log.e("after cancel response: ", responseMessage.Response +"");
                    progressDialog.dismiss();

                        Toast.makeText(getActivity(), "Trip Canceled Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error response: ",error+"");
                }
            });
            MyApplication.getInstance().addToRequestQueue(req);
        }
    }
}
