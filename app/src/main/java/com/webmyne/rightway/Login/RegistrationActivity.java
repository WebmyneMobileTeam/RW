package com.webmyne.rightway.Login;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.webmyne.rightway.Application.BaseActivity;
import com.webmyne.rightway.Model.CustomTypeface;
import com.webmyne.rightway.R;

import java.io.File;

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


    public static class ImageFormFragment extends Fragment implements View.OnClickListener,ImageChooserListener{

        private ImageChooserManager imageChooserManager;
        private ImageView imgProfilePic;
        private Button btnRegister;
        private int chooserType;
        private String filePath;
        private boolean isProfilePicAdded = false;



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

                    if(isProfilePicAdded == true){

                    }else{

                    }

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

            imageChooserManager = new ImageChooserManager(this, chooserType,
                    "cabkab", true);
            imageChooserManager.setImageChooserListener(this);
            imageChooserManager.reinitialize(filePath);
        }
    }


}
