package com.webmyne.rightway.Bookings;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.webmyne.rightway.Application.DrawerActivity;
import com.webmyne.rightway.R;


import java.util.Calendar;

public class BookingFormFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int pos;
    private String title;
    BookCabFragment fragment;

    // variables
    private EditText edEnterLocation;
    private Button btnSubmit;
    private ViewFlipper viewFlipper;
    private Spinner spinnerForm;

    public static BookingFormFragment newInstance(int param1, String param2) {
        BookingFormFragment fragment = new BookingFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public BookingFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.bookingform_enterlocation, container, false);

         edEnterLocation = (EditText)view.findViewById(R.id.edEnterLocation);
         edEnterLocation.setGravity(Gravity.CENTER);
         btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
         viewFlipper = (ViewFlipper)view.findViewById(R.id.viewFlipper);
         spinnerForm = (Spinner)view.findViewById(R.id.spinnerForm);

        switch (pos){

            case 0:

                btnSubmit.setText("PICK UP HERE");

                break;

            case 1:

               Calendar mcurrentTime = Calendar.getInstance();
               final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
               final int minute = mcurrentTime.get(Calendar.MINUTE);

                edEnterLocation.setText(updateTime(hour,minute));
                edEnterLocation.setFocusable(false);

                edEnterLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                edEnterLocation.setText(updateTime(selectedHour,selectedMinute));
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();


                    }
                });
                btnSubmit.setText("NEXT");
                break;


            case 2:
                edEnterLocation.setHint("Take a note");
                btnSubmit.setText("NEXT");
                break;


            case 3:
                btnSubmit.setText("NEXT");
                break;

            case 4:
                viewFlipper.showNext();
                btnSubmit.setText("NEXT");
                break;


            case 5:
                viewFlipper.showNext();
                btnSubmit.setText("NEXT");
                break;


            case 6:
                viewFlipper.showNext();
                btnSubmit.setText("NEXT");
                break;


            case 7:
                edEnterLocation.setText("");
                edEnterLocation.setHint("");
                btnSubmit.setText("REQUEST A TRIP");
                break;

            default:


                edEnterLocation.setHint("Default");
                btnSubmit.setText("Default");
                break;

        }




         btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             fragment.setNextView();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragment = (BookCabFragment)getActivity().getSupportFragmentManager().findFragmentByTag(DrawerActivity.BOOKCAB);
    }

    public void onButtonPressed(Uri uri) {


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

       return aTime;
    }


}
