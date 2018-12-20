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

public class FilterDialog extends Dialog {

    private Context context;
    PrefManager prefManager = PrefManager.getPrefInstance();
    private String filterStr;
    private SelectionCallBack selectionCallBack;


    public FilterDialog(Context context, SelectionCallBack selectionCallBack) {
        super(context);
        this.context = context;
        this.selectionCallBack = selectionCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_dialog);

        ImageView closeBtn =(ImageView)findViewById(R.id.closeBtn);
        TextView btn_target =(TextView)findViewById(R.id.btn_target);
        TextView btn_achieved =(TextView)findViewById(R.id.btn_achieved);
        TextView btn_extra =(TextView)findViewById(R.id.btn_extra);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        filterStr = (String)prefManager.get(Constants.Filter, new String());

        if(filterStr.equals("Target")){
            btn_target.setBackgroundResource(R.drawable.button_small_curve);
        }else if(filterStr.equals("Achieved")){
            btn_achieved.setBackgroundResource(R.drawable.button_small_curve);
        }else{
            btn_extra.setBackgroundResource(R.drawable.button_small_curve);
        }

        btn_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.put(Constants.Filter, "Target");
                selectionCallBack.selection("Target");
            }
        });

        btn_achieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.put(Constants.Filter, "Achieved");
                selectionCallBack.selection("Achieved");
            }
        });

        btn_extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.put(Constants.Filter, "Extra");
                selectionCallBack.selection("Extra");
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        setCancelable(false);

    }


    public interface SelectionCallBack{
        public void selection(String selection);
    }

}

