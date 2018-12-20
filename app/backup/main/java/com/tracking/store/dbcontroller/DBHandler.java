package com.tracking.store.dbcontroller;

import com.activeandroid.query.Select;
import com.google.android.gms.maps.model.LatLng;
import com.tracking.store.bean.ShopMark;
import com.tracking.store.db.Area;
import com.tracking.store.db.CompetitiveItem;
import com.tracking.store.db.InventoryItem;
import com.tracking.store.db.Names;
import com.tracking.store.db.OrderItem;
import com.tracking.store.db.Store;
import com.tracking.store.db.StoreVisit;
import com.tracking.store.db.SubSection;
import com.tracking.store.db.Town;
import com.tracking.store.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZASS on 3/20/2018.
 */

public class DBHandler  {

    public static DBHandler dbHandler = new DBHandler();
    public Util utilInstance = Util.getInstance();

    public static DBHandler getInstance(){
        return dbHandler;
    }

    public List<Store> getStoreList(String status, String currentDate){
        List<Store> storeList = null;
        if(status.equals("Target")) {
            List<Store> storeList1 = new Select().from(Store.class).execute();
            storeList = new Select().from(Store.class).where("NextScheduledForApp = ?", currentDate).and("Status =?", status).execute();
            return storeList;
        }else if(status.equals("Achieved")){
            List<Store> storeList1 = new Select().from(Store.class).execute();
            storeList = new Select().from(Store.class).where("Status = 'Achieved'").execute();
        }else{
            storeList = new Select().from(Store.class).execute();
        }
        return storeList;
    }

    public List<String> getContactList(long storeID){
        List<String> namesList1 = new ArrayList<>();
        List<Names> namesList = new Select().from(Names.class).where("storeID = ?", storeID).execute();
        for(Names name : namesList){
            namesList1.add(name.name_);
        }
        return namesList1;
    }

    public String getContactNumber(String name){
        Names names = new Select().from(Names.class).where("name_ =  '"+name+"'" ).executeSingle();
        if(names != null){
            return names.number_;
        }else
            return "";
    }

    public List<Store> getUnSyncedStores(){
        List<Store> storeList = new Select().from(Store.class).where("IsSync = ?", false).execute();
        return storeList;
    }

    public List<StoreVisit> getUnSyncedStoreVisits(){
        List<StoreVisit> storeList = new Select().from(StoreVisit.class).where("IsSync = ?", false).execute();
        return storeList;

    }

    public List<OrderItem> getUnSyncedOrderByStoreVisits(int storeVisitID){
        List<OrderItem> orderList1 = new Select().from(OrderItem.class).execute();
        List<OrderItem> orderList = new Select().from(OrderItem.class).where("IsSync = ?", false).and("StoreVisitID = ?", storeVisitID).execute();
        return orderList;
    }

    public List<InventoryItem> getUnSyncedInventoryByStoreVisits(int storeVisitID){
        List<InventoryItem> orderList = new Select().from(InventoryItem.class).where("IsSync = ?", false).and("StoreVisitID = ?", storeVisitID).execute();
        return orderList;
    }

    public List<CompetitiveItem> getUnSyncedCompitativeStockByStoreVisits(int storeVisitID){
        List<CompetitiveItem> orderList = new Select().from(CompetitiveItem.class).where("IsSync = ?", false).and("StoreVisitID = ?", storeVisitID).execute();
        return orderList;
    }

    public List<OrderItem> getUnSyncedOrder(){
        List<OrderItem> orderList = new Select().from(OrderItem.class).where("IsSync = ?", false).execute();
        return orderList;
    }

    public Store getStoreDetail(int storeID){
        return new Select().from(Store.class).where("StoreID =?", storeID).executeSingle();
    }

    public List<OrderItem> getOrderList(int storeID){
        return new Select().from(OrderItem.class).where("StoreID = ?", storeID).execute();
    }

    public OrderItem getOrderItem(String orderName , int storeVisitID, int storeID){
        OrderItem orderItem =  new Select().from(OrderItem.class).where("Product = '"+orderName+"'").and("StoreVisitID = ? ", storeVisitID).executeSingle();
        if(orderItem != null)
            return orderItem;
        else
            return null;
    }

    public ArrayList<ShopMark> getShopsLocation(){
        ArrayList<ShopMark> locationList = new ArrayList<>();
        String currentDate = utilInstance.getCurrentDate();
        //List<Store> storeList = new Select().from(Store.class).where("DayRegistered =?", day).and("Status = 'Target' ").execute();
        List<Store> storeList = new Select().from(Store.class).where("NextScheduledForApp = ?", currentDate).and("Status = 'Target'" ).execute();

        for(Store store : storeList){
            try{
                ShopMark shopMark= new ShopMark();

                double lati = Double.parseDouble(store.Latitude);
                double longi = Double.parseDouble(store.Longitude);
                LatLng points =  new LatLng(lati , longi);

                shopMark.latLng = points;
                shopMark.store = store;
                locationList.add(shopMark);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return locationList;
    }

    public List<InventoryItem> getInventoryList(int storeID){
        return new Select().from(InventoryItem.class).where("StoreID = ?", storeID).execute();
    }

    public StoreVisit getLastStoreVisit(){
        return  new Select().from(StoreVisit.class).orderBy("id DESC").executeSingle();
    }

    public boolean isStoreVisitEmpity(int storeID){
        List<StoreVisit>  storeVisits =  new Select().from(StoreVisit.class).where("StoreID =?", storeID).execute();
        if(storeVisits.size() == 0)
            return false;
        else
            return true;
    }

    public List<SubSection> getSubsection(){
        List<SubSection> townList =  new Select().from(SubSection.class).execute();
        return townList;
    }

    public void updateVisits(long tempStoreID, long storeVisitID){
        List<OrderItem> orderList = new Select().from(OrderItem.class).where("StoreVisitID = ?", tempStoreID).execute();
        for(OrderItem orderItem : orderList){
                orderItem.StoreVisitID = storeVisitID;
                orderItem.save();
        }
        List<InventoryItem> InventoryList = new Select().from(InventoryItem.class).where("StoreVisitID = ?", tempStoreID).execute();
        for(InventoryItem inventoryItem : InventoryList){
            inventoryItem.StoreVisitID = storeVisitID;
            inventoryItem.save();
        }
        List<CompetitiveItem> CompetitiveList = new Select().from(CompetitiveItem.class).where("StoreVisitID = ?", tempStoreID).execute();
        for(CompetitiveItem competitiveItem : CompetitiveList){
            competitiveItem.StoreVisitID = storeVisitID;
            competitiveItem.save();
        }
    }

    public Town getTown(String town){
        return  new Select().from(Town.class).where("name =? ", town).executeSingle();
    }

    public SubSection getSubSection(int subsectionId){
        return  new Select().from(SubSection.class).where("subsectionId =? ", subsectionId).executeSingle();
    }

    public List<Area> getAreaByTown(int townID){
        List<Area> townList =  new Select().from(Area.class).where("townId =? ", townID).execute();
        return townList;
    }

    public Area getArea(int areaId){
        return  new Select().from(Area.class).where("areaId = ?", areaId).executeSingle();
    }

    public List<Area> getAreas(){
        List<Area> areaList =  new Select().from(Area.class).execute();
        return areaList;
    }


    public void updateStatus(){
        List<Store> storeList1 = new Select().from(Store.class).execute();
        String todayDate = utilInstance.getCurrentDate();
        List<Store> achievedList = new Select().from(Store.class).where("Status = 'Achieved'").and("NextScheduledForApp != ? ", todayDate).execute();
        if(achievedList.size() > 0){
            for(Store store : achievedList){
                store.Status = "Target";
                store.save();
            }
        }
    }

    public long getUniqueID(String modelString){
        long id = 0;
        if(modelString.equals("Store")) {
            Store store = new Select().from(Store.class).orderBy("id DESC").executeSingle();
            if(store == null)
                return 1;
            else
             id = store.StoreID+1;
        }
        else if(modelString.equals("StoreVisit")) {
            StoreVisit storeVisit = new Select().from(StoreVisit.class).orderBy("id DESC").executeSingle();
            if(storeVisit == null)
                id =1;
            else
                id = storeVisit.StoreVisitID+1;
        }
        return id;
    }

}
