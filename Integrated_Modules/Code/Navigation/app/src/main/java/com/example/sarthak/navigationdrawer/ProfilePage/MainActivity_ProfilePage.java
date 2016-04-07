package com.example.sarthak.navigationdrawer.ProfilePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity_ProfilePage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Profile_RecyclerViewAdapter adapter;
    private ArrayList<ProfileClass> profile;
    private Toolbar toolbar;
    private int profilePicture;
    private int userInfoIcons[] = {R.drawable.ic_call_black_24dp, R.drawable.ic_mail_black_24dp, R.drawable.ic_star_half_black_24dp, R.drawable.ic_location_on_black_24dp};
    private String userInfoTitle[] = {"Phone", "Mail", "Rating", "Location Shared Status"};
    private String userInfoDescription[] = {"1233456789", "123456@gmail.com", "4.5", "Shared"};
    private CollapsingToolbarLayout collapsingToolbar;
    private String nameOfUser = "Sarthak";
    private FloatingActionButton floatingActionButton, floatingActionButtonChangeName, floatingActionButtonChangePhoto;
    private static int RESULT_LOAD_IMG = 1;
    private Intent galleryIntent;
    private ImageView profilePictureImageView;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private String selectedImagePath,imgDecodableString;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile_page);

        /*Customising Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_ProfilePage);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_ProfilePage);
        collapsingToolbar.setTitle(nameOfUser);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout_profilePage);
        appBarLayout.setExpanded(true);
        pref = this.getSharedPreferences("AppPref", Context.MODE_PRIVATE);


        /*If clicked on Profile Pic then show the photo*/
        appBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen == true)
                    animateFAB();
                Intent intent = new Intent(MainActivity_ProfilePage.this, ViewProfileImage.class);
                intent.putExtra("Path_of_Image", selectedImagePath);
                startActivity(intent);
            }
        });

        /*Setting the profile pic*/
        profilePictureImageView = (ImageView) findViewById(R.id.imageView_UserImage);
        profilePicture = R.drawable.header;
        profilePictureImageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.header));
        colorToolbars();

        /*Setting up values for user parameters*/
        initialiseProfile();

        /*Setting up the recycler view for the user parameters to display*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_userDetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        /*Setting up the adapter for the recyclerview*/
        adapter = new Profile_RecyclerViewAdapter(profile, MainActivity_ProfilePage.this);
        recyclerView.setAdapter(adapter);

        /*Adding click listener for each item of recycler view*/
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity_ProfilePage.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (profile.get(position).getTitle().equals("Rating")) {
                            /*If rating then do not allow to change*/
                            Toast.makeText(MainActivity_ProfilePage.this, "Can not edit Ratings", Toast.LENGTH_SHORT).show();
                        } else {
                            /*If fab is open then close it*/
                            if (isFabOpen == true) {
                                animateFAB();
                            }
                            if (profile.get(position).getTitle().equals("Location Shared Status")) {
                                /*Toggle the status*/
                                if (profile.get(position).getDescription().equals("Shared"))
                                    profile.get(position).setDescription("Not Shared");
                                else {
                                    profile.get(position).setDescription("Shared");
                                }
                                /*notify that the dataset has changed*/
                                adapter.notifyDataSetChanged();
                                saveLocationSharedToDatabase();
                            } else {
                                /*Call the EditProfile class to let the user edit his/her detail*/
                                Intent intent = new Intent(MainActivity_ProfilePage.this, EditProfile.class);
                                intent.putExtra("Title", profile.get(position).getTitle());
                                intent.putExtra("Description", profile.get(position).getDescription());
                                startActivity(intent);
                            }
                        }
                    }
                })
        );

        /*3 FABs
        * 1-Main
        * and the other 2 display when the main one is clicked*/
        /*Setting up the FABs*/
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_EditProfile);
        floatingActionButtonChangeName = (FloatingActionButton) findViewById(R.id.fab_ChangeName);
        floatingActionButtonChangePhoto = (FloatingActionButton) findViewById(R.id.fab_ChangePhoto);

        /*On click listeners for each FAB*/
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*If clicked on main FAB then animate the FAB*/
                animateFAB();
            }
        });

        floatingActionButtonChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Close the open FAB*/
                animateFAB();
                /*If clicked on change name FAB then redirect to EditProfile class to change his name*/
                Intent intent = new Intent(MainActivity_ProfilePage.this, EditProfile.class);
                intent.putExtra("Title", "Name");
                intent.putExtra("Description", nameOfUser);
                startActivity(intent);
            }
        });
        floatingActionButtonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Close the open FAB*/
                animateFAB();
                /*Allow the user to choose his image*/
                /*Open Gallery*/
                galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

    }

    /*To add*/
    private void savePhotoToDatabase(String photo){
        ArrayList<NameValuePair> parameters=new ArrayList<>();
        parameters.add(new BasicNameValuePair(Config.photo,photo));
        parameters.add(new BasicNameValuePair(Config.phone_number,"8758964908"));

        ServerRequest sr = new ServerRequest();
        Log.d("here", "params sent");
        //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
        JSONObject json = sr.getJSON(Config.ip+"/register",parameters);
        Log.d("here", "json received");
        if(json != null){
            try{
                String jsonstr = json.getString("response");
                String sue = json.getString("use");

                Toast.makeText(MainActivity_ProfilePage.this,jsonstr+ "     " + sue ,Toast.LENGTH_LONG).show();

                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*To add*/
    private void saveLocationSharedToDatabase(){

    }

    /*Toggle between open and closed FAB states*/
    private void animateFAB() {
        if (isFabOpen) {
            floatingActionButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_edit_white_24dp));
            floatingActionButtonChangeName.startAnimation(fab_close);
            floatingActionButtonChangePhoto.startAnimation(fab_close);
            floatingActionButtonChangeName.setClickable(false);
            floatingActionButtonChangePhoto.setClickable(false);
            isFabOpen = false;
        } else {
            floatingActionButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_clear_white_24dp));
            floatingActionButtonChangeName.startAnimation(fab_open);
            floatingActionButtonChangePhoto.startAnimation(fab_open);
            floatingActionButtonChangeName.setClickable(true);
            floatingActionButtonChangePhoto.setClickable(true);
            isFabOpen = true;
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // When an Image is picked
//        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
//                && null != data) {
//            /*Extract path from the image selected*/
//            Uri selectedImageUri = data.getData();
//            selectedImagePath = getPath(selectedImageUri);
//            System.out.println("Image Path : " + selectedImagePath);
//            profilePictureImageView.setImageURI(selectedImageUri);
//
//            /*Extract colors from the image selected*/
//            /*To write a general method*/
//            Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                @Override
//                public void onGenerated(Palette palette) {
//                    int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
//                    int darkMutedColor = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
//
//                    /*Set color for status bar*/
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Window window = getWindow();
//                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                        window.setStatusBarColor(mutedColor);
//                    }
//
//                    /*Set Collapsible toolbar color*/
//                    collapsingToolbar.setContentScrimColor(mutedColor);
//                }
//            });
//        }
//    }
         @Override
         protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);

                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                profilePictureImageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

                String phone_number = pref.getString("phone_number","");
                ArrayList<NameValuePair >params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone_number", phone_number));
                params.add(new BasicNameValuePair("image", imgDecodableString));
                ServerRequest sr=new ServerRequest();
                JSONObject json = sr.getJSON(Config.ip + "/editProfile/image", params);
                //   JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass/chg", params);

                if (json != null) {
                    try {

                        String jsonstr = json.getString("response");
                        Toast.makeText(MainActivity_ProfilePage.this, ""+jsonstr, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }





            /*Extract colors from the image selected*/
            /*To write a general method*/
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                        int darkMutedColor = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));

                    /*Set color for status bar*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(mutedColor);
                        }

                    /*Set Collapsible toolbar color*/
                        collapsingToolbar.setContentScrimColor(mutedColor);
                    }
                });

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    /*initialise the profile attributes*/
    private void initialiseProfile() {
        profile = new ArrayList<ProfileClass>();
        for (int i = 0; i < userInfoDescription.length; i++) {
            profile.add(new ProfileClass(userInfoIcons[i], userInfoTitle[i], userInfoDescription[i]));
        }
    }

    /*To get path of image from gallery*/
    public String getPath(Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /*Color toolbars*/
    private void colorToolbars() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                profilePicture);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                int darkMutedColor = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
                /*Set color for status bar*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(mutedColor);
                }
                /*Set Collapsible toolbar color*/
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });
    }
}
