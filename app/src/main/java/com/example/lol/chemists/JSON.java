package com.example.lol.chemists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class JSON {

    final static String GETACTUALJSON = "Prescription";
    final static String ID = "id";
    final static String PATIENT_ID = "patient_id";
    final static String PRESCRIPTION = "prescription";
    final static String TIMESTAMP = "timestamp";
    final static String DOCTORNAME = "Doctor_Name";

    /**
     * Gets the actual JSON you have to process on. Use this on the response
     * given by the REST API and then process the returned JSON object further
     *
     * @throws JSONException
     */

    public static JSONObject getActualPrescription(String JSONResponse)
            throws JSONException {

        JSONObject JSONtoProcess = new JSONObject(JSONResponse);

        try {
            JSONtoProcess = JSONtoProcess.getJSONObject(GETACTUALJSON);
        } catch (Exception e) {
            return JSONtoProcess;
        }

        return JSONtoProcess;

    }

    public static JSONArray getListOfAllPrescriptions(String JSONResponse)
            throws JSONException {

        JSONObject actualPresData = new JSONObject(JSONResponse);
        JSONArray presList = actualPresData.getJSONArray(GETACTUALJSON);

        return presList;
    }

    // for parsing list of prescriptions from api
    // Prescription/api/v1.0/prescription/3

    public static HashMap<String, JSONObject> returnMapofMedicine(
            String JSONResponse) throws JSONException {

        JSONObject actualJSON;


        actualJSON = getActualPrescription(JSONResponse);

        String prescriptionJSON = actualJSON.getString(PRESCRIPTION);
        JSONObject prescription = new JSONObject(prescriptionJSON);

        JSONObject medicine = prescription.getJSONObject("Medicines");
        // System.out.println(medicine.toString());

        HashMap<String, JSONObject> mapOfMedicineKey = jsonToMap(medicine);
        return mapOfMedicineKey;
    }


    public static List<String> returnListofMedicineNames(String JSONResponse)
            throws JSONException {

        HashMap<String, JSONObject> MapofMedicine;

        // try catch to handle if the json is from api all prescriptions or only
        // latest prescriptions

        // try{
        MapofMedicine = returnMapofMedicine(JSONResponse);
        // }catch(Exception e){
        // MapofMedicine = returnMapofMedicineAlternate(JSONResponse);
        // }
        List<String> medicineNames = new ArrayList<>();
        medicineNames.addAll(MapofMedicine.keySet());

        return medicineNames;
    }

    public static List<JSONObject> returnListofIndividualMedicineinfo(
            String JSONResponse) throws JSONException {

        HashMap<String, JSONObject> MapofMedicine = returnMapofMedicine(JSONResponse);
        List<JSONObject> medicineIndividualInfo = new ArrayList<>();

        for (Entry<String, JSONObject> entry : MapofMedicine.entrySet()) {
            // System.out.println(entry.getKey());
            // System.out.println(entry.getValue());
            medicineIndividualInfo.add(entry.getValue());
        }

        return medicineIndividualInfo;

    }

    public static HashMap<String, Integer> chemistMedicineListHashMap(
            String JSONResponse) throws JSONException {

        HashMap<String, JSONObject> MapofMedicine = returnMapofMedicine(JSONResponse);
        HashMap<String, Integer> mapViewChemist = new HashMap<>();

        for (Entry<String, JSONObject> entry : MapofMedicine.entrySet()) {

            mapViewChemist.put(entry.getKey(),
                    extractQuantityfromMedicine(entry.getValue()));
        }

        return mapViewChemist;
    }

    public static int retreiveTOTAL_QUANTITY(String JSONResponse)
            throws JSONException {

        return 0;
    }

    public static int extractQuantityfromMedicine(JSONObject t)
            throws JSONException {
        return t.getInt("Total_Quantity");

    }

    public static HashMap<String, JSONObject> parseMedicines(JSONObject t)
            throws JSONException {

        HashMap<String, JSONObject> map = new HashMap<>();
        JSONObject jObject = t;
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {

            String key = (String) keys.next();

            try {
                JSONObject value = jObject.getJSONObject(key);
                map.put(key, value);
            } catch (Exception e) {
                continue;
            }

        }

        return map;
    }

    public static HashMap<String, JSONObject> jsonToMap(JSONObject t)
            throws JSONException {

        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
        JSONObject jObject = t;
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            JSONObject value = jObject.getJSONObject(key);
            map.put(key, value);

        }

        return map;
    }

    public static String getNameofDoctor(String JSONResponse) throws JSONException {

        JSONObject actualJSON = getActualPrescription(JSONResponse);

        JSONObject patient_id = new JSONObject(actualJSON.getString(PRESCRIPTION));
        String doctorName = patient_id.getString(DOCTORNAME);
        System.out.println(doctorName);

        return doctorName;
    }
}