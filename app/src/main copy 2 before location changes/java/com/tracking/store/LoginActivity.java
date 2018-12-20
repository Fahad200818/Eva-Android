package com.tracking.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tracking.store.db.Area;
import com.tracking.store.db.Store;
import com.tracking.store.db.Town;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.util.Constants;
import com.tracking.store.util.PrefManager;
import com.tracking.store.util.Util;
import com.tracking.store.view.ViewGroups;
import com.tracking.store.web.HttpCaller;
import com.tracking.store.web.WebCore;
import com.tracking.store.web.WebURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {


    public HttpCaller httpCaller = HttpCaller.getInstance();
    public Util utilInstance = Util.getInstance();
    public WebCore webCore = WebCore.getInstance();
    public DBHandler dbHandler = DBHandler.getInstance();
    private ViewGroups viewGroup = ViewGroups.getInstance();

    private Button btnLogin;
    private EditText editTextEmail;
    private EditText editTextPass;
    private CheckBox checkBoxRememberdPass;


    public PrefManager prefManager = PrefManager.getPrefInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean isLogin = prefManager.isLogin();

        if (isLogin) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }

        editTextEmail = (EditText) findViewById(R.id.edittext_userid);
        editTextPass = (EditText) findViewById(R.id.edittext_pass);

        checkBoxRememberdPass = (CheckBox) findViewById(R.id.checkBoxRemembPass);

        btnLogin = (Button) findViewById(R.id.btn_login);

        if (!prefManager.getLoginData()[0].equals("") && !prefManager.getLoginData()[1].equals("")) {
            String loginData[] = prefManager.getLoginData();
            editTextEmail.setText("" + loginData[0]);
            editTextPass.setText("" + loginData[1]);
            checkBoxRememberdPass.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String userID = editTextEmail.getText().toString();
                final String userPass = editTextPass.getText().toString();

                if (TextUtils.isEmpty(userID)) {
                    Toast.makeText(LoginActivity.this, "Please enter username ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (!utilInstance.isNetworkConnected(LoginActivity.this)) {

                    String prefID = prefManager.getLoginData()[0];
                    String prefPass = prefManager.getLoginData()[1];

                    if(userID.equals(prefID) && userPass.equals(prefPass)){

                        prefManager.setLogin(true);

                        boolean isChecked = checkBoxRememberdPass.isChecked();
                        if (isChecked) {
                            prefManager.setRememberdPass(userID, userPass);
                        } else {
                            prefManager.removeRememberdData();
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Internet connection problem", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("username", userID);
                        jsonObject.put("password", userPass);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonObject != null)
                        httpCaller.requestToServerForLogin(LoginActivity.this, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {

                                        boolean isChecked = checkBoxRememberdPass.isChecked();
                                        if (isChecked) {
                                            prefManager.setRememberdPass(userID, userPass);
                                        } else {
                                            prefManager.removeRememberdData();
                                        }

                                        JSONObject responseJson = response.getJSONObject("user");
                                        if (responseJson != null) {

                                            final int userId = responseJson.getInt("userid");
                                            final String firstName = responseJson.getString("firstname");
                                            final String lastName = responseJson.getString("lastname");
                                            final int TerritoryId = responseJson.getInt("territoryid");
                                            final String email = responseJson.getString("email");

                                            String name = firstName + " " + lastName;

                                            prefManager.put("FullName", name);
                                            prefManager.put("Email", email);


                                            prefManager.setLogin(true);
                                            prefManager.setUserID(userId);

                                            String url = WebURL.getalluserstores + "" + userId;

                                            httpCaller.requestArrayGETToServer(false, LoginActivity.this, url, new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray jsonArrayResponse) {
                                                    try {

                                                        if(jsonArrayResponse.length() == 0){
                                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                        for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                                            JSONObject jsonRegisteredShop = jsonArrayResponse.getJSONObject(i);

                                                            int storeID = jsonRegisteredShop.getInt("storeId");
                                                            String shopName = jsonRegisteredShop.getString("shopName");
                                                            String shopKeeper = jsonRegisteredShop.getString("shopKeeper");
                                                            String contactNo = jsonRegisteredShop.getString("contactNo");
                                                            String street = jsonRegisteredShop.getString("street");
                                                            String city = jsonRegisteredShop.getString("city");
                                                            String landLine = jsonRegisteredShop.getString("landline");
                                                            String address = jsonRegisteredShop.getString("address");
                                                            String cnic = jsonRegisteredShop.getString("cnic");
                                                            String landmark = jsonRegisteredShop.getString("landMark");
                                                            int dayRegistered = jsonRegisteredShop.getInt("dayRegistered");
                                                            String imageUrl = jsonRegisteredShop.getString("imageUrl");
                                                            String latitude = jsonRegisteredShop.getString("latitude");
                                                            String longitude = jsonRegisteredShop.getString("longitude");
                                                            int visitFrequency = jsonRegisteredShop.getInt("visitFrequency");
                                                            String frequencyType = jsonRegisteredShop.getString("frequencyType");
                                                            int subsectionId = jsonRegisteredShop.getInt("subsectionId");

                                                            String nextScheduledVisit= "";
                                                            try{
                                                                nextScheduledVisit = jsonRegisteredShop.getString("nextScheduledVisit");
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }


                                                            //int frequencyType_ =  utilInstance.getFrequencyType(frequencyType);
                                                            //String nextScheduledVisitDate = utilInstance.getDateByFrequency(visitFrequency, frequencyType_);

                                                            //String nextSche = nextScheduledVisit.replace("T", " ");
                                                            String nextScheduledVisitDate = utilInstance.parseStringDate(nextScheduledVisit);
                                                            String nextScheduledForApp = utilInstance.getDateToString(nextScheduledVisitDate);

                                                            int day = utilInstance.getDayOfWeek();
                                                            Store store = new Store();
                                                            store.StoreID = storeID;
                                                            store.ShopName = shopName;
                                                            store.ShopKeeper = shopKeeper;
                                                            store.ContactNumber = contactNo;
                                                            store.LandLine = landLine;
                                                            store.Address = address;
                                                            store.Street = street;
                                                            store.City = city;
                                                            store.Landmark = landmark;
                                                            store.DayRegistered = dayRegistered;
                                                            store.CNIC = cnic;
                                                            store.IsSync = true;
                                                            store.ImgUrl = imageUrl;
                                                            store.Latitude = latitude;
                                                            store.Longitude = longitude;
                                                            store.NextScheduledVisit = nextScheduledVisitDate;
                                                            store.NextScheduledForApp = nextScheduledForApp;
                                                            store.Frequency = visitFrequency;
                                                            store.SubSectionID = subsectionId;
                                                            store.FrequencyType = frequencyType;
                                                            store.IsSync = true;

                                                            //Date nextScheduledDate = utilInstance.getDate(nextScheduledVisitDate);
                                                            String currentDate = utilInstance.getCurrentDate();
                                                            if (nextScheduledForApp.equals(currentDate))
                                                                store.Status = "Target";

                                                            if (day == dayRegistered)
                                                                store.DayRegistered = day;

                                                            store.save();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    if (TerritoryId != 0) {
                                                        prefManager.put(Constants.IS_SALE_USER, true);
                                                        webCore.getSubSectionByUserID(LoginActivity.this, userId, new WebCore.WebEqecuteCallBack() {
                                                            @Override
                                                            public void finishResponse() {
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                            @Override
                                                            public void updateToDateResponse() {

                                                            }
                                                        });
                                                    } else {
                                                        prefManager.put(Constants.IS_SALE_USER, false);
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Login failed, Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        String msg = response.getString("message");
                                        viewGroup.showSnackMessage(v, "" + msg, 5000);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });
    }

}
