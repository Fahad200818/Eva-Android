package com.tracking.store.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.tracking.store.R;

/**
 * Created by ZASS on 4/14/2018.
 */

public  class ViewGroups {
    private static final ViewGroups ourInstance = new ViewGroups();

    public static ViewGroups getInstance() {
        return ourInstance;
    }


    public BitmapDescriptor getCurrentShopBedge(Activity context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_shop_ledge, null);
        BitmapDescriptor bitmap =BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view));
        return bitmap;
    }

    public Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int width = 500; // v.getMeasuredWidth();
            int height = 500;  //v.getMeasuredHeight();

            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, width, height);
            v.draw(c);
            return b;
        }
        return null;
    }

    public void showSnackMessage(View view, String msg, int duration) {
        Snackbar snackbar = Snackbar.make(view, "" + msg,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.color_highlight));
        snackbar.setDuration(duration);
        snackbar.show();
    }

}
