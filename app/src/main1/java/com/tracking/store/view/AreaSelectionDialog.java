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
import com.tracking.store.adapter.AreaAdapter;
import com.tracking.store.db.Area;

import java.util.List;

/**
 * Created by ZASS on 3/22/2018.
 */

public class AreaSelectionDialog extends Dialog {

    private Context context;
    private SelectionCallBack selectionCallBack;
    private String title;
    private List<Area> areaList;
    private ListView listView;
    private EditText inputSearch;
    AreaAdapter townAdapter;
    private TextView textViewEmpty;

    public AreaSelectionDialog(Context context, String title, List<Area> objectList, SelectionCallBack selectionCallBack) {
        super(context);
        this.context = context;
        this.title = title;
        this.selectionCallBack = selectionCallBack;
        this.areaList = objectList;
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

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        townAdapter = new AreaAdapter(context, areaList);
        txt_title.setText(title);

        if (areaList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.INVISIBLE);
            textViewEmpty.setVisibility(View.VISIBLE);
            textViewEmpty.setText("Area not found");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Area selection = areaList.get(position);
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

        setCancelable(false);

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
    }

    public interface SelectionCallBack {
        public void selection(Area selection);
    }

}

