package com.webmyne.rightway.Profile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
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

import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.Registration.Customer;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.R;


import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {
    Customer customerProfile;
    ProgressDialog progressDialog;
    private EditText txtCustomerName,txtCustomerMobile,txtCustomerEmail,txtCustomerCity,txtCustomerState,txtCustomerZipCode;
    private TextView txtUpdate;
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_profile, container, false);
        txtCustomerName=(EditText)rootView.findViewById(R.id.txtCustomerName);
        txtCustomerMobile=(EditText)rootView.findViewById(R.id.txtCustomerMobile);
        txtCustomerEmail=(EditText)rootView.findViewById(R.id.txtCustomerEmail);
        txtCustomerCity=(EditText)rootView.findViewById(R.id.txtCustomerCity);
        txtCustomerState=(EditText)rootView.findViewById(R.id.txtCustomerState);
        txtCustomerZipCode=(EditText)rootView.findViewById(R.id.txtCustomerZipCode);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
        customerProfile=complexPreferences.getObject("customer_data", Customer.class);

        txtCustomerName.setText(customerProfile.Name);
        txtCustomerMobile.setText(customerProfile.Mobile);
        txtCustomerEmail.setText(customerProfile.Email);
        txtCustomerCity.setText(customerProfile.City);
        txtCustomerState.setText(customerProfile.State);
        txtCustomerZipCode.setText(customerProfile.ZipCode);
        txtUpdate=(TextView)rootView.findViewById(R.id.txtUpdate);

        txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerProfile.Name=txtCustomerName.getText().toString().trim();
                customerProfile.Mobile=txtCustomerMobile.getText().toString().trim();
                customerProfile.Email=txtCustomerEmail.getText().toString().trim();
                customerProfile.City=txtCustomerCity.getText().toString().trim();
                customerProfile.State=txtCustomerState.getText().toString().trim();
                customerProfile.ZipCode=txtCustomerZipCode.getText().toString().trim();
                updateProfileData();
            }
        });
        return rootView;
    }

    public void updateProfileData() {

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JSONObject customerObject=new JSONObject();
        try {
            customerObject.put("CustomerID", customerProfile.CustomerID);
            customerObject.put("CustomerIMEI_Number", customerProfile.CustomerIMEI_Number+"");
            customerObject.put("NotificationID", customerProfile.NotificationID+"");
            customerObject.put("DeviceType", customerProfile.DeviceType+"");
            customerObject.put("Name", customerProfile.Name+"");
            customerObject.put("Mobile", customerProfile.Mobile+"");
            customerObject.put("Email", customerProfile.Email+"");
            customerObject.put("City", customerProfile.City+"");
            customerObject.put("State", customerProfile.State+"");
            customerObject.put("ZipCode", customerProfile.ZipCode+"");
            customerObject.put("DeviceType", customerProfile.DeviceType+"");
            customerObject.put("ProfilePicture", customerProfile.ProfilePicture+"");

        } catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.customerRegistration, customerObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                String response = jobj.toString();
                Log.e("response continue: ", response + "");
                progressDialog.dismiss();
                Customer customerResponse = new GsonBuilder().create().fromJson(response, Customer.class);

                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_profile", 0);
                complexPreferences.putObject("customer_profile_data", customerResponse);
                complexPreferences.commit();

                Log.e("CustomerID",customerResponse.CustomerID+"");
                Log.e("CustomerIMEI_Number",customerResponse.CustomerIMEI_Number+"");
                Log.e("NotificationID",customerResponse.NotificationID+"");
                Log.e("DeviceType",customerResponse.DeviceType+"");
                Log.e("Name",customerResponse.Name+"");
                Log.e("Mobile",customerResponse.Mobile+"");
                Log.e("Email",customerResponse.Email+"");
                Log.e("City",customerResponse.City+"");
                Log.e("State",customerResponse.State+"");
                Log.e("ZipCode",customerResponse.ZipCode+"");

                Log.e("ProfilePicture",customerResponse.ProfilePicture+"");
                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();


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
