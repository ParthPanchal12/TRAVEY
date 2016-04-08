package com.example.sarthak.navigationdrawer.ContactDisplay;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity_Contacts extends AppCompatActivity {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Friends> friends;
    private int inProgress = 0;
    private ProgressDialog progressBar;
    private Handler progressBarHandler = new Handler();
    private Button retryButton;
    private RelativeLayout retryRelativeLayout;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display_contacts);
        friends = new ArrayList<>();


        /*To add menu item refresh icon*/

        /*Progress Bar*/
        progressBar = new ProgressDialog(MainActivity_Contacts.this);
        progressBar.setMessage("Getting Contact List ...");
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        retryRelativeLayout = (RelativeLayout) findViewById(R.id.layout_retry_contact_display);
        retryButton = (Button) findViewById(R.id.button_retry);

        new GetContactsAsync().execute();


        adapter = new RecycleViewAdapter(MainActivity_Contacts.this, friends);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_all_contacts);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity_Contacts.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        /*Item click listener*/

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity_Contacts.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Toast.makeText(MainActivity_Contacts.this, "Clicked on" + position, Toast.LENGTH_SHORT).show();

                    }
                })
        );

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetContactsAsync().execute();
            }
        });


        /*Search View*/
        searchView=(SearchView) findViewById(R.id.search_Contacts);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });




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
            //Toast.makeText(MainActivity_Contacts.this, sb, Toast.LENGTH_SHORT).show();
        }

        alphabeticalSorting();
        inProgress = 1;
    }

    class GetContactsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getAllContacts();
            Log.d("Ser", "" + friends.size());
            return null;
        }

        @Override
        protected void onPreExecute() {
            inProgress = 0;
            super.onPreExecute();
            progressBar.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (inProgress == 1) {
                retryRelativeLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                friends = new ArrayList<>();
                adapter.notifyDataSetChanged();
                retryRelativeLayout.setVisibility(View.VISIBLE);
            }
            progressBar.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressBar != null) {
            progressBar.dismiss();
        }
    }
}

