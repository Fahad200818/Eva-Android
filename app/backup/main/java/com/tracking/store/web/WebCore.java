package com.tracking.store.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tracking.store.LoginActivity;
import com.tracking.store.MainActivity;
import com.tracking.store.db.Area;
import com.tracking.store.db.CompetitiveItem;
import com.tracking.store.db.InventoryItem;
import com.tracking.store.db.OrderItem;
import com.tracking.store.db.Store;
import com.tracking.store.db.StoreVisit;
import com.tracking.store.db.SubSection;
import com.tracking.store.db.Town;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.util.Constants;
import com.tracking.store.util.DBToObject;
import com.tracking.store.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ZASS on 4/28/2018.
 */

public class WebCore {
    private static final WebCore ourInstance = new WebCore();

    private DBHandler dbHandler = DBHandler.getInstance();
    private DBToObject dbToObject = DBToObject.getInstance();
    private HttpCaller httpCaller = HttpCaller.getInstance();
    private PrefManager prefManager = PrefManager.getPrefInstance();

    public static WebCore getInstance() {
        return ourInstance;
    }

    int countStore = 0;
    int countStoreVisits = 0;

    public void pushToWeb(final Context context, final WebEqecuteCallBack webEqecuteCallBack) {
        int userID = prefManager.getUserID();
        final List<Store> storeList = dbHandler.getUnSyncedStores();
        if(storeList.size() > 0){
            for (int i=0; i < storeList.size(); i++) {
                final Store store = storeList.get(i);
                JSONObject jsonObject = null;
                try {
                    jsonObject = dbToObject.storeToJSONObject(store, userID);
                    if (jsonObject != null) {
                        String URL = WebURL.storeRegistration;
                        httpCaller.jsonObjectPOSTRequest(false, context, URL, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int storeID = response.getInt("id");
                                    store.StoreID = storeID;
                                    store.IsSync = true;
                                    store.save();

                                    syncImageFile("" + storeID, store, webEqecuteCallBack);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                webEqecuteCallBack.finishResponse();
                                Toast.makeText(context, "StoreStore registered failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countStore++;
            }
        }else{
            webEqecuteCallBack.updateToDateResponse();
        }
    }

    public void pushStoreCheckOutToServer(final Context context, final WebEqecuteCallBack webEqecuteCallBack){
        final List<StoreVisit> storeVisitList = dbHandler.getUnSyncedStoreVisits();
        if(storeVisitList.size() > 0){
            for (int i=0; i < storeVisitList.size(); i++) {
                final StoreVisit storeVisits = storeVisitList.get(i);
                JSONObject jsonObject = null;
                try {
                    jsonObject = dbToObject.checkInOutToJSONObject(storeVisits);
                    if (jsonObject != null) {
                        String URL = WebURL.updateStoreVisit;
                        httpCaller.jsonObjectPOSTRequest(false, context, URL, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    long tempStoreID = storeVisits.StoreVisitID;

                                    int storeVisitId = response.getInt("storeVisitId");
                                    storeVisits.StoreVisitID = storeVisitId;
                                    storeVisits.IsSync = true;
                                    storeVisits.save();

                                    dbHandler.updateVisits(tempStoreID, storeVisitId);

                                    final List<OrderItem> orderItems = dbHandler.getUnSyncedOrderByStoreVisits(storeVisitId);
                                    if(orderItems.size() > 0 ){
                                        JSONObject jsonObject = dbToObject.OrderTakingToJSONObject(orderItems);
                                        if(jsonObject != null){
                                            String url = WebURL.addmultipleorders;
                                            httpCaller.jsonObjectPOSTRequest(false, context, url, jsonObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    if (response != null) {

                                                        for(OrderItem orderItem: orderItems){
                                                            orderItem.IsSync = true;
                                                            orderItem.save();
                                                        }
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    webEqecuteCallBack.finishResponse();
                                                    error.printStackTrace();
                                                }
                                            });
                                        }
                                    }

                                    final List<InventoryItem> inventoryItems = dbHandler.getUnSyncedInventoryByStoreVisits(storeVisitId);
                                    if(inventoryItems.size() > 0 ){
                                        JSONObject jsonObject = dbToObject.InventoryTakingToJSONObject(inventoryItems);
                                        if(jsonObject != null){
                                            String url = WebURL.addMultipleStock;
                                            httpCaller.jsonObjectPOSTRequest(false, context, url, jsonObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    if (response != null) {

                                                        for(InventoryItem inventoryItem: inventoryItems){
                                                            inventoryItem.IsSync = true;
                                                            inventoryItem.save();
                                                        }
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    webEqecuteCallBack.finishResponse();
                                                    error.printStackTrace();
                                                }
                                            });
                                        }
                                    }

                                    final List<CompetitiveItem> compitativeItemsList = dbHandler.getUnSyncedCompitativeStockByStoreVisits(storeVisitId);
                                    if(compitativeItemsList.size() > 0 ){
                                        JSONObject jsonObject = dbToObject.compitativeStockToJSONObject(compitativeItemsList);
                                        if(jsonObject != null){
                                            String url = WebURL.addMultipleCompetativeStock;
                                            httpCaller.jsonObjectPOSTRequest(false, context, url, jsonObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    if (response != null) {
                                                        for(CompetitiveItem competitiveItem: compitativeItemsList){
                                                            competitiveItem.IsSync = true;
                                                            competitiveItem.save();
                                                        }
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    webEqecuteCallBack.finishResponse();
                                                    error.printStackTrace();
                                                }
                                            });
                                        }
                                    }


                                    webEqecuteCallBack.finishResponse();
                                    Toast.makeText(context, "Offline Data sync successfully", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                webEqecuteCallBack.finishResponse();
                                Toast.makeText(context, "StoreStore registered failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countStore++;
            }
        }else{
            webEqecuteCallBack.updateToDateResponse();
        }

    }
    public void pullFromServer(final Context context, final int userID, final ProgressDialog progressDialog){
        progressDialog.show();
        String url = WebURL.getUserData + "" + userID;
        httpCaller.request1GETToServer(true, context, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

               if(jsonObject != null){
                   try{
                       JSONObject distributerObject = jsonObject.getJSONObject("distributer");
                       if(distributerObject != null){
                           int territoryId = distributerObject.getInt("territoryId");
                           if(territoryId != 0){
                               prefManager.put(Constants.IS_SALE_USER, true);

                               getSubSectionByUserID(context, userID, new WebEqecuteCallBack() {
                                   @Override
                                   public void finishResponse() {
                                       progressDialog.dismiss();
                                   }

                                   @Override
                                   public void updateToDateResponse() {
                                       Toast.makeText(context, "Data is Up-To-Date", Toast.LENGTH_SHORT).show();
                                       progressDialog.dismiss();
                                   }
                               });
                           }
                       }else{
                           Toast.makeText(context, "Distributer not assigned", Toast.LENGTH_SHORT).show();
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                       progressDialog.dismiss();
                   }

               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void syncImageFile(final String storeID, final Store store, final WebEqecuteCallBack webEqecuteCallBack) {
        String url = WebURL.fileUpload + "/" + storeID;
        httpCaller.uploadImage(url, store.ImagePath, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        String imgUrl = response.getString("filepath");
                        store.ImgUrl = imgUrl;
                        store.save();

                        webEqecuteCallBack.finishResponse();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void getSubSectionByUserID(final Context context, int userID, final WebEqecuteCallBack webEqecuteCallBack) {
        String url = WebURL.GET_SUBSECTION + "/" + userID;
        httpCaller.requestArrayGETToServer(false, context, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                if(jsonArray.length() > 0 ){
                    try {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            int subsectionId = jsonObject.getInt("subsectionId");
                            String name = jsonObject.getString("name");

                            SubSection subSection = dbHandler.getSubSection(subsectionId);
                            if(subSection ==  null){
                                subSection = new SubSection();
                                subSection.subsectionId = subsectionId;
                                subSection.name = name;
                                subSection.save();
                            }
                        }
                        webEqecuteCallBack.finishResponse();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    webEqecuteCallBack.updateToDateResponse();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                webEqecuteCallBack.updateToDateResponse();
            }
        });
    }
    public interface WebEqecuteCallBack {
        public void finishResponse();
        public void updateToDateResponse();
    }
}
