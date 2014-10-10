package com.webmyne.rightway.Login;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webmyne.rightway.Application.BaseActivity;
import com.webmyne.rightway.Model.CustomTypeface;
import com.webmyne.rightway.R;

public class RegistrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BasicFormFragment())
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
        getMenuInflater().inflate(R.menu.registration, menu);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_forward) {


            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container,new ImageFormFragment(),"imageFragment");
            ft.addToBackStack("imageFragment");
            ft.commit();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class BasicFormFragment extends Fragment {

        public BasicFormFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
            setHasOptionsMenu(true);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ((RegistrationActivity)getActivity()).setActionBarTitle("TELL US ABOUT YOU");
        }
    }


    public static class ImageFormFragment extends Fragment {

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



            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ((RegistrationActivity)getActivity()).setActionBarTitle("TELL US ABOUT YOU");
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
           // super.onCreateOptionsMenu(menu, inflater);
        }
    }


}
