package com.tracking.store.web;

/**
 * Created by ZASS on 3/22/2018.
 */

public class WebURL {



    //public static String host = "http://evasalesforcestage.azurewebsites.net/service/"; // dev
    public static String host = "http://evapakistan.com/service/"; // dev
    public static String url = host+"api/"; // dev
    public static String login = url+"Auth/login";
    public static String storeRegistration = url+"store/createstore";

    public static String fileUpload = url+"store/uploadfiles";

    public static String getUserData = url+"user/GetUser/";
    public static String addStoreVisit = url+"StoreVisit/AddStoreVisit";
    public static String addOrderTaken = url+"storevisit/addorder";
    public static String addOutletStock = url+"storevisit/AddOutletStock";
    public static String addmultipleorders = url+"storevisit/addmultipleorders";
    public static String addMultipleStock = url+"storevisit/AddMultipleStock";
    public static String addMultipleCompetativeStock = url+"storevisit/AddMultipleCompetativeStock";
    public static String addCompetativeStock = url+"api/storevisit/UploadStockImage/";

    public static String getalluserstores = url+"store/getalluserstores/";
    public static String getStoreVisits = url+"StoreVisit/GetVisits";
    public static String POST_LOCATION = url+"GeoLocation/PostLocationCoordinated/";

    public static String GET_TERRITORIES = url+"territory/getterritories";
    public static String GET_TOWN_BY_TERRITORY = url+"territory/gettownsbyterritory";
    public static String GET_AREAS_BY_TOWN = url+"territory/getareasbytown";
    public static String GET_SUBSECTION = url+"territory/GetSubsectionsByUser";

    public static String updateStoreVisit = url+"storevisit/updatestorevisit";

   // public static String host = "http://evasalesforcestage.azurewebsites.net/service/";
}