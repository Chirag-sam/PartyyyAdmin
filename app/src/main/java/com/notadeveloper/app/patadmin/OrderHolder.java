package com.notadeveloper.app.patadmin;

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
      cv1 = itemView.findViewById(R.id.cv1);
      orderid = itemView.findViewById(R.id.orderid);
      date = itemView.findViewById(R.id.date);
      price = itemView.findViewById(R.id.orderprice);
      status = itemView.findViewById(R.id.orderstatus);
      noofpot = itemView.findViewById(R.id.noofpot);
      deliverby = itemView.findViewById(R.id.deliverby);
      details = itemView.findViewById(R.id.details);
      switch1 = itemView.findViewById(R.id.switch1);
      flavours = itemView.findViewById(R.id.flavours);

    }
}
