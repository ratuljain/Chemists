package com.example.lol.chemists;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lol.chemists.adapter.MedicineAdapter;
import com.example.lol.chemists.model.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientDetails extends AppCompatActivity {

    private final String LOG_TAG = PatientDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Medicine> arrayOfUsers = new ArrayList<>();
        final HashMap<String, Integer> list;

//        TextView temp1 = (TextView) findViewById(R.id.docNamePatientDetails);
//        temp1.setText("My Awesome Text");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String s = getIntent().getStringExtra("mapJSON");

        try {
            list = JSON.chemistMedicineListHashMap(s);

            for (Map.Entry<String, Integer> entry : list.entrySet()){
                String name = entry.getKey();
                String quantity = Integer.toString(entry.getValue());

                arrayOfUsers.add(new Medicine(name, quantity));

//                Log.v(LOG_TAG, name + " - " + quantity);
            }

// Create the adapter to convert the array to views
            MedicineAdapter adapter = new MedicineAdapter(this, arrayOfUsers);
// Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Something is wrong with JSON", e);
        }

//        Button buttonOne = (Button) findViewById(R.id.btn);
//        buttonOne.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                //Do stuff here
////                Log.v("Intentclass", s);
//                Toast.makeText(PatientDetails.this, s,
//                        Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


    }
}
