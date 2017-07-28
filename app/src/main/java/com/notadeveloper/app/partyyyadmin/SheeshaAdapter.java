package com.notadeveloper.app.partyyyadmin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                final Dialog dialog;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog = new Dialog(mContext, R.style.dialogthemez);
                } else {
                    dialog = new Dialog(mContext);
                }
                dialog.setContentView(R.layout.addflavourdialog);
                final AutoCompleteTextView flavour = (AutoCompleteTextView)dialog.findViewById(R.id.flavour);
                final TextView pottext = (TextView)dialog.findViewById(R.id.pottext);
                Button confirm = (Button)dialog.findViewById(R.id.confirm);

                pottext.setText("Edit the flavour:");
                flavour.setText(s.getFlavour());
                final String s1 = s.getFlavour();

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = flavour.getText().toString();
                        if(s.equals(null))
                        {
                            Toast.makeText(mContext, "Field cannot be empty!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref1 = ref.child("Sheesha");
                            ref1.child(s1).setValue(null);
                            ref1.child(s).child("flavour").setValue(s);
                            Toast.makeText(mContext,"Flavour successfully edited!",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String s1 = s.getFlavour();
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(mContext, R.style.pop);
                builder.setMessage("Are You Sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref1 = ref.child("Sheesha");
                        ref1.child(s1).setValue(null);
                        Toast.makeText(mContext,"Flavour successfully deleted!",Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("No", null);
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
