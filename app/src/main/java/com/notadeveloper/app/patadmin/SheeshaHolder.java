package com.notadeveloper.app.patadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
      flavor = itemView.findViewById(R.id.flavor);
      edit = itemView.findViewById(R.id.edit);
      delete = itemView.findViewById(R.id.delete);
    }
}
