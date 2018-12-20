package com.tracking.store.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tracking.store.App;
import com.tracking.store.R;
import com.tracking.store.ShopProfileAtivity;
import com.tracking.store.StoreDetailActivity;
import com.tracking.store.adapter.JourneyAdapter;
import com.tracking.store.db.Store;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.util.Util;
import com.tracking.store.view.FilterDialog;

import java.util.List;

/**
 * Created by Irfan Ali on 2/28/2018.
 */

public class EmptyStoreVisitsFragment extends Fragment {

    private DBHandler dbHandler = DBHandler.getInstance();
    private App appInstance = App.getAppInstance();

    private RecyclerView recyclerView;
    private JourneyAdapter journeyAdapter;
    private List<Store> storeList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_journey_cycle, container, false);
        return view;
    }

    public void refreshStoreVisit(Store store){
        journeyAdapter.add(store);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);//Menu Resource, Menu

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.box_background_color));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));
        searchAutoComplete.setHint("Search Store...");
        searchAutoComplete.setTextSize(14);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
