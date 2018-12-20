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
import com.tracking.store.MainActivity;
import com.tracking.store.R;
import com.tracking.store.ShopProfileAtivity;
import com.tracking.store.StoreDetailActivity;
import com.tracking.store.adapter.JourneyAdapter;
import com.tracking.store.db.Store;
import com.tracking.store.dbcontroller.DBHandler;
import com.tracking.store.util.Util;
import com.tracking.store.view.FilterDialog;
import com.tracking.store.view.ViewGroups;

import java.util.List;

/**
 * Created by Irfan Ali on 2/28/2018.
 */

public class StoreVisitsFragment extends Fragment {

    private DBHandler dbHandler = DBHandler.getInstance();
    private App appInstance = App.getAppInstance();
    private Util utilInstance = Util.getInstance();
    private ViewGroups viewGroups = ViewGroups.getInstance();

    FilterDialog filterDialog;
    private RecyclerView recyclerView;
    private JourneyAdapter journeyAdapter;
    private List<Store> storeList;
    private String status = "Target";

    private boolean IsTarget = true;

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
        appInstance.storeVisitsFragment = this;

        int day = Util.getInstance().getDayOfWeek();

        String currentDate = utilInstance.getCurrentDate();
        storeList = dbHandler.getStoreList("Target",  currentDate);

        recyclerView = (RecyclerView)view.findViewById(R.id.journey_recycler_view);

        if(journeyAdapter == null){
            journeyAdapter = new JourneyAdapter(storeList,getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(journeyAdapter);
        }else{
            journeyAdapter.refreshStoreVisist(storeList);
        }

        journeyAdapter.setOnItemClickListener(new JourneyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int positioon) {

                String isFockLocationEnabled = "";//utilInstance.isMockLocationEnabled(getActivity());
                if(!isFockLocationEnabled.equals("")){
                    viewGroups.showSnackMessage(view, "You are using fake GPS location app : "+isFockLocationEnabled+" , please disable that app", 20000);
                }else{
                    try{
                        Store store = storeList.get(positioon);
                        StoreDetailActivity.storeInfo = store;
                        StoreDetailActivity.Status = status;
                        ShopProfileAtivity.storeInfo = store;
                        ShopProfileAtivity.Status = status;
                        startActivity(new Intent(getActivity(), ShopProfileAtivity.class));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        });
        return view;
    }

    public void refreshStoreVisit(Store store){
        journeyAdapter.add(store);
    }

    public void updateStoreVisit(Store store){
       journeyAdapter.update(store);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);//Menu Resource, Menu

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.box_background_color));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));
        searchAutoComplete.setHint("Search Store...");
        searchAutoComplete.setTextSize(14);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                journeyAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public void popupForFilter(){
        filterDialog = new FilterDialog(getActivity(), new FilterDialog.SelectionCallBack() {
            @Override
            public void selection(String selection) {
                String currentDate = utilInstance.getCurrentDate();
                if(selection.equals("Target")){
                    status = "Target";
                    storeList = dbHandler.getStoreList("Target", currentDate);
                }else if(selection.equals("Achieved")){
                    status = "Achieved";
                    storeList = dbHandler.getStoreList("Achieved", currentDate);
                }else{
                    status = "Extra";
                    storeList = dbHandler.getStoreList("Extra", currentDate);
                }

                journeyAdapter.refreshStoreVisist(storeList);
                filterDialog.dismiss();
            }
        });
        filterDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                popupForFilter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        if(status.equals("Target")){
            String currentDate = utilInstance.getCurrentDate();
            storeList = dbHandler.getStoreList("Target",  currentDate);
            if(journeyAdapter != null){
                journeyAdapter.refreshStoreVisist(storeList);
            }
        }

        super.onResume();
    }
}
