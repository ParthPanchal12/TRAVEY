package com.example.sarthak.contactdisplay;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Friends> friends;

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        friends = new ArrayList<>();



        /*To add search box*/

        /*Progress Bar*/
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Getting Contact List ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();


        progressBarStatus = 0;

        progressBar.onStart();
//        if (progressBarStatus < 100) {
//
//
//            progressBarHandler.post(new Runnable() {
//                public void run() {
//                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
//                    try {
//                        Thread.sleep(2000);
//                        progressBarStatus = 100;
//                        if (progressBarStatus >= 100) {
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            progressBar.dismiss();
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
        if(progressBarStatus<100){
            new Thread(new Runnable() {

                @Override
                public void run() {
                    getAllContacts();
                    progressBarStatus=100;
                    progressBar.dismiss();
                }
            }).start();
        }



        //       getAllContacts();

        adapter = new RecycleViewAdapter(MainActivity.this, friends);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_all_contacts);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        /*Item click listener*/

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Toast.makeText(MainActivity.this, "Clicked on" + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );


    }

    private void alphabeticalSorting() {
        if (friends != null && friends.size() > 0) {
            Collections.sort(friends, new Comparator<Friends>() {
                @Override
                public int compare(final Friends object1, final Friends object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
    }

    public void getAllContacts() {
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;
        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {
                Friends friend = new Friends();
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                image_uri = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);
                    friend.setName(name);
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                        friend.setPhone(phone);
                        System.out.println("phone" + phone);
                    }
                    pCur.close();
                    friends.add(friend);


                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        emailContact = emailCur
                                .getString(emailCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur
                                .getString(emailCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                        System.out.println("Email " + emailContact
                                + " Email Type : " + emailType);

                    }

                    emailCur.close();
                }

                if (image_uri != null) {
                    System.out.println(Uri.parse(image_uri));
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(),
                                        Uri.parse(image_uri));
                        sb.append("\n Image in Bitmap:" + bitmap);
                        System.out.println(bitmap);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


                sb.append("\n........................................");
            }
            //Toast.makeText(MainActivity.this, sb, Toast.LENGTH_SHORT).show();
        }

        alphabeticalSorting();
        progressBarStatus = 100;
    }


}
