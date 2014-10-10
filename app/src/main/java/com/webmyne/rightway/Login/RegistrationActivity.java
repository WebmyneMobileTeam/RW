package com.webmyne.rightway.Login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.rightway.Application.BaseActivity;
import com.webmyne.rightway.Model.CustomTypeface;
import com.webmyne.rightway.R;

public class RegistrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ((RegistrationActivity)getActivity()).setActionBarTitle("TELL US ABOUT YOU");
        }
    }





}
