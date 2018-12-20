package com.tracking.store.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.request.DirectionDestinationRequest;
import com.akexorcist.googledirection.request.DirectionOriginRequest;
import com.akexorcist.googledirection.request.DirectionRequest;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.liefery.android.icon_badge.IconBadge;
import com.tracking.store.R;
import com.tracking.store.bean.ShopMark;
import com.tracking.store.db.Store;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.util.DistanceFromMeComparator;
import com.tracking.store.util.GPSValue;
import com.tracking.store.util.PrefManager;
import com.tracking.store.util.Util;
import com.tracking.store.view.TProgressDialog;
import com.tracking.store.view.ViewGroups;
import com.tracking.store.web.WebCore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Irfan Ali on 2/28/2018.
 */

public class EmptyMapFragment extends Fragment{

    private GPSValue gpsValue = GPSValue.getInstance();
    private DBHandler dbHandler = DBHandler.getInstance();
    private ViewGroups viewGroup = ViewGroups.getInstance();
    private PrefManager prefManager = PrefManager.getPrefInstance();
    private WebCore webCore = WebCore.getInstance();

    private ArrayList<ShopMark> locationList;
    private GoogleMap googleMap;
    LatLng currentLatLng = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);//Menu Resource, Menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                ProgressDialog progressDialog = new TProgressDialog().createTProgressDialog(getActivity());
                int userID = prefManager.getUserID();
                webCore.pullFromServer(getActivity(), userID, progressDialog);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
