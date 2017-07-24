package com.notadeveloper.app.partyyyadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xtrem on 7/24/2017.
 */

public class SheeshaHolder extends RecyclerView.ViewHolder {

    TextView flavor;
    ImageView edit;
    ImageView delete;

    public SheeshaHolder(View itemView) {
        super(itemView);
        flavor = (TextView)itemView.findViewById(R.id.flavor);
        edit = (ImageView)itemView.findViewById(R.id.edit);
        delete = (ImageView)itemView.findViewById(R.id.delete);
    }
}
