package com.tracking.store;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.FirebaseApp;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.fragment.EmptyMapFragment;
import com.tracking.store.fragment.EmptyStoreVisitsFragment;
import com.tracking.store.fragment.MapFragment;
import com.tracking.store.fragment.StoreVisitsFragment;
import com.tracking.store.service.CoreService;
import com.tracking.store.service.MyService;
import com.tracking.store.util.Constants;
import com.tracking.store.util.GPSValue;
import com.tracking.store.util.PrefManager;
import com.tracking.store.util.Util;
import com.tracking.store.view.DisableSwipViewPager;
import com.tracking.store.view.ViewGroups;

import im.delight.android.location.SimpleLocation;


public class MainActivity extends AppCompatActivity {

    private GPSValue gpsValue = GPSValue.getInstance();
    private Util utilInstance = Util.getInstance();
    private PrefManager prefManager = PrefManager.getPrefInstance();
    private DBHandler dbHandler = DBHandler.getInstance();
    private CoreService coreService = CoreService.getCoreService();
    private ViewGroups viewGroups = ViewGroups.getInstance();

    public static Location locationInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseApp.initializeApp(MainActivity.this);
        locationInstance = gpsValue.getLocation();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navOptionClickListerner(drawer);
        initJourneyCycle();

        getSupportActionBar().setTitle(null);

        dbHandler.updateStatus();

        String isFockLocationEnabled = ""; //utilInstance.isMockLocationEnabled(MainActivity.this);
        try{

            if(!isFockLocationEnabled.equals("")){
                viewGroups.showSnackMessage(drawer, "You are using fake GPS location app : "+isFockLocationEnabled+" , please disable that app", 20000);
            }else if (GPSStoragePermissionGranted()) {
                if (utilInstance.isGPSEnable(MainActivity.this)) {
                    if (!utilInstance.isMyServiceRunning(CoreService.class, MainActivity.this))
                        startService(new Intent(MainActivity.this, MyService.class));
                } else {
                    gpsLocationDialog();
                }

            } else {
                Toast.makeText(MainActivity.this, "GPS permission granted", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void navOptionClickListerner(DrawerLayout drawerLayout) {
        RelativeLayout layoutStoreRegistration = (RelativeLayout) drawerLayout.findViewById(R.id.layout_storeregistration);
        RelativeLayout layoutReport = (RelativeLayout) drawerLayout.findViewById(R.id.layout_report);
        RelativeLayout layout_logout = (RelativeLayout) drawerLayout.findViewById(R.id.layout_logout);

        TextView textViewSalesEmail = (TextView) drawerLayout.findViewById(R.id.textViewSalesEmail);
        TextView textViewSalesname = (TextView) drawerLayout.findViewById(R.id.textViewSalesname);

        String name = "" + prefManager.get(Constants.FullName, new String());
        String email = "" + prefManager.get(Constants.Email, new String());

        if(email.equals("null")) {
            textViewSalesEmail.setText("");
        }else{
            textViewSalesEmail.setText(email);
        }

        if(name.equals("null")) {
            textViewSalesname.setText("");
        }else{
            textViewSalesname.setText(name );
        }


        layoutStoreRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StoreRegistrationActivity.class));
            }
        });

        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

    }

    public void initJourneyCycle() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Map", MapFragment.class)
                .add("Store visits", StoreVisitsFragment.class)
                .create());

        DisableSwipViewPager viewPager = (DisableSwipViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);

        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //GPS On response
        if (requestCode == 100) {
            if (!utilInstance.isMyServiceRunning(CoreService.class, MainActivity.this))
                startService(new Intent(MainActivity.this, CoreService.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            if (!utilInstance.isMyServiceRunning(CoreService.class, MainActivity.this))
                startService(new Intent(MainActivity.this, CoreService.class));
        }
    }

    public void logoutDialog() {
        new MaterialDialog.Builder(MainActivity.this).content("Do you want to logout?").contentColor(getResources().getColor(R.color.black)).backgroundColor(getResources().getColor(R.color.white))
                .positiveText("Yes")
                .negativeText("No").negativeColor(Color.parseColor("#ff0000")).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                prefManager.logout();
                logOut();

                if (utilInstance.isMyServiceRunning(MyService.class, MainActivity.this)) {
                    Intent service_intent = new Intent(MainActivity.this, MyService.class);
                    stopService(service_intent);
                }
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }).show();

    }

    public boolean GPSStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public void logOut() {
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "Store"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "OrderItem"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "InventoryItem"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "StoreVisit"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "SubSection"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "CompetitiveItem"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "Names"));
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "Logs"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        }
    }

    public void gpsLocationDialog() {
        new MaterialDialog.Builder(MainActivity.this).content("Turn on locationInstance services to determine your locationInstance").contentColor(getResources().getColor(R.color.black)).backgroundColor(getResources().getColor(R.color.white))
                .positiveText("Setting")
                .negativeText("Cancel").negativeColor(Color.parseColor("#ff0000")).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
            }
        }).show();
    }
}

