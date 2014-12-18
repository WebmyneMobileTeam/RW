package com.webmyne.rightway.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;
import com.webmyne.rightway.Application.DrawerActivity;
import com.webmyne.rightway.Application.MyApplication;

import com.webmyne.rightway.CustomComponents.CircleDialog;
import com.webmyne.rightway.CustomComponents.CircularImageView;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.Model.API;
import com.webmyne.rightway.Model.ResponseMessage;
import com.webmyne.rightway.Registration.Customer;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Reader;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class ProfileFragment extends Fragment implements View.OnClickListener,ImageChooserListener {
    private Customer customerProfile;
//    private ProgressDialog progressDialog;
    private CircleDialog circleDialog;
    private EditText txtCustomerName,txtCustomerMobile,txtCustomerEmail,txtCustomerCity,txtCustomerState,txtCustomerZipCode;
    private TextView txtUpdate;
    private CircularImageView profileImage;
    private int chooserType;
    private String filePath;
    private boolean isProfilePicAdded = false;
    private String imageFilePath;
    private ImageChooserManager imageChooserManager;
//    Customer customerResponse;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_profile, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        profileImage=(CircularImageView)rootView.findViewById(R.id.profileImage);
        registerForContextMenu(profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().openContextMenu(profileImage);
            }
        });
        txtCustomerName=(EditText)rootView.findViewById(R.id.txtCustomerName);
        txtCustomerMobile=(EditText)rootView.findViewById(R.id.txtCustomerMobile);
        txtCustomerEmail=(EditText)rootView.findViewById(R.id.txtCustomerEmail);
        txtCustomerCity=(EditText)rootView.findViewById(R.id.txtCustomerCity);
        txtCustomerState=(EditText)rootView.findViewById(R.id.txtCustomerState);
        txtCustomerZipCode=(EditText)rootView.findViewById(R.id.txtCustomerZipCode);
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
                if(isConnected()==true) {

                    updateProfileData();
                } else {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
        customerProfile=complexPreferences.getObject("customer_data", Customer.class);
        Log.e("image path:",AppConstants.fileDownloadPath + customerProfile.ProfilePicture+"");
        Picasso.with(getActivity()).load(AppConstants.fileDownloadPath + customerProfile.ProfilePicture)
                .placeholder(R.drawable.sample_profile)
                .error((R.drawable.sample_profile))
                .into(profileImage);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,1, Menu.NONE,"Choose from Camera");
        menu.add(0,2,Menu.NONE,"Choose from gallery");

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
        imageChooserManager = new ImageChooserManager(this,ChooserType.REQUEST_CAPTURE_PICTURE, "cabkab", true);
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
    public void onImageChosen(final ChosenImage image) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {

                    profileImage.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
                    imageFilePath=image.getFilePathOriginal().toString();
                    Log.e("image file path...........",imageFilePath+"");
                    isProfilePicAdded=true;
                }
            }
        });
    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onResume() {
        super.onResume();
        isProfilePicAdded = false;
        txtCustomerName.setText(customerProfile.Name);
        txtCustomerMobile.setText(customerProfile.Mobile);
        txtCustomerEmail.setText(customerProfile.Email);
        txtCustomerCity.setText(customerProfile.City);
        txtCustomerState.setText(customerProfile.State);
        txtCustomerZipCode.setText(customerProfile.ZipCode);

    }

    public  boolean isConnected() {
        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }

    public void updateProfileData() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                circleDialog=new CircleDialog(getActivity(),0);
                circleDialog.setCancelable(true);
                circleDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

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
                    if(isProfilePicAdded) {
                        customerObject.put("ProfilePicture", imageFilePath.substring(imageFilePath.lastIndexOf("/")+1));
                    }else {
                        customerObject.put("ProfilePicture", customerProfile.ProfilePicture+"");
                    }
                    Log.e("isProfilePicAdded",isProfilePicAdded+"");
                    Log.e("image path:",customerObject.get("ProfilePicture")+"");
                } catch (JSONException e){
                    e.printStackTrace();
                }

                Reader reader = API.callWebservicePost(AppConstants.customerProfile, customerObject.toString());
                customerProfile= new GsonBuilder().create().fromJson(reader, Customer.class);

                handlePostData(customerProfile);
                return null;
            }
        }.execute();
    }

    public void handlePostData(final Customer customerResponse) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isProfilePicAdded) {
                    postImage();
                } else {
                    circleDialog.dismiss();
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
                    complexPreferences.putObject("customer_data", customerResponse);
                    complexPreferences.commit();
                    Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }

            isProfilePicAdded = true;
            imageChooserManager.submit(requestCode, data);
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,"cabkab", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


    @Override
    public void onClick(View v) {

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

        Log.e("filename",fileName+"");
        FTPClient client = new FTPClient();

        try {
            client.connect(AppConstants.ftpPath,121);
            client.login(AppConstants.ftpUsername, AppConstants.ftpPassword);
            client.setType(FTPClient.TYPE_AUTO);
            client.changeDirectory("/Images/");

            client.upload(fileName, new MyTransferListener());
            Log.e("filename",fileName+"");
        } catch (Exception e) {
            e.printStackTrace();

            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            Log.e("filename","Upload Started ");
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
            circleDialog.dismiss();

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);

            complexPreferences.putObject("customer_data", customerProfile);
            complexPreferences.commit();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            });

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
}
