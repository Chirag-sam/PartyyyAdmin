package com.notadeveloper.app.partyyyadmin;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by xtrem on 7/24/2017.
 */

public class SheeshaAdapter extends RecyclerView.Adapter<SheeshaHolder> {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ArrayList<shesha> list;
    Context mContext;

    public SheeshaAdapter(ArrayList<shesha> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public SheeshaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.sheeshacard, parent, false);

        return new SheeshaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SheeshaHolder holder, int position) {

        final shesha s = list.get(position);

        holder.flavor.setText(s.getFlavour());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
