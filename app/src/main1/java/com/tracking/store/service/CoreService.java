package com.tracking.store.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tracking.store.MainActivity;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.realtimedb.RealTimeDBHandling;
import com.tracking.store.util.DBToObject;
import com.tracking.store.util.GPSValue;
import com.tracking.store.util.PrefManager;
import com.tracking.store.util.Util;
import com.tracking.store.web.HttpCaller;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.location.SimpleLocation;

public class CoreService extends Service {

    private static CoreService coreService = new CoreService();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PrefManager prefManager = PrefManager.getPrefInstance();
    private RealTimeDBHandling realTimeDBHandling = RealTimeDBHandling.getInstance();
    private GPSValue gpsValue = GPSValue.getInstance();
    private Util utilInstance = Util.getInstance();
    private DBHandler dbHandler = DBHandler.getInstance();
    private DBToObject dbToObject = DBToObject.getInstance();
    private HttpCaller httpCaller = HttpCaller.getInstance();

    public static CoreService coreInstance;
    private Handler syncLocationHandler;
    private Runnable myRunnable;
    private static SimpleLocation locationInstance;

    private static double startLatitude;
    private static double startLongitude;


    public static CoreService getCoreService() {
        return coreService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateLocation();
        coreInstance = this;
        return super.onStartCommand(intent, flags, startId);
    }


    public void updateLocation() {
        long updateIntervalInMilliseconds = 1 * 5 * 1000;

        locationInstance = new SimpleLocation(this, false, false, updateIntervalInMilliseconds, true);
        if (locationInstance == null)
            return;

        locationInstance.setListener(new SimpleLocation.Listener() {
            @Override
            public void onPositionChanged() {
                double longitude = locationInstance.getLongitude();
                double latitude = locationInstance.getLatitude();

                if (!utilInstance.isNetworkConnected(CoreService.this))
                    stopService(CoreService.this);

                if (latitude != 0.0 && longitude != 0.0) {

                    //gpsValue.setLocation(locationInstance);

                    //initRealTimeFirebase();

                    startLatitude = locationInstance.getLatitude();
                    startLongitude = locationInstance.getLongitude();
                }

            }
        });
        locationInstance.beginUpdates();
    }

    public void initRealTimeFirebase() {
        Map<String, Object> userObject = getHashMapValues(locationInstance);
        double distance = locationInstance.calculateDistance(startLatitude, startLongitude, locationInstance.getLatitude(), locationInstance.getLongitude());
        if (distance >= 1 || true) {
            realTimeDatabase(userObject); // for realTime Tracking
            historyLocationSync(userObject); // for tracking history
        }
    }

    public void realTimeDatabase(Map<String, Object> user) {
        if (user != null) {
            int userID = prefManager.getUserID();
            realTimeDBHandling.realTimeCurrentLocation(db, "" + userID, user, CoreService.this);
        }
    }

    public void historyLocationSync(final Map<String, Object> user) {
        if (user != null) {
            syncLocationHandler = new Handler();
            final int intervalTime = 1 * 10 * 1000; //3 minutes
            myRunnable = new Runnable() {
                public void run() {
                    int userID = prefManager.getUserID();
                    if (utilInstance.isNetworkConnected(CoreService.this))
                        try {
                           realTimeDBHandling.RealTimeUserLocationHistory(db, "" + userID, user, CoreService.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            };

            syncLocationHandler.postDelayed(myRunnable, intervalTime);
        }
    }

    public void stopService(MainActivity context) {
        context.stopService(new Intent(context, CoreService.class));
        if (syncLocationHandler != null) {
            syncLocationHandler.removeCallbacks(myRunnable);
        }
        if(locationInstance != null)
            locationInstance.endUpdates();
    }

    public void stopService(CoreService context) {
        if (context != null) {
            stopService(new Intent(context, CoreService.class));
            if (syncLocationHandler != null) {
                syncLocationHandler.removeCallbacks(myRunnable);
            }
            if(locationInstance != null)
                locationInstance.endUpdates();
        }
    }

    public void stopService() {
        if (locationInstance != null) {
            locationInstance.endUpdates();
        }

        if (syncLocationHandler != null) {
            syncLocationHandler.removeCallbacks(myRunnable);
        }

    }

    public Map<String, Object> getHashMapValues(SimpleLocation simpleLocation) {
        int userID = prefManager.getUserID();
        Map<String, Object> user = new HashMap<>();
        user.put("lat", "" + simpleLocation.getLatitude());
        user.put("lng", "" + simpleLocation.getLongitude());
        user.put("timestamp", utilInstance.getCurrentDateIntoUTC());
        user.put("userid", userID);
        return user;
    }






}

/*
    public void scheduleLocationSync() {
        syncLocationHandler = new Handler();
        final int intervalTime = 1 * 40 * 1000; //3 minutes

        syncLocationHandler.postDelayed(new Runnable() {
            public void run() {

                String data = utilInstance.getData();
                String jsonBody = "";
                String startArrayBody = "{" + "\"LocationJson\":[";
                jsonBody = startArrayBody;
                jsonBody += data;
                jsonBody += "]" + "}";

                if(utilInstance.isNetworkConnected(CoreService.this))
                    try {
                        JSONObject jk = new JSONObject(jsonBody);
                        JSONArray jsonArray = jk.getJSONArray("LocationJson");
                        if (jsonArray.length() > 0) {
                            String url = WebURL.POST_LOCATION + prefManager.getUserID();
                            httpCaller.jsonObjectPOSTRequest(false, CoreService.this, url, jk, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    utilInstance.clearDataFromVJSON();
                                    //  Toast.makeText(CoreService.this, "Synced Location", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                syncLocationHandler.postDelayed(this, intervalTime);
            }
        }, intervalTime);
    }*/
