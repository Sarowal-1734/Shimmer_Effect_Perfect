package com.example.shimmereffectperfect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ShimmerFrameLayout shimmerContainer;
    MobileAdapter adapter;
    ArrayList<MobileModel> mobileInfoList = new ArrayList<>();
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        shimmerContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

        // Personal API
        String api = "https://jsonkeeper.com/b/CF6J";

        new mobileTask().execute(api);  //Creating asyncTask to get data from web/api

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                //recreate();
                //or fetch data again
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //Creating asyncTask to get data from web/api
    public class mobileTask extends AsyncTask<String, Void, ArrayList<MobileModel>> {

        @Override
        protected ArrayList<MobileModel> doInBackground(String... strings) {
            String s = strings[0];
            try {
                URL url = new URL(s);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();  //get byte code
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //byte code to char
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //partially display
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                if ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String JSONFile = stringBuilder.toString(); //stores all data to JSONFile
                JSONArray jsonArray = new JSONArray(JSONFile);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MobileModel mobileInfo = new MobileModel();
                    mobileInfo.setModel(jsonObject.getString("model"));
                    mobileInfo.setPrice(jsonObject.getString("price")); //use int as String other app crashes!
                    mobileInfo.setImage(jsonObject.getString("image"));

                    mobileInfoList.add(mobileInfo);
                }
                return mobileInfoList; //return list of data to onPostExecute
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MobileModel> mobileModels) {
            super.onPostExecute(mobileModels);

            // Shimmer effect closing
            shimmerContainer.stopShimmer();
            shimmerContainer.setVisibility(View.GONE);
            // Make visible the recyclerView
            swipeRefreshLayout.setVisibility(View.VISIBLE);

            // Setup recyclerView
            adapter = new MobileAdapter(MainActivity.this, mobileInfoList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
}