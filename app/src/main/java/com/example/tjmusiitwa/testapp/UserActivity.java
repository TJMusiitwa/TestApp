package com.example.tjmusiitwa.testapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UserActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> userList;
    private String TAG = UserActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetUser().execute();
    }

    private class GetUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(UserActivity.this, "JSON Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            UserHandler hh = new UserHandler();
            String url = "https://api.myjson.com/bins/a9eh1";
            URL myURL = null;
            try {
                myURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String jsonStr = null;
            try {
                jsonStr = hh.makeHttpRequest(myURL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray user = jsonObject.getJSONArray("0");

                    for (int i = 0; i < user.length(); i++) {
                        JSONObject u = user.getJSONObject(i);
                        String name = u.getString("name");
                        String age = u.getString("age");
                        String phonenumber = u.getString("phone");
                        String company = u.getString("company");
                        String email = u.getString("email");
                        String balance = u.getString("balance");
                        String picture = u.getString("picture");

                        HashMap<String, String> user_info = new HashMap<>();
                        user_info.put("name", name);
                        user_info.put("age", age);
                        user_info.put("phone", phonenumber);
                        user_info.put("company", company);
                        user_info.put("email", email);
                        user_info.put("balance", balance);

                        userList.add(user_info);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "JSON Parsing Error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "JSON Parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't connect so server");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(UserActivity.this, userList,
                    R.layout.list_item, new String[]{"name", "phone"},
                    new int[]{R.id.Name, R.id.Age, R.id.PhoneNumber});
            lv.setAdapter(adapter);
        }

    }
}