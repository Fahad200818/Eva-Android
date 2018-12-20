package com.tracking.store.util;

import com.tracking.store.bean.Stocks;
import com.tracking.store.db.CompetitiveItem;
import com.tracking.store.db.InventoryItem;
import com.tracking.store.db.OrderItem;
import com.tracking.store.db.Store;
import com.tracking.store.db.StoreVisit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ZASS on 4/27/2018.
 */

public class DBToObject {

    public static DBToObject dbToObject = new DBToObject();

    public static DBToObject getInstance(){
        return dbToObject;
    }

    public JSONObject storeToJSONObject(Store store, int userID){

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId", userID);
            jsonObject.put("shopName", store.ShopName);
            jsonObject.put("shopKeeper", store.ShopKeeper);
            jsonObject.put("contactNo", store.ContactNumber);
            jsonObject.put("street", store.Street);
            jsonObject.put("city", store.City);
            jsonObject.put("landline", store.LandLine);
            jsonObject.put("address", store.Address);
            jsonObject.put("cnic", store.CNIC);
            jsonObject.put("landMark", store.Landmark);
            jsonObject.put("dayRegistered", store.DayRegistered);
            jsonObject.put("latitude", store.Latitude);
            jsonObject.put("longitude", store.Longitude);
            jsonObject.put("StartTime", store.StartTime);
            jsonObject.put("EndTime", "" + store.EndTime);
            jsonObject.put("subsectionId", store.SubSectionID);
            jsonObject.put("Status", store.Status);
            jsonObject.put("Category", store.Category);
            jsonObject.put("Classification", store.Classification);
            jsonObject.put("VisitFrequency", store.Frequency);
            jsonObject.put("frequencyType", store.FrequencyType);
            jsonObject.put("NextScheduledVisit", store.NextScheduledVisit);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject checkInToJSONObject(StoreVisit storeVisit) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", "" + storeVisit.Latitude);
            jsonObject.put("longitude", "" + storeVisit.Longitude);
            jsonObject.put("ContactPersonName", storeVisit.ContactName);
            jsonObject.put("ContactNo", storeVisit.ContactNumber);
            jsonObject.put("StartTime", storeVisit.StartTime);
            jsonObject.put("StoreId", storeVisit.StoreID);
            jsonObject.put("Status", "Pending");
            jsonObject.put("NextScheduledVisit", storeVisit.NextScheduledVisit);
        } catch (Exception e) {
            System.out.println("checkInToJSONObject Exception");
        }
        return jsonObject;
    }

    public JSONObject OrderTakingToJSONObject(List<OrderItem> orderItems) {

        JSONObject jsonObjectBody = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        try {
            for (OrderItem orderItem : orderItems) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("item", orderItem.Product);
                jsonObject.put("quantity", "" + orderItem.Qty);
                jsonObject.put("storeVisitId", orderItem.StoreVisitID);
                jsonArray.put(jsonObject);
            }
            jsonObjectBody.put("Order", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectBody;
    }

    public JSONObject checkOutToJSONObject(StoreVisit storeVisit) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", "" + storeVisit.Latitude);
            jsonObject.put("longitude", "" + storeVisit.Longitude);
            jsonObject.put("ContactPersonName", storeVisit.ContactName);
            jsonObject.put("ContactNo", storeVisit.ContactNumber);
            jsonObject.put("StartTime", storeVisit.StartTime);
            jsonObject.put("StoreId", storeVisit.StoreID);
            jsonObject.put("NextScheduledVisit", storeVisit.NextScheduledVisit);
            jsonObject.put("Location", "" + storeVisit.Location);
            jsonObject.put("Notes", "" + storeVisit.Notes);
            jsonObject.put("EndTime", storeVisit.EndTime);
            jsonObject.put("StoreVisitId", storeVisit.StoreVisitID);
            jsonObject.put("Status", storeVisit.Status);
        } catch (Exception e) {
            System.out.println("checkInToJSONObject Exception");
        }
        return jsonObject;
    }

    public JSONObject checkInOutToJSONObject(StoreVisit storeVisit) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", "" + storeVisit.Latitude);
            jsonObject.put("longitude", "" + storeVisit.Longitude);
            jsonObject.put("ContactPersonName", storeVisit.ContactName);
            jsonObject.put("ContactNo", storeVisit.ContactNumber);
            jsonObject.put("StartTime", storeVisit.StartTime);
            jsonObject.put("StoreId", storeVisit.StoreID);
            jsonObject.put("Status", "Achieved");
            jsonObject.put("NextScheduledVisit", storeVisit.NextScheduledVisit);
            jsonObject.put("Location", "" + storeVisit.Location);
            jsonObject.put("Notes", "" + storeVisit.Notes);
            jsonObject.put("EndTime", storeVisit.EndTime);
            jsonObject.put("Status", storeVisit.Status);
        } catch (Exception e) {
            System.out.println("checkInToJSONObject Exception");
        }
        return jsonObject;
    }

    public JSONObject orderTakingToJSONObject(List<OrderItem> orderList) {
        JSONObject jsonObjectBody = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        try {
            for (OrderItem order : orderList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("item", order.Product);
                jsonObject.put("quantity", "" + order.Qty);
                jsonObject.put("storeVisitId", order.StoreVisitID);
                jsonArray.put(jsonObject);
            }
            jsonObjectBody.put("Order", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectBody;
    }

    public JSONObject compitativeStockToJSONObject(List<CompetitiveItem> orderList) {
        JSONObject jsonObjectBody = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        try {
            for (CompetitiveItem compitative : orderList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("item", compitative.Product);
                jsonObject.put("quantity", "" + compitative.Qty);
                jsonObject.put("storeVisitId", compitative.StoreVisitID);
                jsonArray.put(jsonObject);
            }
            jsonObjectBody.put("CompetativeStock", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectBody;
    }

    public JSONObject InventoryTakingToJSONObject(List<InventoryItem> orderList) {
        JSONObject jsonObjectBody = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        try {
            for (InventoryItem order : orderList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("item", order.Product);
                jsonObject.put("quantity", "" + order.Qty);
                jsonObject.put("storeVisitId", order.StoreVisitID);
                jsonObject.put("type", "default");
                jsonArray.put(jsonObject);
            }
            jsonObjectBody.put("Stock", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectBody;
    }
}
