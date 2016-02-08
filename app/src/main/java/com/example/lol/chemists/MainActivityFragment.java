package com.example.lol.chemists;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    String receivedJSONString;
    EditText PatientName;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final TextInputLayout usernameWrapper = (TextInputLayout) rootView.findViewById(R.id.usernameWrapper);
        usernameWrapper.setHint("Enter Patient's name");

        PatientName = (EditText) rootView.findViewById(R.id.username);

        Button buttonOne = (Button) rootView.findViewById(R.id.btn);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                String name = PatientName.getText().toString();
                //Do stuff here
                CallAPI c = new CallAPI();
                try {
                    receivedJSONString = c.execute(name).get();
                    Log.v("MainMethod", receivedJSONString);
                }
                catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }
                Intent myIntent = new Intent(getActivity(), PatientDetails.class);
//                ArrayList<Integer> a = new ArrayList<Integer>();
                myIntent.putExtra("JSON", receivedJSONString); //Optional parameters
                startActivity(myIntent);
            }
        });

        Button prescriptionView = (Button) rootView.findViewById(R.id.prescriptionView);
        prescriptionView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = new Intent(getActivity(), PrescriptionList.class);
//                myIntent.putExtra("JSON", receivedJSONString); //Optional parameters
                startActivity(myIntent);
            }
        });


        return rootView;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FetchData();
//    }

    public void FetchData() {
        CallAPI c = new CallAPI();
        c.execute();
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
