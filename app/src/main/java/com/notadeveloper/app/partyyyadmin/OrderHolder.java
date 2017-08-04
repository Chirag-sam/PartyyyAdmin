package com.notadeveloper.app.partyyyadmin;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by xtrem on 7/27/2017.
 */

public class OrderHolder extends RecyclerView.ViewHolder {
    final TextView orderid;
    final TextView date;
    final TextView price;
    final TextView status;
    final TextView noofpot;
    final TextView deliverby;
    final TextView details;
    final CardView cv1;
    final Switch switch1;
    final TextView flavours;

    public OrderHolder(View itemView) {
        super(itemView);
        cv1 = (CardView) itemView.findViewById(R.id.cv1);
        orderid = (TextView)itemView.findViewById(R.id.orderid);
        date = (TextView)itemView.findViewById(R.id.date);
        price = (TextView)itemView.findViewById(R.id.orderprice);
        status= (TextView) itemView.findViewById(R.id.orderstatus);
        noofpot= (TextView) itemView.findViewById(R.id.noofpot);
        deliverby = (TextView) itemView.findViewById(R.id.deliverby);
        details = (TextView) itemView.findViewById(R.id.details);
        switch1 = (Switch)itemView.findViewById(R.id.switch1);
        flavours = (TextView)itemView.findViewById(R.id.flavours);

    }
}
