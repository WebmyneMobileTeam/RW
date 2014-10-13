package com.webmyne.rightway.Application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.webmyne.rightway.Bookings.BookCabFragment;
import com.webmyne.rightway.ContactUs.ContactUsFragment;
import com.webmyne.rightway.MyBooking.MyBookingFragment;
import com.webmyne.rightway.MyNotifications.MyNotificationFragment;
import com.webmyne.rightway.Profile.ProfileFragment;
import com.webmyne.rightway.R;

public class DrawerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawer;
    private ListView leftDrawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String[] leftSliderData = {"BOOK A CAB", "MY BOOKINGS", "MY PROFILE", "CONTACT US","NOTIFICATIONS"};
    private boolean isPupil;
    private static String BOOKCAB = "bookcab";
    private static String MYBOOKING = "mybooking";
    private static String PROFILE = "profile";
    private static String CONTACTUS = "contactus";
    private static String MYNOTIFICATION = "mynotification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //Load My Places First
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();


            BookCabFragment fragmentBooking = BookCabFragment.newInstance("", "");
            if (manager.findFragmentByTag(BOOKCAB) == null) {
                ft.replace(R.id.main_content, fragmentBooking,BOOKCAB).commit();
            }


        // header
        txtHeader.setText("BOOK A CAB");
        initFields();
        initDrawer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume ", "in drawer activty");
    }

    private void initFields() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftDrawerList.setAdapter(new NavigationDrawerAdapter(DrawerActivity.this, leftSliderData));
        leftDrawerList.setOnItemClickListener(this);

    }

    private void initDrawer() {



        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DrawerActivity.this, drawer, R.drawable.ic_navigation_drawer, R.string.open_drawer, R.string.close_drawer) {

            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {

            }

        };
        drawer.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Add your onclick logic here
        drawer.closeDrawers();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        //drawer items for pupil

            switch (position) {

                case 0:

                    BookCabFragment fragmentBookCab = BookCabFragment.newInstance("", "");
                    if (manager.findFragmentByTag(BOOKCAB) == null) {
                        ft.replace(R.id.main_content, fragmentBookCab,BOOKCAB).commit();
                    }
                    txtHeader.setText("BOOKING");
                    break;

                case 1:
                    MyBookingFragment fragmentMyBooking = MyBookingFragment.newInstance("", "");
                    if (manager.findFragmentByTag(MYBOOKING) == null) {
                        ft.replace(R.id.main_content, fragmentMyBooking,MYBOOKING).commit();
                    }
                    txtHeader.setText("MY BOOKINGS");
                    break;

                case 2:
                    ProfileFragment fragmentProfile = ProfileFragment.newInstance("", "");
                    if (manager.findFragmentByTag(PROFILE) == null) {
                        ft.replace(R.id.main_content, fragmentProfile,PROFILE).commit();
                    }
                    txtHeader.setText("MY PROFILE");
                    break;

                case 3:
                    ContactUsFragment fragmentcontactus = ContactUsFragment.newInstance("", "");
                    if (manager.findFragmentByTag(CONTACTUS) == null) {
                        ft.replace(R.id.main_content, fragmentcontactus,CONTACTUS).commit();
                    }
                    txtHeader.setText("CONTACT US");
                    break;

                case 4:
                    MyNotificationFragment fragmentmynotification = MyNotificationFragment.newInstance("", "");
                    if (manager.findFragmentByTag(MYNOTIFICATION) == null) {
                        ft.replace(R.id.main_content, fragmentmynotification,MYNOTIFICATION).commit();
                    }
                    txtHeader.setText("NOTIFICATIONS");
                    break;


            }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                actionBarDrawerToggle.onOptionsItemSelected(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();


    }


    //region Drawer code
    // Navigation Drawer Adapter
    public class NavigationDrawerAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        String[] leftSliderData;

        public NavigationDrawerAdapter(Context context, String[] leftSliderData) {
            this.context = context;
            this.leftSliderData = leftSliderData;
        }

        public int getCount() {

            return leftSliderData.length;

        }

        public Object getItem(int position) {
            return leftSliderData[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            TextView txtDrawerItem;
        }


        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_drawer, parent, false);
                holder = new ViewHolder();
                holder.txtDrawerItem = (TextView) convertView.findViewById(R.id.txtDrawerItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtDrawerItem.setText(leftSliderData[position]);
            return convertView;

        }

    }
    //</editor-fold>


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}
