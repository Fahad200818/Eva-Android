package com.tracking.store;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.Model;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tracking.store.db.Store;
import com.tracking.store.db.StoreVisit;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.fragment.CompitativeStockFragment;
import com.tracking.store.fragment.InventoryTakingFragment;
import com.tracking.store.fragment.OrderTakingFragment;
import com.tracking.store.util.DBToObject;
import com.tracking.store.util.GPSValue;
import com.tracking.store.util.Util;
import com.tracking.store.view.ContactPersonDialog;
import com.tracking.store.view.NotesDialog;
import com.tracking.store.web.HttpCaller;
import com.tracking.store.web.WebURL;

import org.json.JSONObject;

import im.delight.android.location.SimpleLocation;


public class StoreDetailActivity extends AppCompatActivity {

    public Util utilInstance = Util.getInstance();
    private HttpCaller httpCaller = HttpCaller.getInstance();
    private GPSValue gpsValueInstance = GPSValue.getInstance();
    private DBToObject dbToObject = DBToObject.getInstance();
    private DBHandler dbHandler = DBHandler.getInstance();

    public static Store storeInfo;
    public static String Status;
    public Button btnCheckOut;
    public LinearLayout contentCheckin;
    public ContactPersonDialog contactDialog;
    public NotesDialog notesDialog;
    public static StoreVisit storeVisit = null;

    public JSONObject jsonObject;
    public Location locationInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCheckOut = (Button) toolbar.findViewById(R.id.toolbar_checkin);
        contentCheckin = (LinearLayout) findViewById(R.id.contentCheckin);

        storeVisit = new StoreVisit();

        locationInstance = gpsValueInstance.getLocation();

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notesDialog = new NotesDialog(StoreDetailActivity.this, new NotesDialog.ContactCallback() {
                    @Override
                    public void buttonPress(View v, String notes) {
                        notesDialog.dismiss();

                        boolean isInternetConnectionEnabled = utilInstance.isNetworkConnected(StoreDetailActivity.this);
                        int frequencyType =  utilInstance.getFrequencyType(storeInfo.FrequencyType);

                        String status = "";

                        if(Status.equals("Target")) {
                            status = "Target";
                            String nextScheduledVisit = utilInstance.getDateByFrequency(storeInfo.Frequency, frequencyType);
                            String nextScheduledForApp = utilInstance.getDateToString(nextScheduledVisit);
                            storeInfo.NextScheduledVisit = nextScheduledVisit;
                            storeInfo.NextScheduledForApp = nextScheduledForApp;

                        }else if(Status.equals("Extra")){
                            String nextScheduledVisit_ = storeInfo.NextScheduledVisit;
                            String nextScheduledForApp_ = utilInstance.getDateToString(nextScheduledVisit_);
                            status = "Out Of PGP";

                            storeInfo.NextScheduledVisit = nextScheduledVisit_;
                            storeInfo.NextScheduledForApp = nextScheduledForApp_;
                        }


                        storeVisit.EndTime = utilInstance.getCurrentTime();
                        storeVisit.Location = "";
                        storeVisit.Status = status;
                        storeVisit.Notes = notes;
                        storeVisit.save();



                        storeInfo.Status = "Achieved";
                        storeInfo.CheckOutDate = utilInstance.getCurrentDate();
                        storeInfo.save();


                        if (isInternetConnectionEnabled) {
                            jsonObject =  dbToObject.checkOutToJSONObject(storeVisit);
                            checkout(jsonObject);
                        }else{
                            ShopProfileAtivity.isCheckout = true;
                            Toast.makeText(StoreDetailActivity.this, "Check out successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
                notesDialog.show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Order Placed", OrderTakingFragment.class)
                .add("Current Inventory", InventoryTakingFragment.class)
                .add("Competitor Inventory", CompitativeStockFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        if (utilInstance.isGPSEnable(StoreDetailActivity.this)) {
            contactPopup();
        } else {
            Toast.makeText(StoreDetailActivity.this, "Please enable GPS", Toast.LENGTH_SHORT).show();
            finish();
        }
        checkOutBtn();
    }

    public void checkOutBtn() {
        btnCheckOut.setBackgroundResource(R.drawable.checkout_button);
        btnCheckOut.setText("Check Out");
        Drawable img = getResources().getDrawable(R.mipmap.ic_checkout);
        img.setBounds(0, 0, 40, 30);
        btnCheckOut.setCompoundDrawables(img, null, null, null);
    }

    public void contactPopup() {
        contactDialog = new ContactPersonDialog(StoreDetailActivity.this, storeInfo.StoreID, new ContactPersonDialog.ContactCallback() {
            @Override
            public void buttonPress(View view, String contactName, String contactNumber) {

                utilInstance.hideKeybaord(StoreDetailActivity.this, view);

                boolean isInternetConnection  = utilInstance.isNetworkConnected(StoreDetailActivity.this);

                String longitude = "0.0";
                String latitude = "0.0";
                if (locationInstance != null) {
                    longitude = locationInstance.getLongitude() + "";
                    latitude = locationInstance.getLatitude() + "";
                } else {
                    Toast.makeText(StoreDetailActivity.this, "Getting GPS value failed", Toast.LENGTH_SHORT).show();
                    contactDialog.dismiss();
                    finish();
                }

                int frequencyType =  utilInstance.getFrequencyType(storeInfo.FrequencyType);
                String nextScheduledVisit = utilInstance.getDateByFrequency(storeInfo.Frequency, frequencyType);

                long id = dbHandler.getUniqueID("StoreVisit");
                storeVisit.StoreVisitID = id;
                storeVisit.StartTime = utilInstance.getCurrentTime();
                storeVisit.StoreID = storeInfo.StoreID;
                storeVisit.ContactName = contactName;
                storeVisit.ContactNumber = contactNumber;
                storeVisit.Longitude = "" + latitude;
                storeVisit.Latitude = "" + longitude;
                storeVisit.Status = "Pending";
                storeVisit.NextScheduledVisit = "" + nextScheduledVisit;
                storeVisit.IsSync = isInternetConnection;
                storeVisit.save();

                if (isInternetConnection) {
                    jsonObject = dbToObject.checkInToJSONObject(storeVisit);
                    webCheckIn(jsonObject, storeVisit);
                }else{
                    Toast.makeText(StoreDetailActivity.this, "Check in successfully", Toast.LENGTH_SHORT).show();
                    contactDialog.dismiss();
                }
            }
        });
        contactDialog.show();
    }

    public void webCheckIn(JSONObject jsonObject, final StoreVisit storeVisit) {
        String url = WebURL.addStoreVisit;
        httpCaller.jsonObjectPOSTRequest(false, StoreDetailActivity.this, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (storeVisit != null) {
                    try {

                        int storeVisitId = response.getInt("storeVisitId");
                        storeVisit.StoreVisitID = storeVisitId;
                        storeVisit.IsSync = true;
                        storeVisit.save();

                        Toast.makeText(StoreDetailActivity.this, "Check in successfully", Toast.LENGTH_SHORT).show();
                        contactDialog.dismiss();

                    } catch (Exception e) {
                        contactDialog.dismiss();
                        Toast.makeText(StoreDetailActivity.this, "Check in failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        finish();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                contactDialog.dismiss();
                Toast.makeText(StoreDetailActivity.this, "Check In failed", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                finish();
            }
        });
    }

    public void checkout(JSONObject jsonObject) {
        final String url = WebURL.updateStoreVisit;
        httpCaller.jsonObjectPOSTRequest(false, StoreDetailActivity.this, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (storeVisit != null) {
                    try {
                        int storeVisitId = response.getInt("storeVisitId");
                        storeVisit.StoreVisitID = storeVisitId;
                        storeVisit.save();

                        ShopProfileAtivity.isCheckout = true;

                        Toast.makeText(StoreDetailActivity.this, "Check out successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoreDetailActivity.this, "Check Out failed", Toast.LENGTH_SHORT).show();
                finish();
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(StoreDetailActivity.this, "Please check out", Toast.LENGTH_SHORT).show();
    }
}


