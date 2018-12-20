package com.tracking.store.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tracking.store.R;
import com.tracking.store.db.Names;
import com.tracking.store.db.Town;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZASS on 3/20/2018.
 */

public class NamesAutocompleteAdapter extends ArrayAdapter {
    Context context;
    private List<Names> namesList;


    public NamesAutocompleteAdapter(Context context,int id, List<Names> townList) {
        super(context, id , townList);
        this.context = context;
        this.namesList = townList;
        ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.town_area_row, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.textid);

        textView.setText(namesList.get(position).name_);
        return convertView;
    }


    @Override
    public int getCount() {
        return namesList.size();
    }

}