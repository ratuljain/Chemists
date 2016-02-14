package com.example.lol.chemists;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PrescriptionList extends AppCompatActivity {

    private final String LOG_TAG = PrescriptionList.class.getSimpleName();
    String receivedJSONString;
    ArrayList<HashMap<String, Object>> mapForList;
    Toast mtoast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Prescription> arrayOfPrescriptions = new ArrayList<>();  // stores objects of prescription class

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String s = getIntent().getStringExtra("JSON");

        CallAPI c = new CallAPI();
        try {
            receivedJSONString = c.execute(s).get();
            mapForList = JSON.getHashMapforPrescriptionList(receivedJSONString);
        } catch (Exception ei) {
            ei.printStackTrace();
        }

        for (HashMap<String, Object> maps : mapForList) {
            String id = (String) maps.get("presID");
            String docName = (String) maps.get("DocName");
            Date date = (Date) maps.get("presDate");
            String mapJson = (String) maps.get("jsonFile");

//            HashMap<String, Integer> medQuantityMap = (HashMap<String, Integer>) maps.get("medQuantity");
//            String mapJson = medQuantityMap.toString();

            Log.v(LOG_TAG, "Doc name - " + docName);
            Log.v(LOG_TAG, "Pres date - " + date.toString().substring(0, 10));

            arrayOfPrescriptions.add(new Prescription(docName, date, id, mapJson));
        }


        //Do stuff here
        // Create the adapter to convert the array to views
        PrescriptionListAdapter prescriptionViewAdapter = new PrescriptionListAdapter(this, arrayOfPrescriptions);
        // Attach the adapter to a ListView
        ListView preslistView = (ListView) findViewById(R.id.listviewprescription);
        preslistView.setAdapter(prescriptionViewAdapter);

        preslistView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                HashMap<String, Integer> medQuantityMap = new HashMap<>();
                TextView temp1 = (TextView) view.findViewById(R.id.medListJson);
                TextView temp2 = (TextView) view.findViewById(R.id.docName);

                String mapJson = temp1.getText().toString();
                String docName = temp2.getText().toString();

//                for (HashMap<String, Object> maps : mapForList){
//                    if(maps.containsKey(idKey)){
//                        medQuantityMap = (HashMap<String, Integer>) maps.get(idKey);
//                        mapJson = new JSONObject(medQuantityMap);
//                    }
//                    else
//                        continue;
//                }
//                    Toast.cancel();
//                    Toast.makeText(getApplicationContext(),str + " is pressed " + position,Toast.LENGTH_SHORT).show();


//                showAToast(mapJson + " is pressed " + position);
                Intent myIntent = new Intent(getApplicationContext(), PatientDetails.class);
                Bundle extras = new Bundle();
                extras.putString("mapJSON", mapJson);
                extras.putString("docName", docName);
                myIntent.putExtras(extras);
//                myIntent.putExtra("mapJSON", mapJson); //Optional parameters
                startActivity(myIntent);

            }
        });

    }

    public void showAToast(String message) {
        if (mtoast != null) {
            mtoast.cancel();
        }
        mtoast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mtoast.show();
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

//                Log.v(LOG_TAG, patientDetails);

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
