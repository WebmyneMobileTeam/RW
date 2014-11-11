package com.webmyne.rightway.Login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.webmyne.rightway.Application.BaseActivity;
import com.webmyne.rightway.Application.DrawerActivity;
import com.webmyne.rightway.Application.MyApplication;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.CustomTypeface;
import com.webmyne.rightway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class RegistrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BasicFormFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setActionBarTitle(String title){
        txtHeader.setText(title);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return CustomTypeface.getInstance().createView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        return false;
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class BasicFormFragment extends Fragment {

        private EditText etCustomerName,etCustomerMobile,etCustomerEmail,etCustomerCity,etCustomerState,etCustomerZipcode;
        public BasicFormFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
            etCustomerName=(EditText)rootView.findViewById(R.id.etCustomerName);
            etCustomerMobile=(EditText)rootView.findViewById(R.id.etCustomerMobile);
            etCustomerEmail=(EditText)rootView.findViewById(R.id.etCustomerEmail);
            etCustomerCity=(EditText)rootView.findViewById(R.id.etCustomerCity);
            etCustomerState=(EditText)rootView.findViewById(R.id.etCustomerState);
            etCustomerZipcode=(EditText)rootView.findViewById(R.id.etCustomerZipcode);

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ((RegistrationActivity)getActivity()).setActionBarTitle("TELL US ABOUT YOU");
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            getActivity().getMenuInflater().inflate(R.menu.registration, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_forward) {
                Customer customerInfo=new Customer();
                customerInfo.Name=etCustomerName.getText().toString().trim()+"";
                customerInfo.Mobile=etCustomerMobile.getText().toString().trim()+"";
                customerInfo.Email=etCustomerEmail.getText().toString().trim()+"";
                customerInfo.City=etCustomerCity.getText().toString().trim()+"";
                customerInfo.State=etCustomerState.getText().toString().trim();
                customerInfo.ZipCode=etCustomerZipcode.getText().toString().trim();
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
                complexPreferences.putObject("customer_data", customerInfo);
                complexPreferences.commit();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();

                ft.replace(R.id.container, new ImageFormFragment(), "imageFragment");
                ft.addToBackStack("imageFragment");
                ft.commit();
            }
            return true;

        }
    }


    public static class ImageFormFragment extends Fragment implements View.OnClickListener,ImageChooserListener{
        ProgressDialog progressDialog;
        private ImageChooserManager imageChooserManager;
        private ImageView imgProfilePic;
        private Button btnRegister;
        private int chooserType;
        private String filePath;
        private boolean isProfilePicAdded = false;

        Customer customerResponse;



        public ImageFormFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           setHasOptionsMenu(false);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image_registration, container, false);

            imgProfilePic = (ImageView)rootView.findViewById(R.id.imgProfilePic);
            btnRegister = (Button)rootView.findViewById(R.id.btnRegister);
            registerForContextMenu(imgProfilePic);

            imgProfilePic.setOnClickListener(this);
            btnRegister.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ((RegistrationActivity)getActivity()).setActionBarTitle("CHOOSE YOUR AVTAR");
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
           // super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);

            menu.add(0,1,Menu.NONE,"Camera");
            menu.add(0,2,Menu.NONE,"Choose from gallery");
            if(isProfilePicAdded == false){

            }else{
                menu.add(0,3,Menu.NONE,"Remove Picture");
            }


        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {



            switch (item.getItemId()){

                case 1:
                    takePicture();
                    break;

                case 2:
                    chooseImage();
                    break;

                case 3:
                    displayNoneImage();
                    break;

            }

            return super.onContextItemSelected(item);
        }

        private void chooseImage() {

            chooserType = ChooserType.REQUEST_PICK_PICTURE;
            imageChooserManager = new ImageChooserManager(this,
                    ChooserType.REQUEST_PICK_PICTURE, "cabkab", true);
            imageChooserManager.setImageChooserListener(this);
            try {
                //pbar.setVisibility(View.VISIBLE);
                filePath = imageChooserManager.choose();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void takePicture() {

            chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
            imageChooserManager = new ImageChooserManager(this,
                    ChooserType.REQUEST_CAPTURE_PICTURE, "cabkab", true);
            imageChooserManager.setImageChooserListener(this);
            try {
              //  pbar.setVisibility(View.VISIBLE);
                filePath = imageChooserManager.choose();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.btnRegister:
                    postRegistrationData();
                    break;

                case R.id.imgProfilePic:

                    getActivity().openContextMenu(imgProfilePic);
                    break;
            }

        }

        @Override
        public void onImageChosen(final ChosenImage image) {

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (image != null) {
                        imgProfilePic.setImageURI(Uri.parse(new File(image
                                .getFileThumbnail()).toString()));

                    }
                }
            });

        }

        @Override
        public void onError(String s) {
            displayNoneImage();
        }

        private void displayNoneImage() {

            btnRegister.setText("SKIP AND CONTINUE");
            isProfilePicAdded = false;
            imgProfilePic.setImageResource(R.drawable.ic_none_profile);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK
                    && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
                if (imageChooserManager == null) {
                    reinitializeImageChooser();
                }

                btnRegister.setText("CONTINUE");
                isProfilePicAdded = true;
                imageChooserManager.submit(requestCode, data);
            } else if(resultCode == RESULT_CANCELED){
               // pbar.setVisibility(View.GONE);

            }else{
                displayNoneImage();
            }
        }

        private void reinitializeImageChooser() {

            imageChooserManager = new ImageChooserManager(this, chooserType,"cabkab", true);
            imageChooserManager.setImageChooserListener(this);
            imageChooserManager.reinitialize(filePath);
        }
        public void postRegistrationData() {

            new AsyncTask<Void,Void,Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog=new ProgressDialog(getActivity());
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }

                protected Void doInBackground(Void... params) {
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
                    Customer customer = complexPreferences.getObject("customer_data", Customer.class);
                    customer.ProfilePicture="temp_path.jpg";
                    customer.DeviceType="android";

                    TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    customer.CustomerIMEI_Number= telephonyManager.getDeviceId();

                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("GCM",getActivity().MODE_PRIVATE);
                    customer.NotificationID=sharedPreferences.getString("GCM_ID","");

                    JSONObject customerObject=new JSONObject();
                    try {
                        customerObject.put("CustomerID", "0");
                        customerObject.put("CustomerIMEI_Number", customer.CustomerIMEI_Number+"");
                        customerObject.put("NotificationID", customer.NotificationID+"");
                        customerObject.put("DeviceType", customer.DeviceType+"");
                        customerObject.put("Name", customer.Name+"");
                        customerObject.put("Mobile", customer.Mobile+"");
                        customerObject.put("Email", customer.Email+"");
                        customerObject.put("City", customer.City+"");
                        customerObject.put("State", customer.State+"");
                        customerObject.put("ZipCode", customer.ZipCode+"");
                        customerObject.put("ProfilePicture", customer.ProfilePicture+"");
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.e("Customer Info: ",customerObject+"");

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.customerRegistration, customerObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jobj) {
                            String response = jobj.toString();
                            Log.e("response continue: ", response + "");
                           customerResponse = new GsonBuilder().create().fromJson(response, Customer.class);



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
                            Log.e("DeviceType", customerResponse.ZipCode + "");
                            Log.e("ProfilePicture",customerResponse.ProfilePicture+"");
                            handleCustomerRegistrationData();
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


            }.execute();
        }

        public void handleCustomerRegistrationData() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
                    complexPreferences.putObject("customer_data", customerResponse);
                    complexPreferences.commit();

                    SharedPreferences preferences = getActivity().getSharedPreferences("is_registered",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("registration", true);
                    editor.commit();

                    progressDialog.dismiss();
                    Intent i = new Intent(getActivity(), DrawerActivity.class);
                    startActivity(i);
                }
            });
        }

    }// end of fragment

}// end of activity
