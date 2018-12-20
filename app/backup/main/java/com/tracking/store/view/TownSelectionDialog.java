package com.tracking.store.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tracking.store.R;
import com.tracking.store.adapter.TownAdapter;
import com.tracking.store.db.Town;
import com.tracking.store.util.Constants;
import com.tracking.store.util.PrefManager;

import java.util.List;

/**
 * Created by ZASS on 3/22/2018.
 */

public class TownSelectionDialog extends Dialog {

    private Context context;
    PrefManager prefManager = PrefManager.getPrefInstance();
    private String filterStr;
    private SelectionCallBack selectionCallBack;
    private String title;
    private List<Town> townList;
    private ListView listView;
    private EditText inputSearch;
    TownAdapter townAdapter;
    private TextView textViewEmpty;

    public TownSelectionDialog(Context context, String title, List<Town> objectList, SelectionCallBack selectionCallBack) {
        super(context);
        this.context = context;
        this.title = title;
        this.selectionCallBack = selectionCallBack;
        this.townList = objectList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.town_area_dialogbox);

        ImageView closeBtn = (ImageView) findViewById(R.id.closeBtn);
        TextView txt_title = (TextView) findViewById(R.id.txt_title);

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        listView = (ListView) findViewById(R.id.listview);

        if (townList.size() > 0) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        filterStr = (String) prefManager.get(Constants.Filter, new String());


        townAdapter = new TownAdapter(context, townList);
        txt_title.setText(title);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Town selection = townList.get(position);
                selectionCallBack.selection(selection);
                dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                townAdapter.filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listView.setAdapter(townAdapter);
        setCancelable(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public interface SelectionCallBack {
        public void selection(Town selection);
    }

}

