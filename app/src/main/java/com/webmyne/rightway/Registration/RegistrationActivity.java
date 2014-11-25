package com.webmyne.rightway.Registration;


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
import android.widget.Toast;

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
import com.webmyne.rightway.CustomComponents.FormValidator;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.CustomComponents.CustomTypeface;
import com.webmyne.rightway.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class RegistrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new BasicFormFragment()).addToBackStack(null).commit();
        }
    }

    public void setActionBarTitle(String title){

        txtHeader.setText(title);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // for custome font
        return CustomTypeface.getInstance().createView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }

    /**
     * A placeholder fragment containing basic form view.
     */
    public static class BasicFormFragment extends Fragment {

       private EditText etCustomerName,etCustomerMobile,etCustomerEmail,etCustomerCity,etCustomerState,etCustomerZipcode;

        public BasicFormFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
            initView(rootView);

            return rootView;
        }

        private void initView(View rootView){

            etCustomerName=(EditText)rootView.findViewById(R.id.etCustomerName);
            etCustomerMobile=(EditText)rootView.findViewById(R.id.etCustomerMobile);
            etCustomerEmail=(EditText)rootView.findViewById(R.id.etCustomerEmail);
            etCustomerCity=(EditText)rootView.findViewById(R.id.etCustomerCity);
            etCustomerState=(EditText)rootView.findViewById(R.id.etCustomerState);
            etCustomerZipcode=(EditText)rootView.findViewById(R.id.etCustomerZipcode);

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
                emptyFieldsCheck();
            }
            return true;
        }

        public void emptyFieldsCheck() {
            ArrayList<View> arrayList = new ArrayList<View>();
            arrayList.add(etCustomerName);
            arrayList.add(etCustomerMobile);
            arrayList.add(etCustomerEmail);
            FormValidator validator = new FormValidator(new FormValidator.ResultValidationListner() {

                @Override
                public void complete() {
                    if(validateEmail(etCustomerEmail.getText().toString().trim())==true){

                        Customer customerInfo=new Customer();
                        customerInfo.Name=etCustomerName.getText().toString().trim()+"";
                        customerInfo.Mobile=etCustomerMobile.getText().toString().trim()+"";
                        customerInfo.Email=etCustomerEmail.getText().toString().trim()+"";
                        customerInfo.City=etCustomerCity.getText().toString().trim()+"";
                        customerInfo.State=etCustomerState.getText().toString().trim()+"";
                        customerInfo.ZipCode=etCustomerZipcode.getText().toString().trim()+"";

                        // Store customer form data
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
                        complexPreferences.putObject("customer_data", customerInfo);
                        complexPreferences.commit();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.container, new ImageFormFragment(), "imageFragment");
                        ft.addToBackStack("imageFragment");
                        ft.commit();
                    } else {
                        Toast.makeText(getActivity(), "Fill up Valid Email" , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void error(String error) {
                    Toast.makeText(getActivity(), "Fill up " + error, Toast.LENGTH_SHORT).show();
                }
            }).validate(arrayList);
        }

        private boolean validateEmail(String email) {
            Pattern pattern;
            Matcher matcher;
            String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
            return matcher.matches();
        }

    }

    /**
     * A placeholder fragment containing  image view.
     */
    public static class ImageFormFragment extends Fragment implements View.OnClickListener,ImageChooserListener{

        private ProgressDialog progressDialog;
        private ImageChooserManager imageChooserManager;
        private ImageView imgProfilePic;
        private Button btnRegister;
        private int chooserType;
        private String filePath;
        private boolean isProfilePicAdded = false;
        private Customer customerResponse;
        private String imageFilePath;
        public ImageFormFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           setHasOptionsMenu(false);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image_registration, container, false);
            initView(rootView);
            registerForContextMenu(imgProfilePic);
            return rootView;
        }

        private void initView(View rootView) {
            imgProfilePic = (ImageView)rootView.findViewById(R.id.imgProfilePic);
            btnRegister = (Button)rootView.findViewById(R.id.btnRegister);
            imgProfilePic.setOnClickListener(this);
            btnRegister.setOnClickListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            ((RegistrationActivity)getActivity()).setActionBarTitle("CHOOSE YOUR AVTAR");
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);

            menu.add(0,1,Menu.NONE,"Camera");
            menu.add(0,2,Menu.NONE,"Choose from gallery");
            if(isProfilePicAdded == false){
            }else {
                menu.add(0, 3, Menu.NONE, "Remove Picture");
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
            imageChooserManager = new ImageChooserManager(this,ChooserType.REQUEST_PICK_PICTURE, "cabkab", true);
            imageChooserManager.setImageChooserListener(this);
            try {
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
                    //TODO
//                      postImage();

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
                        imgProfilePic.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
                        imageFilePath=image.getFilePathOriginal().toString();
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
            if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
                if (imageChooserManager == null) {
                    reinitializeImageChooser();
                }
                btnRegister.setText("CONTINUE");
                isProfilePicAdded = true;
                imageChooserManager.submit(requestCode, data);
            } else if(resultCode == RESULT_CANCELED){

            }else{
                displayNoneImage();
            }
        }

        private void reinitializeImageChooser() {
            imageChooserManager = new ImageChooserManager(this, chooserType,"cabkab", true);
            imageChooserManager.setImageChooserListener(this);
            imageChooserManager.reinitialize(filePath);
        }

       public void postImage() {


           new AsyncTask<Void,Void,Void>(){
               @Override
               protected Void doInBackground(Void... params) {
                   Log.e("file path: ",imageFilePath+"");
                   File imageFile = new File(imageFilePath);
                   uploadFile(imageFile);
                   return null;
               }
           } .execute();

        }

        public void uploadFile(File fileName){

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(AppConstants.ftpPath);

            try {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("image", new FileBody(fileName));
                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost, localContext);

            } catch (IOException e) {
                e.printStackTrace();
            }


//            FTPClient client = new FTPClient();
//
//            try {
//                client.connect(AppConstants.ftpPath,121);
//                client.login(AppConstants.ftpUsername, AppConstants.ftpPassword);
//                client.setType(FTPClient.TYPE_BINARY);
//                client.changeDirectory("/RiteWay/Images/");
//
//                client.upload(fileName, new MyTransferListener());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                try {
//                    client.disconnect(true);
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
//            }

        }

        /*******  Used to file upload and show progress  **********/

        public class MyTransferListener implements FTPDataTransferListener {

            public void started() {


                // Transfer started
//                Toast.makeText(getActivity(), " Upload Started ...", Toast.LENGTH_SHORT).show();
                System.out.println(" Upload Started ...");
            }

            public void transferred(int length) {
                System.out.println(" transferred ..." );
//                Toast.makeText(getActivity(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
            }

            public void completed() {
                // Transfer completed
                System.out.println(" completed ..." );
//                Toast.makeText(getActivity(), " completed ...", Toast.LENGTH_SHORT).show();
            }

            public void aborted() {
                // Transfer aborted
                System.out.println(" transfer aborted ,please try again..." );
//                Toast.makeText(getActivity()," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
            }

            public void failed() {
                // Transfer failed
                System.out.println(" failed ..." );
            }

        }

        public void postRegistrationData() {

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            // Get customer form data from BasicFormFragment
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
            Customer customer = complexPreferences.getObject("customer_data", Customer.class);

            //Get IMEI Number
            TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            customer.CustomerIMEI_Number= telephonyManager.getDeviceId();

            //Get GCM ID
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("GCM",getActivity().MODE_PRIVATE);
            customer.NotificationID=sharedPreferences.getString("GCM_ID","");

            customer.ProfilePicture=telephonyManager.getDeviceId()+".jpg";
            customer.DeviceType="Android";
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

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.customerProfile, customerObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    String response = jobj.toString();
                    Log.e("response continue: ", response + "");
                    customerResponse = new GsonBuilder().create().fromJson(response, Customer.class);

                    handleCustomerRegistrationData();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error response: ",error+"");
                }
            });
            MyApplication.getInstance().addToRequestQueue(req);

        }

        public void handleCustomerRegistrationData() {

                    // Store customer data from response
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
                    complexPreferences.putObject("customer_data", customerResponse);
                    complexPreferences.commit();

                    SharedPreferences preferences = getActivity().getSharedPreferences("is_registered",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("registration", true);
                    editor.commit();

                    progressDialog.dismiss();

                    Intent i = new Intent(getActivity(), DrawerActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    getActivity().finish();

        }
    }// end of fragment
}// end of activity
