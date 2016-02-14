package com.example.lol.chemists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by lol on 12/02/16.
 */
public class PrescriptionListAdapter extends ArrayAdapter<Prescription> {
    // View lookup cache
    private static class ViewHolder {
        TextView docname;
        TextView date;
        TextView id;
        TextView mapJson;

    }

    public PrescriptionListAdapter(Context context, ArrayList<Prescription> prescriptions) {
        super(context, R.layout.prescription_list_element_layout, prescriptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Prescription prescription = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.prescription_list_element_layout, parent, false);
            viewHolder.docname = (TextView) convertView.findViewById(R.id.docName);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.id = (TextView) convertView.findViewById(R.id.presID);
            viewHolder.mapJson = (TextView) convertView.findViewById(R.id.medListJson);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.docname.setText(prescription.docname);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        viewHolder.date.setText(df.format(prescription.date).toString());
        viewHolder.id.setText(prescription.id);
        viewHolder.mapJson.setText(prescription.mapJson);
        // Return the completed view to render on screen
        return convertView;
    }
}