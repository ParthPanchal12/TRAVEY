package com.example.sarthak.navigationdrawer.ContactDisplay;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.GCM.App;
import com.example.sarthak.navigationdrawer.LeaderBoard.User;
import com.example.sarthak.navigationdrawer.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity_Contacts extends AppCompatActivity {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Friends> friends;
    private ArrayList<Friends> actualFriends;
    private int inProgress = 0;
    private Button retryButton;
    private RelativeLayout retryRelativeLayout;
    private SearchView searchView;
    private String selectedFriend;
    private ArrayList<User> allDatabaseUsers;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    private SweetAlertDialog progressBar;
    private ArrayList<Friends> friendsFromSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display_contacts);
        friends = new ArrayList<>();

        pref = this.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        edit = pref.edit();


        /*To add menu item refresh icon*/

        /*Progress Bar*/
        progressBar = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressBar.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressBar.setTitleText("Getting your contacts");
        progressBar.setCancelable(false);


        actualFriends = new ArrayList<>();

        allDatabaseUsers = new ArrayList<>();
        getDatabaseContacts();

        retryRelativeLayout = (RelativeLayout) findViewById(R.id.layout_retry_contact_display);
        retryButton = (Button) findViewById(R.id.button_retry);


        adapter = new RecycleViewAdapter(MainActivity_Contacts.this, actualFriends);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_all_contacts);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity_Contacts.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        new GetContactsAsync().execute();

        /*Item click listener*/

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity_Contacts.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Toast.makeText(MainActivity_Contacts.this, "Clicked on" + adapter.friends.get(position).getName(), Toast.LENGTH_SHORT).show();
                        String phone = adapter.friends.get(position).getPhone();
                        String acc_phone = "";
                        if (phone.length() == 11) {
                            acc_phone = "" + phone.charAt(1) + phone.charAt(2) + phone.charAt(3) + phone.charAt(4) + phone.charAt(5) + phone.charAt(6) + phone.charAt(7) + phone.charAt(8) + phone.charAt(9) + phone.charAt(10);
                        } else if (phone.length() == 13) {
                            acc_phone = "" + phone.charAt(3) + phone.charAt(4) + phone.charAt(5) + phone.charAt(6) + phone.charAt(7) + phone.charAt(8) + phone.charAt(9) + phone.charAt(10) + phone.charAt(11) + phone.charAt(12);
                        }
                        selectTypeForFriend(acc_phone);
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
        searchView = (SearchView) findViewById(R.id.search_Contacts);
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
        if (actualFriends != null && actualFriends.size() > 0) {
            Collections.sort(actualFriends, new Comparator<Friends>() {
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

                    }

                    emailCur.close();
                }

                if (image_uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(),
                                        Uri.parse(image_uri));
                        sb.append("\n Image in Bitmap:" + bitmap);


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


        getCommonContacts();
        alphabeticalSorting();
        inProgress = 1;
    }

    private void getDatabaseContacts() {

        /*Take all the users from the database*/
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Config.phone_number, pref.getString(Config.phone_number, "")));
        ServerRequest sr = new ServerRequest(MainActivity_Contacts.this);
        Log.d("Contacts", "params sent");
        //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
        JSONArray json = sr.getJSONArray(Config.ip + "/allContacts", params);
        Log.d("here", "json received1" + json);
        if (json != null) {
            try {
                Log.d("JsonAllContacts", "" + json);
                for (int i = 0; i < json.length(); i++) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(json.getString(i), new TypeToken<User>() {
                    }.getType());
                    allDatabaseUsers.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    private void getCommonContacts() {

        for (int i = 0; i < friends.size(); i++) {
            int flag = 0;
            for (int j = 0; j < allDatabaseUsers.size(); j++) {
                if (friends.get(i).getPhone().contains(allDatabaseUsers.get(j).getPhone_number())) {
                    flag = 1;
                }
            }
            if (flag == 1) {
                actualFriends.add(friends.get(i));
            }
        }


    }

    class GetContactsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getAllContacts();
            inProgress = 1;
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
                adapter.notifyDataSetChanged();
                retryRelativeLayout.setVisibility(View.GONE);
            } else {
                actualFriends.clear();
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

    String reg_id = "";

    private void selectTypeForFriend(final String phone) {
        String methodsToTakeSource[] = {"Share your location", "Get his location"};


        new MaterialDialog.Builder(MainActivity_Contacts.this)
                .title("Select an option")
                .items(methodsToTakeSource)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which) {
                            case 0:
                                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair(Config.phone_number, phone));
//                                params.add(new BasicNameValuePair(Config.user_name, pref.getString(Config.user_name, "")));
                                ServerRequest sr = new ServerRequest(MainActivity_Contacts.this);
                                Log.d("here", "" + phone);
                                //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
                                JSONObject jsonObject = sr.getJSON(Config.ip + "/getGcm", params);
                                Log.d("TAgs", "" + jsonObject);
                                if (jsonObject != null) {
                                    try {

                                        reg_id = jsonObject.getString(Config.gcmId);
                                        Log.d("temp", reg_id);
                                        new Async_Task().execute();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case 1:
                                Intent intent = new Intent(MainActivity_Contacts.this, DisplayFriendsOnMap.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();

    }

    class Async_Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            App gcmApp = new App();
            gcmApp.sendNotification(reg_id, pref.getString(Config.user_name, "") + " shared his location with you", "TRAVEY speaking..");
            return null;
        }
    }
}

