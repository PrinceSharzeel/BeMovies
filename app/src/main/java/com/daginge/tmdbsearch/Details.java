package com.daginge.tmdbsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Details extends AppCompatActivity {
    InputStream stream = null; String query;
    String country,dname,dbackdrop,dbud,doverview,did,dpop,dpath,adult,dstat,dvot,drd;
    public static final String EXTRA_QUERY = "com.daginge.tmdbsearch.QUERY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);




        Intent intent = getIntent();

        query= intent.getStringExtra(TMDBSearchResultActivity.EXTRA_QUERY);
        TextView name=(TextView)findViewById(R.id.tvNumber1);
        //name.setText(query);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBQueryManager().execute(query);
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
        }

    }








    public void updateViewWithResults(final ArrayList<MovieResult> result) {
   int position=1;
        TextView name=(TextView)findViewById(R.id.tvNumber1);
        TextView status=(TextView)findViewById(R.id.tvNumber6);
        TextView popularity=(TextView)findViewById(R.id.popularity);
        TextView bud=(TextView)findViewById(R.id.tvNumber2);
        TextView overview=(TextView)findViewById(R.id.overview);
        TextView vot=(TextView)findViewById(R.id.vote);
        ImageView dp=(ImageView)findViewById(R.id.dp);
        TextView ad=(TextView)findViewById(R.id.tvn);
      TextView rdate=(TextView)findViewById(R.id.status);
        name.setText(dname);
        rdate.setText(drd);
        vot.setText("Votes: "+dvot);
        status.setText(dstat);
        popularity.setText("Popularity "+dpop);
        bud.setText("Budget: "+dbud);
        overview.setText(doverview);
        if(adult=="false")ad.setText("Adult:U/A");else ad.setText("Adult:A");

        Log.e("reults",result.toString());
        Picasso.with(this).load("https://image.tmdb.org/t/p/w640/"+dpath).fit().into(dp);
        Log.e("dsf","https://image.tmdb.org/t/p/w640/"+dpath);

        FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);
        fb.setBackgroundColor(Color.YELLOW);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Details.this,"Watch Trailer",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Details.this,Trailer.class);


                intent.putExtra(EXTRA_QUERY,query);
                startActivity(intent);
            }
        });

    }

    private class TMDBQueryManager extends AsyncTask {

        private final String TMDB_API_KEY = "ef68bfed72780ce7ae801b9daba23069";
        private static final String DEBUG_TAG = "TMDBQueryManager";

        @Override
        protected ArrayList<MovieResult> doInBackground(Object... params) {
            try {
                return searchIMDB((String) params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            updateViewWithResults((ArrayList<MovieResult>) result);
        };

        /**
         * Searches IMDBs API for the given query
         * @param query The query to search.
         * @return A list of all hits.
         */
        public ArrayList<MovieResult> searchIMDB(String query) throws IOException {
            // Build URL
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/movie/"+query);
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
            URL url = new URL(stringBuilder.toString());
            Log.e("sffe",url.toString());


            try {
                // Establish a connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());

                stream = conn.getInputStream();
                return parseResult(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }
        private ArrayList<MovieResult> parseResult(String result) {
            String streamAsString = result;
            Log.e("DDresults wali string",result);
            ArrayList<MovieResult> results = new ArrayList<MovieResult>();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);





                doverview = jsonObject.getString("overview");
               adult= jsonObject.getString("adult");
                dbud = jsonObject.getInt("budget")+"$";
                dpath= jsonObject.getString("backdrop_path");
                dpop= jsonObject.getString("popularity");
                dstat= jsonObject.getString("status");
                drd= jsonObject.getString("release_date");
                dvot= jsonObject.getString("vote_count");
                Log.e("dpop",adult);
                Log.e("iddd",dbud);


            } catch (JSONException e) {
                System.err.println(e);
                Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
            }
            return results;
        }

















        public String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }


}
