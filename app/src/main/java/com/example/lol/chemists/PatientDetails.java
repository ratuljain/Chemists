package com.example.lol.chemists;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String s = getIntent().getStringExtra("JSON");

        try {
            list = JSON.chemistMedicineListHashMap(s);

            for (Map.Entry<String, Integer> entry : list.entrySet()){
                String name = entry.getKey();
                String quantity = Integer.toString(entry.getValue());

                arrayOfUsers.add(new Medicine(name, quantity));

//                Log.v(LOG_TAG, name + " - " + quantity);

            }
//            list = new ArrayList<>();
//
//            list.add("Name 1");
//            list.add("Name 2");
//            list.add("Name 3");



// Create the adapter to convert the array to views
            MedicineAdapter adapter = new MedicineAdapter(this, arrayOfUsers);
// Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(adapter);

//            final ListView listview = (ListView) findViewById(R.id.listview);
//            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    R.layout.chemist_medicine_view_layout,
//                    R.id.title,
//                    list);
//
//            listview.setAdapter(adapter);
//            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                @Override
//                public void onItemClick(AdapterView<?> parent, final View view,
//                                        int position, long id) {
//                    final String item = (String) parent.getItemAtPosition(position);
//                    view.animate().setDuration(2000).alpha(0)
//                            .withEndAction(new Runnable() {
//                                @Override
//                                public void run() {
//                                    list.remove(item);
//                                    adapter.notifyDataSetChanged();
//                                    view.setAlpha(1);
//                                }
//                            });
//                }
//
//            });

        } catch (Exception e) {
            Log.e(LOG_TAG, "Something is wrong with JSON", e);
        }

        Button buttonOne = (Button) findViewById(R.id.btn);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
//                Log.v("Intentclass", s);
                Toast.makeText(PatientDetails.this, s,
                        Toast.LENGTH_LONG).show();
            }
        });


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
