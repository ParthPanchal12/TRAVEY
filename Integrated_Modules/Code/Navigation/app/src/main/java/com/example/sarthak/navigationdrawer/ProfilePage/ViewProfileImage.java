package com.example.sarthak.navigationdrawer.ProfilePage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.R;

/**
 * Created by sarthak on 23/3/16.
 */
public class ViewProfileImage extends AppCompatActivity {
    private Intent intent;
    private ImageView imageViewProfilePictureComplete;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_picture);
        toolbar = (Toolbar) findViewById(R.id.toolbar_ViewFullPhoto);
        setSupportActionBar(toolbar);
        setTitle("Photo");
        intent = getIntent();
        if (intent != null) {
            String pathOfImage = intent.getExtras().getString("Path_of_Image");
            imageViewProfilePictureComplete = (ImageView) findViewById(R.id.imageView_ViewUserPictureComplete);
            try {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeFile(pathOfImage);
                if (bitmap == null) {
                    Toast.makeText(ViewProfileImage.this, "Unable to display Image!!\nSomething went wrong!!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    imageViewProfilePictureComplete.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                Log.e("Error", "Error while setting up the picture for the imageView");
                e.printStackTrace();
                Toast.makeText(ViewProfileImage.this, "Unable to display Image!!\nSomething went wrong", Toast.LENGTH_SHORT).show();
                onDestroy();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
