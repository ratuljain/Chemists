package com.example.lol.chemists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PrescriptionList extends AppCompatActivity {

    private final String LOG_TAG = PrescriptionList.class.getSimpleName();
    String receivedJSONString;
    EditText PatientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        usernameWrapper.setHint("Enter Patient's name");

        PatientName = (EditText) findViewById(R.id.username);

        Button buttonOne = (Button) findViewById(R.id.btn);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                String name = PatientName.getText().toString();
                //Do stuff here
                CallAPI c = new CallAPI();
                try {
                    receivedJSONString = c.execute(name).get();
                    Log.v(LOG_TAG, receivedJSONString);
                    JSONArray presList = JSON.getListOfAllPrescriptions(receivedJSONString);
                    Log.v(LOG_TAG, Integer.toString(presList.length()));

                    for (int i = 0; i < presList.length(); i++) {
                        HashMap<String, Integer> test = JSON.chemistMedicineListHashMap(presList.getJSONObject(i).toString());

                        for (Map.Entry<String, Integer> entry : test.entrySet()) {
                            String key = entry.getKey();
                            String value = Integer.toString(entry.getValue());
                            // do stuff
                            Log.v(LOG_TAG, key + " - " + value);
                        }
                    }
                } catch (Exception ei) {
                    ei.printStackTrace();
                }

            }
        });
    }

    private class CallAPI extends AsyncTask<String, String, String> {

//        private final String LOG_TAG = CallAPI.class.getSimpleName();


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String patientDetails;

            try {

                String ip = "http://104.131.46.2:5000/Prescription/api/v1.0/prescription/all/" + params[0].trim();
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

                Log.v(LOG_TAG, patientDetails);

            } catch (Exception e) {

                Log.e(LOG_TAG, e.getMessage());

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
