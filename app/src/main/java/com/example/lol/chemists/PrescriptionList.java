package com.example.lol.chemists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PrescriptionList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        private final String LOG_TAG = CallAPI.class.getSimpleName();


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String patientDetails;

            try {

                String ip = "http://104.131.46.2:5000/Prescription/api/v1.0/prescription/" + params[0].trim();
//                String ip = "http://10.0.3.2:5000/Prescription/api/v1.0/prescription/" + params[0].trim();

                URL url = new URL(ip);

                Log.v(LOG_TAG, ip);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                patientDetails = buffer.toString();

                Log.v(LOG_TAG, (patientDetails));

                HashMap<String, Integer> test = JSON.chemistMedicineListHashMap(patientDetails);

                for (Map.Entry<String,Integer> entry : test.entrySet()) {
                    String key = entry.getKey();
                    String value = Integer.toString(entry.getValue());
                    // do stuff
                    Log.v(LOG_TAG, key + " - " + value );
                }

            } catch (Exception e) {

                System.out.println(e.getMessage());

                return e.getMessage();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return patientDetails;

        }

//        @Override
//        protected void onPostExecute(String s) {
//            Toast.makeText(getActivity(), s,
//                    Toast.LENGTH_LONG).show();
//        }


    }

}
