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
import android.widget.ImageView;
import android.widget.Toast;

import com.tracking.store.R;
import com.tracking.store.bean.Stocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZASS on 3/20/2018.
 */

public class CompitatorOrderListAdapter extends ArrayAdapter {
    Context context;
    List<Stocks> stocksList ;

    public CompitatorOrderListAdapter(Context context, List<Stocks> resource) {
        super(context, R.layout.item_row, resource);
        this.context = context;
        this.stocksList = resource;

        addRow();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.compitator_row, parent, false);

        EditText editText = (EditText) convertView.findViewById(R.id.txtItem);
        Button btnMinus = (Button) convertView.findViewById(R.id.btnMinus);
        Button btnPlus = (Button) convertView.findViewById(R.id.btnPlus);
        ImageView addRowBtn = (ImageView) convertView.findViewById(R.id.addRowBtn);
        final EditText editTextQty = (EditText) convertView.findViewById(R.id.editTextQty);



        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = stocksList.get(position).Qty;
                if(qty == 0){
                    qty = 0;
                }else{
                    --qty;
                }

                editTextQty.setText(""+qty);

                Stocks stocks =  stocksList.get(position);
                stocks.Qty = qty;
                stocksList.set(position, stocks);
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = stocksList.get(position).Qty;
                ++qty;
                editTextQty.setText(""+qty);

                Stocks stocks =  stocksList.get(position);
                stocks.Qty = qty;
                stocksList.set(position, stocks);
            }
        });

        editTextQty.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    int qty = Integer.parseInt(s.toString());
                    Stocks stocks =  stocksList.get(position);
                    stocks.Qty = qty;
                    stocksList.set(position, stocks);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    Stocks stocks =  stocksList.get(position);
                    stocks.Product = s.toString();
                    stocksList.set(position, stocks);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        addRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpityLastRow()){
                    addRow();
                }else{
                    Toast.makeText(context, "Add Item's title and Quantity", Toast.LENGTH_SHORT).show();
                }

            }
        });

        String product = stocksList.get(position).Product;
        if(!product.equals("")){
            addRowBtn.setVisibility(View.GONE);
        }

        editText.setText(product);
        editTextQty.setText(stocksList.get(position).Qty+"");
        return convertView;
    }

    public void addRow(){
        Stocks stocks = new Stocks();
        stocks.Product = "";
        stocks.Qty = 0;
        stocksList.add(stocks);
        notifyDataSetChanged();
    }

    public boolean isEmpityLastRow(){

        for(int i = 0; i < stocksList.size() ; i++){
            if(stocksList.get(i).Qty == 0 || stocksList.get(i).Product.equals("")){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Stocks> getStocksList(){
        ArrayList<Stocks> stringList = new ArrayList<>();
        if(!isEmpityLastRow()){
            for(int i = 0; i < stocksList.size() ; i++){
                if(stocksList.get(i).Qty != 0){
                    Stocks stock = stocksList.get(i);
                    stringList.add(stock);
                }
            }
        }else{
            Toast.makeText(context, "Add Item's title and Quantity", Toast.LENGTH_SHORT).show();
        }



        return stringList;
    }
}