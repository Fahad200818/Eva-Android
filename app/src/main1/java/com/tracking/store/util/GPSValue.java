package com.tracking.store.util;

import android.location.Location;

import im.delight.android.location.SimpleLocation;

/**
 * Created by ZASS on 4/13/2018.
 */

public class GPSValue {
    private static final GPSValue ourInstance = new GPSValue();

    public static GPSValue getInstance() {
        return ourInstance;
    }

    Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

