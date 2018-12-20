package com.tracking.store.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tracking.store.R;
import com.tracking.store.adapter.OrderListAdapter;
import com.tracking.store.bean.Stocks;
import com.tracking.store.db.InventoryItem;
import com.tracking.store.util.Util;

import java.util.ArrayList;

/**
 * Created by ZASS on 3/22/2018.
 */

public class InventoryTakingDialog extends Dialog {


    private Context context;
    private OrderListAdapter orderListAdapter;
    private Button addBtn;
    private InventoryCallback inventoryCallback;
    private ImageView closeBtn;
    private long storeID;

    public InventoryTakingDialog(Context context, long storeId, InventoryCallback inventoryCallback) {
        super(context);
        this.context = context;
        this.inventoryCallback = inventoryCallback;
        this.storeID = storeId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inventory_dialog);
        closeBtn =(ImageView)findViewById(R.id.closeBtn);
        addBtn =(Button) findViewById(R.id.addBtn);

        ListView listView = (ListView)findViewById(R.id.listview);
        orderListAdapter = new OrderListAdapter(context, getItemList());

        listView.setAdapter(orderListAdapter);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Stocks> stringArrayList = orderListAdapter.getStocksList();
                if(stringArrayList.size() == 0){
                    Toast.makeText(context, R.string.please_add_inventory_with_Qty, Toast.LENGTH_SHORT).show();
                }else {
                    inventoryCallback.InventoryGetter(stringArrayList);
                    dismiss();
                }
            }
        });

        setCancelable(false);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }

    public ArrayList<Stocks> getItemList(){
        ArrayList<Stocks> stringArrayList = new ArrayList<>();
        Stocks stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(StandUpPouch)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(StandUpPouch)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(3 Ltr Bottle)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(5 Ltr Bottle)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);
        stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(5 Ltr Tin)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(16 Ltr Tin)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Cooking Oil(16 Ltr J/Can)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Canola Oil(StandUpPouch)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Canola Oil(StandUpPouch)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = " Eva Canola Oil(3 Ltr Bottle)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = " Eva Canola Oil(5 Ltr Bottle)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Canola Oil(10 Ltrs J/Can))";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Canola Oil 16 Ltr Tin)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Canola Oil 5 Litr Tin(CP)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Sunflower 1x5 (StandUpPouch)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Sunflower 1x5 (StandUpPouch)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Sunflower Oil 1x5 Pouch(P.P)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Sunflower Oil 3 Ltr Pet Bottle";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva Sunflower Oil 5 Ltr Pet Bottle";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Oil 1x5 Pouch";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Cooking Oil 1x10 Pouch";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Oil 1x12 Pouch";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Cooking Oil 3 Ltr Pet Bottle";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Cooking Oil 5 Ltr Pet Bottle";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Oil (10 Ltrs J/Can)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Oil 16 Ltrs J/Can";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Oil 16 Ltrs Tin";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva VTF Banaspati 1x5";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva VTF Banaspati 1x5";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva VTF Banaspati 5 Kg Tin";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva VTF Banaspati 5 Kg Bucket";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Eva VTF Banaspati 5 Kg Tin (CP)";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1/4 x 48";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = " Maan Banaspati 1/4 x 64";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1/2 x 32";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1/2 x 24";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1x5";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1x10 P.Pouch";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = " Maan Banaspat 1x12";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1x16 Pouch";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 1x16 Pouch";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 2.5 Kgs Tin";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 5 Kgs Tin";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 5 Kg Bucket";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 10 Kg Bucket";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 16 Kg Bucket";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        stocks = new Stocks();
        stocks.Product = "Maan Banaspati 16 Kgs Tin";
        stocks.Qty = 0;
        stringArrayList.add(stocks);

        return stringArrayList;
    }

    public interface InventoryCallback{
        public void InventoryGetter(ArrayList<Stocks> inventoryItemArrayList);
    }
}

