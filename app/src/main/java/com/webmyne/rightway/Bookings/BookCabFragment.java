package com.webmyne.rightway.Bookings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.rightway.CustomComponents.CallWebService;
import com.webmyne.rightway.CustomComponents.CircleDialog;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.CustomComponents.CustomTimePickerDialog;
import com.webmyne.rightway.CustomComponents.FormValidator;
import com.webmyne.rightway.CustomComponents.ListDialog;
import com.webmyne.rightway.CustomComponents.ListDialogDrivers;
import com.webmyne.rightway.MapNavigator.Navigator;
import com.webmyne.rightway.Model.API;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.MapController;
import com.webmyne.rightway.Model.ResponseMessage;
import com.webmyne.rightway.Model.SharedPreferenceNotification;
import com.webmyne.rightway.MyBooking.MyBookingFragment;
import com.webmyne.rightway.MyNotifications.CustomerNotification;
import com.webmyne.rightway.R;
import com.webmyne.rightway.Registration.Customer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookCabFragment extends Fragment implements View.OnClickListener,MapController.ClickCallback{

    private String currentRate,currentFee,selectedTip,selectedDriver,selectedDriverNotification;
    private float distance;
    private SharedPreferenceNotification sharedPreferenceNotification;
    private int badgeValue=0;
    private ArrayList<CustomerNotification> notificationList;
    private ArrayList<String> driverNames = new ArrayList<String>();
    private ArrayList<LatLng> cabs = new ArrayList<LatLng>();
//    private ProgressDialog progressDialog;
    private static final String LOG_TAG = "RiteWay Book Cab";
    private MapView mv;
    private MapController mc;
    private boolean isPickUpLocationAdded = false;
    private AutoCompleteTextView edPickUpLocation;
    private AutoCompleteTextView edDropOffLocation;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCMEHJ3Z6P1NaDqmSkdTkO-YhnZwYCGDx8";
    private ArrayList<String> place_ids;
    private LatLng pickup_latlng = null;
    private LatLng dropoff_latlng = null;
    private TextView txtDistance,txtPickUpTime,txtTip,txtDriver;
    private EditText etPickupNote;
    private ArrayList<Driver> availableDrivers;
    private Customer customerProfile;
    private CircleDialog circleDialog;


    public static BookCabFragment newInstance(String param1, String param2) {
        BookCabFragment fragment = new BookCabFragment();
        return fragment;
    }

    public BookCabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
        customerProfile=complexPreferences.getObject("customer_data", Customer.class);
//        Log.e("customer ID: ",customerProfile.CustomerID+"");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_cab, container, false);
        mv = (MapView)view.findViewById(R.id.map);

        edPickUpLocation = (AutoCompleteTextView)view.findViewById(R.id.edPickUpLocation);
        edDropOffLocation = (AutoCompleteTextView)view.findViewById(R.id.edDropOffLocation);
        etPickupNote=(EditText)view.findViewById(R.id.etPickupNote);
        edPickUpLocation.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.auto_complete_item));
        edDropOffLocation.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.auto_complete_item));
        txtDistance = (TextView)view.findViewById(R.id.txtDistance);
        txtDistance.setVisibility(View.GONE);
        txtPickUpTime = (TextView)view.findViewById(R.id.txtPickUpTime);
        txtPickUpTime.setOnClickListener(this);
        txtTip = (TextView)view.findViewById(R.id.txtTip);
        txtTip.setOnClickListener(this);
        txtDriver = (TextView)view.findViewById(R.id.txtDriver);
        txtDriver.setOnClickListener(this);

        edPickUpLocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isPickUpLocationAdded = false;
                if(pickup_latlng != null){
                    mc.animateTo(pickup_latlng.latitude,pickup_latlng.longitude);
                }
                return false;
            }
        });

        edDropOffLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(pickup_latlng == null){
                    Toast.makeText(getActivity(), "Set Pick up location first", Toast.LENGTH_SHORT).show();
                    edDropOffLocation.setEnabled(false);
                }else{
                    isPickUpLocationAdded = true;
                    if(dropoff_latlng != null){
                        mc.animateTo(dropoff_latlng.latitude,dropoff_latlng.longitude);
                    }
                }

                return false;
            }
        });

        edDropOffLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                hideKeyBoard(edDropOffLocation);

                new AsyncTask<Void,Void,Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        dropoff_latlng = getLatLng(place_ids.get(position));

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        //  Toast.makeText(getActivity(),ll.latitude+"    :   "+ll.longitude, Toast.LENGTH_SHORT).show();

                        displayMarkers();

                    }
                }.execute();

            }
        });

        edPickUpLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                hideKeyBoard(edPickUpLocation);

                new AsyncTask<Void,Void,Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        pickup_latlng = getLatLng(place_ids.get(position));

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        //  Toast.makeText(getActivity(),ll.latitude+"    :   "+ll.longitude, Toast.LENGTH_SHORT).show();

                        displayMarkers();
                        edDropOffLocation.setEnabled(true);

                    }
                }.execute();
            }
        });

        setHasOptionsMenu(true);

        setView(savedInstanceState);

        return view;
    }

    private void setView(Bundle savedInstanceState) {

        mv.onCreate(savedInstanceState);
        mc = new MapController(mv.getMap());
        mc.whenMapClick(this);
        new CountDownTimer(1500, 1000) {

            @Override
            public void onFinish() {

                int zoom = (int)(mc.getMap().getMaxZoomLevel() - (mc.getMap().getMinZoomLevel()*2.5));
                try {
                    mc.animateTo(mc.getMyLocation().getLatitude(), mc.getMyLocation().getLongitude(), zoom);
//                    displayAvailableDrivers();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {


            }
        }.start();

    }



    @Override
    public void onResume() {

        super.onResume();
        mv.onResume();
        sharedPreferenceNotification=new SharedPreferenceNotification();
        if(isConnected()==true) {
            getActiveDriversList();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
        }

        mc.startTrackMyLocation(mc.getMap(),2000,0, MapController.TrackType.TRACK_TYPE_NONE,new MapController.ChangeMyLocation() {
            @Override
            public void changed(GoogleMap map, Location location, boolean lastLocation) {

            }
        });
    }

    public void getActiveDriversList() {

        circleDialog=new CircleDialog(getActivity(),0);
        circleDialog.setCancelable(true);
        circleDialog.show();


        new CallWebService(AppConstants.getActiveDrivers, CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {
                Type listType = new TypeToken<List<Driver>>() {
                }.getType();

                availableDrivers= new GsonBuilder().create().fromJson(response, listType);

                for(int i=0;i<availableDrivers.size();i++) {
//                    Log.e("DriverID", availableDrivers.get(i).DriverID+"");
//                    Log.e("DriverNotificationID", availableDrivers.get(i).DriverNotificationID+"");
//                    Log.e("FirstName", availableDrivers.get(i).FirstName+"");
//                    Log.e("LastName", availableDrivers.get(i).LastName+"");
//                    Log.e("Webmyne_Latitude", availableDrivers.get(i).Webmyne_Latitude+"");
//                    Log.e("Webmyne_Longitude", availableDrivers.get(i).Webmyne_Longitude+"");
                }

                try {
//                    Log.e("availableDrivers size ",availableDrivers.size()+"");

                    for (int i = 0; i < availableDrivers.size(); i++) {
                        if (availableDrivers.get(i).Webmyne_Latitude != null && availableDrivers.get(i).Webmyne_Longitude != null) {
                            cabs.add(new LatLng(Double.parseDouble(availableDrivers.get(i).Webmyne_Latitude),Double.parseDouble(availableDrivers.get(i).Webmyne_Longitude)));
                            driverNames.add(availableDrivers.get(i).FirstName+","+availableDrivers.get(i).LastName+","+availableDrivers.get(i).DriverID+","+availableDrivers.get(i).DriverNotificationID);
                        }
                    }

                    displayAvailableDrivers();

                } catch(NullPointerException e){
                    e.printStackTrace();
                }

                getCurrentRate();
            }

            @Override
            public void error(VolleyError error) {

                Log.e("error response: ",error+"");
            }
        }.start();

    }

    public void getCurrentRate() {

        new CallWebService(AppConstants.CurrentRate, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                CurrentRate currentRates= new GsonBuilder().create().fromJson(response, CurrentRate.class);

//                Log.e("Rate: ",currentRates.Rate+"");
//                Log.e("TripFee: ",currentRates.TripFee+"");

                SharedPreferences preferences = getActivity().getSharedPreferences("current_rate",getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("rate", currentRates.Rate+"");
                editor.putString("tripFee", currentRates.TripFee+"");
                editor.commit();

                SharedPreferences preferencesRate = getActivity().getSharedPreferences("current_rate",getActivity().MODE_PRIVATE);
                currentRate=preferencesRate.getString("rate","");
                currentFee=preferencesRate.getString("tripFee","");

                getNotificationList();
            }

            @Override
            public void error(VolleyError error) {

            }
        }.start();
    }

    public void getNotificationList() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
        customerProfile =complexPreferences.getObject("customer_data", Customer.class);

        new CallWebService(AppConstants.GetCustomerNotifications+customerProfile.CustomerID , CallWebService.TYPE_JSONARRAY) {

            @Override
            public void response(String response) {

                Type listType=new TypeToken<List<CustomerNotification>>(){
                }.getType();

                notificationList = new GsonBuilder().create().fromJson(response, listType);
//                Log.e("notification list size:",notificationList.size()+"");

                if(notificationList != null) {
                    sharedPreferenceNotification.clearNotification(getActivity());
                    for (int i = 0; i < notificationList.size(); i++) {
                        if(notificationList.get(i).Status.equalsIgnoreCase("false")){
                            badgeValue=badgeValue+1;
                        }
                        sharedPreferenceNotification.saveNotification(getActivity(), notificationList.get(i));
                    }
                }
//                Log.e("Badge value: ",badgeValue+"");
                SharedPreferences preferencesTimeInterval = getActivity().getSharedPreferences("badge_value",getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencesTimeInterval.edit();
                editor.putString("badge_value",badgeValue+"");
                editor.commit();
                circleDialog.dismiss();
            }

            @Override
            public void error(VolleyError error) {

                Log.e("error: ",error+"");

            }
        }.start();


    }

    private void hideKeyBoard(AutoCompleteTextView ed) {

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(ed.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void displayMarkers() {

        mc.clearMarkers();

        displayAvailableDrivers();

        if(pickup_latlng != null) {

            MarkerOptions opts = new MarkerOptions();
            opts.position(pickup_latlng);
            opts.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_pin));
            opts.title("PICK ME UP HERE");
            opts.snippet("");
            addMarker(opts);
            mc.animateTo(pickup_latlng);
        }

        if(dropoff_latlng != null) {

            MarkerOptions opts_drop = new MarkerOptions();
            opts_drop.position(dropoff_latlng);
            opts_drop.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dropoff_pin));
            opts_drop.title("DROP ME HERE");
            opts_drop.snippet("");
            addMarker(opts_drop);
            mc.animateTo(dropoff_latlng);


            Navigator nav = new Navigator(mv.getMap(), pickup_latlng, dropoff_latlng);
            nav.findDirections(false);
            nav.setPathColor(Color.parseColor("#4285F4"), Color.BLUE, Color.BLUE);

            Location lFrom = new Location("");
            lFrom.setLatitude(pickup_latlng.latitude);
            lFrom.setLongitude(pickup_latlng.longitude);

            Location lTo = new Location("");
            lTo.setLatitude(dropoff_latlng.latitude);
            lTo.setLongitude(dropoff_latlng.longitude);

            distance = lFrom.distanceTo(lTo) / 1000;

            // display distance to map
            if (!txtDistance.isShown()) {
                txtDistance.setVisibility(View.VISIBLE);
            }

            txtDistance.setText(String.format("%.2f miles", distance*0.6214) + "\n" + String.format("$ %.2f", distance * 0.6214 * (Double.parseDouble(currentRate))));

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_bookcab,menu);

    }

    public  boolean isConnected() {

        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_book:

                ArrayList<View> arrayList = new ArrayList<View>();
                arrayList.add(edPickUpLocation);
                arrayList.add(edDropOffLocation);
                arrayList.add(txtPickUpTime);
                arrayList.add(txtDriver);


                FormValidator validator = new FormValidator(new FormValidator.ResultValidationListner() {

                    @Override
                    public void complete() {
                        if(isConnected()==true) {
                            sendBookRequest();
                        } else {
                            Toast.makeText(getActivity(), "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void error(String error) {
                        Toast.makeText(getActivity(),"Fill up "+error, Toast.LENGTH_SHORT).show();
                    }
                }).validate(arrayList);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendBookRequest() {
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

                JSONObject tripObject = new JSONObject();
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("GCM",getActivity().MODE_PRIVATE);
                String GCM_ID=sharedPreferences.getString("GCM_ID","");

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String stringDate=dateFormat.format(new Date());
                    Date date =dateFormat.parse(stringDate) ;

                    tripObject.put("TripID", "0");
                    tripObject.put("CustomerID", customerProfile.CustomerID+"");
                    tripObject.put("DriverID",selectedDriver+ "");
                    tripObject.put("PickupLatitude", pickup_latlng.latitude+"");
                    tripObject.put("PickupLongitude", pickup_latlng.longitude+"");
                    tripObject.put("PickupAddress", edPickUpLocation.getText().toString().trim()+"");
                    tripObject.put("PickupNote", etPickupNote.getText().toString().trim()+"");
                    tripObject.put("DropoffLatitude", dropoff_latlng.latitude+"");
                    tripObject.put("DropoffLongitude", dropoff_latlng.longitude+"");
                    tripObject.put("DropOffAddress", edDropOffLocation.getText().toString().trim()+"");
                    tripObject.put("PickupTime", txtPickUpTime.getText().toString().trim()+"");
                    tripObject.put("TripDate", (date.getTime()/1000)+"");
                    tripObject.put("TipPercentage", selectedTip+"");
//                    Log.e("trip rate when send:  ",currentRate+"");
                    tripObject.put("TripFare", currentRate+"");
                    tripObject.put("TripFee", currentFee+"");
                    tripObject.put("TripDistance", String.format("%.2f", distance)+"");
                    tripObject.put("PaymentType", "");
                    tripObject.put("TripStatus", AppConstants.tripInProgressStatus);
                    tripObject.put("CustomerName", "");
                    tripObject.put("DriverName", "");
                    tripObject.put("CustomerNotificationID", GCM_ID);
                    tripObject.put("DriverNotificationID", selectedDriverNotification);
                    tripObject.put("isCustomerFeedbackGiven", false);
                    tripObject.put("isDriverFeedbackGiven", false);
//                    Log.e("tripObject: ",tripObject+"");

                }catch(JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Reader reader = API.callWebservicePost(AppConstants.bookTrip, tripObject.toString());
                ResponseMessage responseMessage = new GsonBuilder().create().fromJson(reader, ResponseMessage.class);
//                Log.e("responseMessage:",responseMessage.Response+"");
                handlePostData();

                return null;
            }

        }.execute();
    }

    public void handlePostData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                circleDialog.dismiss();
                Toast.makeText(getActivity(), "Trip Requested Successfully", Toast.LENGTH_SHORT).show();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                MyBookingFragment fragmentMyBooking = MyBookingFragment.newInstance("", "");
                if (manager.findFragmentByTag("MYBOOKING") == null) {
                    ft.replace(R.id.main_content, fragmentMyBooking,"MYBOOKING").commit();
                }
            }
        });
    }



    public void displayAvailableDrivers() {

        for(int i=0;i<cabs.size();i++){
            String[] splited = driverNames.get(i).split(",");
            MarkerOptions opts = new MarkerOptions();
            opts.position(cabs.get(i));
            opts.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_driver));
            opts.title(splited[0] + " " + splited[1]);
            opts.snippet("");

            addMarker(opts);

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }






    @Override
    public void onPause() {


        mv.onPause();
        mc.stopTrackMyLocation();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mv.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txtPickUpTime:

                showTimeDialog();

                break;

            case R.id.txtTip:

                showTipDialog();

                break;

            case R.id.txtDriver:

                showAvailableDrivers();

                break;

        }
    }

    private void showAvailableDrivers() {

        ListDialogDrivers listDialog = new ListDialogDrivers(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        listDialog.setCancelable(true);
        listDialog.setCanceledOnTouchOutside(true);
        listDialog.title("CHOOSE DRIVER");
        listDialog.setItems(driverNames);
        listDialog.setSelectedListner(new ListDialogDrivers.setSelectedListner() {
            @Override
            public void selected(String value) {
                String[] splited = value.split(",");
                txtDriver.setText(splited[0]+" "+splited[1]);

                selectedDriver=splited[2];
                selectedDriverNotification=splited[3];


            }
        });
        listDialog.show();

    }



    private void showTipDialog() {

        ListDialog listDialog = new ListDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        listDialog.setCancelable(true);
        listDialog.setCanceledOnTouchOutside(true);
        listDialog.title("CHOOSE TIP");
        final ArrayList<String> tips = new ArrayList<String>();
        tips.add("0");
        tips.add("2");
        tips.add("5");
        tips.add("10");
        tips.add("15");
        listDialog.setItems(tips);
        listDialog.setSelectedListner(new ListDialog.setSelectedListner() {
            @Override
            public void selected(String value) {

                txtTip.setText(value+"%");
                for(int i=0;i<tips.size();i++) {
                    if(tips.get(i).equalsIgnoreCase(value)){
                        selectedTip=tips.get(i);
                    }
                }

            }
        });
        listDialog.show();


    }

    private void showTimeDialog() {

        CustomTimePickerDialog dialog = new CustomTimePickerDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.title("SELECT TIME FROM SCROLL DOWN BELOW");
        dialog.setSelectedListner(new CustomTimePickerDialog.setSelectedListner() {
            @Override
            public void selected(String value) {

                txtPickUpTime.setText(value);

            }
        });
        dialog.show();

    }

    @Override
    public void clicked(GoogleMap map, final LatLng latLng) {

        if(isPickUpLocationAdded == false){

            pickup_latlng = latLng;
            displayMarkers();

            new AsyncTask<Void,Void,Void>() {
                String address = null;

                @Override
                protected Void doInBackground(Void... voids) {

                    address = getAddress(getActivity(),latLng.latitude,latLng.longitude);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);


                    edPickUpLocation.setText(address);


                }
            }.execute();

        }else{

            dropoff_latlng = latLng;
            displayMarkers();

            new AsyncTask<Void,Void,Void>() {
                String address = null;

                @Override
                protected Void doInBackground(Void... voids) {

                    address = getAddress(getActivity(),latLng.latitude,latLng.longitude);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    edDropOffLocation.setText(address);

                }
            }.execute();
        }
    }

    private void addMarker(MarkerOptions opts) {



        mc.addMarker(opts, new MapController.MarkerCallback() {
            @Override
            public void invokedMarker(GoogleMap map, Marker marker) {

            }
        });
    }

    private LatLng getLatLng(String place_id){

        LatLng latLng = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
            sb.append("?placeid="+place_id);
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return latLng;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return latLng;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject predsJsonObj = jsonObj.getJSONObject("result");
            JSONObject objGeoMet = predsJsonObj.getJSONObject("geometry");
            JSONObject objLoc = objGeoMet.getJSONObject("location");

            double lat = Double.parseDouble(objLoc.getString("lat"));
            double lng = Double.parseDouble(objLoc.getString("lng"));
            latLng = new LatLng(lat,lng);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }


        return  latLng;

    }


    private ArrayList<String> autocomplete(String input) {


        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

//            Log.e("Log Url PlaceAPI",sb.toString());
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            place_ids = new ArrayList<String>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                place_ids.add(predsJsonArray.getJSONObject(i).getString("place_id"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }


    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

        private ArrayList<String> resultList;


        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    public  String getAddress(Context ctx, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String locality=address.getLocality();
                String city=address.getCountryName();
                String region_code=address.getCountryCode();
                String zipcode=address.getPostalCode();
                String street = address.getAddressLine(0);
                String street2 = address.getAddressLine(1);

                double lat =address.getLatitude();
                double lon= address.getLongitude();

                if(street !=null){
                    result.append(street+" ");
                }

                if(street2 != null){
                    result.append(street2+" ");
                }

                if(locality != null){
                    result.append(locality+" ");
                }


                if(city != null){
                    result.append(city+" "+ region_code+" ");
                }

                if(zipcode != null){
                    result.append(zipcode);
                }

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

}
