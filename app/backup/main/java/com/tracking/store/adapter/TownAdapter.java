package com.tracking.store.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.tracking.store.R;
import com.tracking.store.bean.Stocks;
import com.tracking.store.db.Store;
import com.tracking.store.db.Town;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZASS on 3/20/2018.
 */

public class TownAdapter extends ArrayAdapter {
    Context context;
    private List<Town> townList;
    private List<Town> mFilteredList;


    public TownAdapter(Context context, List<Town> townList) {
        super(context, R.layout.town_area_row, townList);
        this.context = context;
        this.townList = townList;
        mFilteredList = new ArrayList<>();
        this.mFilteredList.addAll(townList);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.town_area_row, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.textid);

        textView.setText(townList.get(position).name);
        return convertView;
    }


    public void filter(String filter) {

        townList.clear();
        if (filter.length() == 0) {
            townList.addAll(mFilteredList);
        }
        else
        {
            for (Town  town : mFilteredList)
            {
                if (town.name.toLowerCase(Locale.getDefault()).contains(filter))
                {
                    townList.add(town);
                }
            }
        }
        notifyDataSetChanged();

    }
}