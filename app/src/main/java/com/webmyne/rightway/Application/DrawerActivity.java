package com.webmyne.rightway.Application;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
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
import com.webmyne.rightway.CurrentTrip.FragmentCurrentTripMap;
import com.webmyne.rightway.DrawerLibrary.ActionBarDrawerToggle;
import com.webmyne.rightway.DrawerLibrary.DrawerArrowDrawable;
import com.webmyne.rightway.Model.CustomTypeface;
import com.webmyne.rightway.MyBooking.MyBookingFragment;
import com.webmyne.rightway.MyNotifications.MyNotificationFragment;
import com.webmyne.rightway.Profile.ProfileFragment;
import com.webmyne.rightway.R;

public class DrawerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private DrawerLayout drawer;
    private ListView leftDrawerList;
    //    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String[] leftSliderData = {"BOOK A CAB", "MY BOOKINGS", "MY PROFILE", "CONTACT US","NOTIFICATIONS","CURRENT TRIP","SETTING"};
    private boolean isPupil;
    public static String BOOKCAB = "bookcab";
    public static String MYBOOKING = "mybooking";
    public static String PROFILE = "profile";
    public static String CONTACTUS = "contactus";
    public static String MYNOTIFICATION = "mynotification";
    public static String CURRENT_TRIP = "current_trip";
    public static String SETTINGS="settings";




    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return CustomTypeface.getInstance().createView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

         //Load My Places First
         FragmentManager manager = getSupportFragmentManager();
         FragmentTransaction ft = manager.beginTransaction();

        BookCabFragment fragmentBookCab = BookCabFragment.newInstance("", "");
        if (manager.findFragmentByTag(BOOKCAB) == null) {
            ft.replace(R.id.main_content, fragmentBookCab,BOOKCAB).commit();
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

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,drawerArrow, R.string.drawer_open,R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
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

            case 5:

                FragmentCurrentTripMap fragmentCurrentTripMap = FragmentCurrentTripMap.newInstance("", "");
                if (manager.findFragmentByTag(CURRENT_TRIP) == null) {
                    ft.replace(R.id.main_content, fragmentCurrentTripMap,CURRENT_TRIP).commit();
                }
                txtHeader.setText("CURRENT TRIP");
                break;

            case 6:

                FragmentCurrentTripMap fragmentCurrentTripMaps = FragmentCurrentTripMap.newInstance("", "");
                if (manager.findFragmentByTag(SETTINGS) == null) {
                    ft.replace(R.id.main_content, fragmentCurrentTripMaps,SETTINGS).commit();
                }
                txtHeader.setText("CURRENT TRIP");
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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

        return false;
    }



}
