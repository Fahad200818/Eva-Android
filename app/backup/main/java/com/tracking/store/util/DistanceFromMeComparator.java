package com.tracking.store.util;

import com.google.android.gms.maps.model.LatLng;
import com.tracking.store.bean.Point;
import com.tracking.store.bean.ShopMark;

import java.util.Comparator;

/**
 * Created by ZASS on 4/10/2018.
 */

public class DistanceFromMeComparator implements Comparator<ShopMark> {

    LatLng me;
    public DistanceFromMeComparator(LatLng me) {
        this.me = me;
    }

    private Double distanceFromMe(LatLng p) {
        double theta = p.longitude - me.longitude;
        double dist = Math.sin(deg2rad(p.latitude)) * Math.sin(deg2rad(me.latitude))
                + Math.cos(deg2rad(p.latitude)) * Math.cos(deg2rad(me.latitude))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist;
    }

    private double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
    private double rad2deg(double rad) { return (rad * 180.0 / Math.PI); }

    @Override
    public int compare(ShopMark p1, ShopMark p2) {
        return distanceFromMe(p1.latLng).compareTo(distanceFromMe(p2.latLng));
    }
}