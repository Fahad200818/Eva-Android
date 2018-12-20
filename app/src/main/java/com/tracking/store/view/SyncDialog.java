package com.tracking.store.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tracking.store.R;
import com.tracking.store.util.Constants;
import com.tracking.store.util.PrefManager;

/**
 * Created by ZASS on 3/22/2018.
 */

public class SyncDialog extends Dialog {

    private Context context;
    PrefManager prefManager = PrefManager.getPrefInstance();
    private String filterStr;
    private SelectionCallBack selectionCallBack;


    public SyncDialog(Context context, SelectionCallBack selectionCallBack) {
        super(context);
        this.context = context;
        this.selectionCallBack = selectionCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sync_dialog);

        ImageView closeBtn =(ImageView)findViewById(R.id.closeBtn);
        TextView btn_pushToServer =(TextView)findViewById(R.id.btn_pushToServer);
        TextView btn_pullFromServer =(TextView)findViewById(R.id.btn_pullFromServer);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        filterStr = (String)prefManager.get(Constants.Filter, new String());


        btn_pushToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.put(Constants.Filter, "Target");
                selectionCallBack.selection("Push");
            }
        });

        btn_pullFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.put(Constants.Filter, "Achieved");
                selectionCallBack.selection("Pull");
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionCallBack.cancel();
                dismiss();

            }
        });

        setCancelable(false);

    }


    public interface SelectionCallBack{
        public void selection(String pushOrPull);
        public void cancel();
    }

}

