package com.example.lol.chemists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.assent.AssentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AssentActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GCMCheck";
    private AccountHeader headerResult = null;
    String token;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.healthcare_cropped)
                .withSavedInstance(savedInstanceState)
                .build();


        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withName("View Prescription").withIdentifier(1);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Orders").withIdentifier(2);

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentNavigationBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D


                        if (drawerItem.getIdentifier() == 1) {
                            Intent myIntent = new Intent(getApplicationContext(), GCMCheck.class);
                            startActivity(myIntent);
                        } else if (drawerItem.getIdentifier() == 2) {
                            Toast.makeText(MainActivity.this, "this is my Toast message!!! =)",
                                    Toast.LENGTH_LONG).show();
                        }

                        return true;
                    }
                })
                .build();


//        if (!Assent.isPermissionGranted(Assent.READ_PHONE_STATE)) {
//            // The if statement checks if the permission has already been granted before
//            Assent.requestPermissions(new AssentCallback() {
//                @Override
//                public void onPermissionResult(PermissionResultSet result) {
//                    // Permission granted or denied
//                }
//            }, 69, Assent.READ_PHONE_STATE);
//        }
//
//
//        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String deviceId = deviceUuid.toString();
//
//        Log.v("MainActivityId", deviceId);
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("UNIQUE_DEVICE_ID", deviceId);
//        editor.apply();
//
//
//        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }
//
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String devID = settings.getString("UNIQUEID", "no string found");
//        String regToken = settings.getString("GCM_REG_TOKEN", "Reg token not found");
//
////        Log.v("SharedPrefReturnedVal", devID);
////        Log.v("SharedPrefReturnedVal", regToken);
//
//        HashMap<String, String> mapRegToken = new HashMap<>();
//
//        mapRegToken.put("device_id", devID);
//        mapRegToken.put("registrationToken", regToken);
//
//        new JSONObject(mapRegToken).toString();
//
//        Log.v("SharedPrefReturnedVal", new JSONObject(mapRegToken).toString());
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


}
