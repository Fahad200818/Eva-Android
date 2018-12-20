package com.tracking.store.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.activeandroid.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import im.delight.android.location.SimpleLocation;

/**
 * Created by irfan on 3/19/18.
 */

public class Util {

    public static Util util = new Util();
    private final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd-MM-yyyy");

    public static Util getInstance() {
        return util;
    }

    private static char[] smallCaps = new char[]
            {
                    '\uf761', //A
                    '\uf762',
                    '\uf763',
                    '\uf764',
                    '\uf765',
                    '\uf766',
                    '\uf767',
                    '\uf768',
                    '\uf769',
                    '\uf76A',
                    '\uf76B',
                    '\uf76C',
                    '\uf76D',
                    '\uf76E',
                    '\uf76F',
                    '\uf770',
                    '\uf771',
                    '\uf772',
                    '\uf773',
                    '\uf774',
                    '\uf775',
                    '\uf776',
                    '\uf777',
                    '\uf778',
                    '\uf779',
                    '\uf77A'   //Z
            };

    public int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public File createImageFile(String fileName) {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "eTracker");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(fileName, ".jpg");
            if(image != null){
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public String getDateByFrequency(int frequency, int type){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(type, frequency);
        date = c.getTime();
        return dateFormatGmt.format(date);
    }

    public int getFrequencyType(String type){
        int frequencyType = 0;
        if(type.equals("Days")){
            frequencyType = Calendar.DAY_OF_WEEK;
        }else if(type.equals("Weeks")){
            frequencyType = Calendar.WEEK_OF_MONTH;
        }else if(type.equals("Months")){
            frequencyType = Calendar.MONTH;
        }
        return frequencyType;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        return sdf.format(new Date());
    }

    public String parseStringDate(String date){
        String output1 = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date1 = df.parse(date);
            DateFormat outputFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            output1 = outputFormatter1.format(date1); //
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output1;
    }

    public String getDateToString(String date){
        String output = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        try {
            Date date1 = df.parse(date);
            DateFormat outputFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
            output = outputFormatter1.format(date1); //
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }

    public Date getDate(String today){
        Date date = null;
        DateFormat outputFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date =  outputFormatter1.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date getCurrentDateIntoUTC() {
        Date date = null;
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        try {
            return dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new Date());
    }

    public String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        return sdf.format(new Date());
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public boolean isGPSEnable(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public int getUniqueID() {
        Random r = new Random();
        return r.nextInt(50000 - 500 + 1) + 500;
    }

    public String getRandomID() {
        final int sizeOfRandomString = 10;
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public String getRandomString() {
        final int sizeOfRandomString = 20;
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public void hideKeybaord(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String getSmallCapsString(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = smallCaps[chars[i] - 'a'];
            }
        }
        return String.valueOf(chars);
    }

    public void saveLocationInJSONFile(String mJsonResponse) {
        try {
            String lastData = mJsonResponse + "," + getData();
            File myDirectory = new File(Environment.getExternalStorageDirectory(), "eTrackert");
            if (!myDirectory.exists()) {
                myDirectory.mkdirs();
            }

            FileWriter file = new FileWriter(myDirectory.getPath() + "/" + "valus.json");
            file.append(lastData);
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    public String getData() {
        try {
            File myDirectory = new File(Environment.getExternalStorageDirectory(), "eTrackert");
            if (!myDirectory.exists()) {
                myDirectory.mkdirs();
            }

            File f = new File(myDirectory.getPath() + "/" + "valus.json");
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }

    public void clearDataFromVJSON() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "eTrackert");
        if (myDirectory.exists()) {
            try {
                FileWriter file = new FileWriter(myDirectory.getPath() + "/" + "valus.json");
                file.write("");
                file.flush();
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String isMockLocationEnabled(Context context) {
       String appName = "";
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;
                String packgeName = packageInfo.packageName;
                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        String packageImage = requestedPermissions[i];
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName()) && !packgeName.equals("com.verizon.messaging.vzmsgs") && !packgeName.equals("com.lge.systemservice") && !packgeName.equals("com.lge.gnsspostest") && !packgeName.equals("com.android.settings")) {

                            appName = applicationInfo.packageName;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }

        return appName;
    }

}
