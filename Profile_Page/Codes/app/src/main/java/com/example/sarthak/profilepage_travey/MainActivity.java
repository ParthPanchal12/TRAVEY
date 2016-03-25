package com.example.sarthak.profilepage_travey;

import android.content.Intent;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Customising Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_ProfilePage);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_ProfilePage);
        collapsingToolbar.setTitle(nameOfUser);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout_profilePage);
        appBarLayout.setExpanded(true);

        /*If clicked on Profile Pic then show the photo*/
        appBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen == true)
                    animateFAB();
                Intent intent = new Intent(MainActivity.this, ViewProfileImage.class);
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
        adapter = new Profile_RecyclerViewAdapter(profile, MainActivity.this);
        recyclerView.setAdapter(adapter);

        /*Adding click listener for each item of recycler view*/
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (profile.get(position).getTitle().equals("Rating")) {
                            /*If rating then do not allow to change*/
                            Toast.makeText(MainActivity.this, "Can not edit Ratings", Toast.LENGTH_SHORT).show();
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
                            } else {
                                /*Call the EditProfile class to let the user edit his/her detail*/
                                Intent intent = new Intent(MainActivity.this, EditProfile.class);
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
                Intent intent = new Intent(MainActivity.this, EditProfile.class);
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
                galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            /*Extract path from the image selected*/
            Uri selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            System.out.println("Image Path : " + selectedImagePath);
            profilePictureImageView.setImageURI(selectedImageUri);

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
