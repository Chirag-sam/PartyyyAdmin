package com.notadeveloper.app.patadmin;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Chirag on 13-Apr-17.
 */

public class TicketHolder extends RecyclerView.ViewHolder {

  final TextView name;
  final TextView orderprice;
  final TextView nameText;

  final TextView stagprice;
  final TextView coupleprice;
  final CardView cv1;

  public TicketHolder(View itemView) {
    super(itemView);

    name = itemView.findViewById(R.id.name);
    orderprice = itemView.findViewById(R.id.orderprice);
    nameText = itemView.findViewById(R.id.nametext);
    stagprice = itemView.findViewById(R.id.stagprice);
    coupleprice = itemView.findViewById(R.id.coupleprice);
    cv1 = itemView.findViewById(R.id.cv1);
  }
}
