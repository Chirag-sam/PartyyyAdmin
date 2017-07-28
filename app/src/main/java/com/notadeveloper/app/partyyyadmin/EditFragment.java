package com.notadeveloper.app.partyyyadmin;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
  ArrayList<shesha> list = new ArrayList<>();
  DatabaseReference ref;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView nopot;
    private ImageView edit;
    private Button add;
    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        ref = mDatabase.child("Sheesha");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datasnapshot) {

                for (DataSnapshot postSnapshot : datasnapshot.getChildren()) {
                    shesha s = postSnapshot.getValue(shesha.class);
                    if (!list.contains(s) && list != null) {
                        list.add(s);
                    }
                    SheeshaAdapter ss = new SheeshaAdapter(list, getActivity());
                    rv.setAdapter(ss);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nopot = (TextView)view.findViewById(R.id.nopot);
        edit = (ImageView)view.findViewById(R.id.edit);
        add = (Button)view.findViewById(R.id.add123);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref1 = ref.child("potprice");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                nopot.setText("₹"+snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog = new Dialog(getActivity(), R.style.dialogthemez);
                } else {
                    dialog = new Dialog(getActivity());
                }
                dialog.setContentView(R.layout.addflavourdialog);
                final AutoCompleteTextView flavour = (AutoCompleteTextView)dialog.findViewById(R.id.flavour);
                final TextView pottext = (TextView)dialog.findViewById(R.id.pottext);
                Button confirm = (Button)dialog.findViewById(R.id.confirm);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = flavour.getText().toString();
                        if(s.equals(null))
                        {
                            Toast.makeText(getActivity(), "Field cannot be empty!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref1 = ref.child("Sheesha");
                            ref1.child(s).child("flavour").setValue(s);
                            Toast.makeText(getActivity(),"Flavour successfully added!",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog = new Dialog(getActivity(), R.style.dialogthemez);
                } else {
                    dialog = new Dialog(getActivity());
                }
                dialog.setContentView(R.layout.editdialog);
                final AutoCompleteTextView price = (AutoCompleteTextView)dialog.findViewById(R.id.price);
                TextInputLayout price1 = (TextInputLayout) dialog.findViewById(R.id.price1);
                Button confirm = (Button)dialog.findViewById(R.id.confirm);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = price.getText().toString();
                        if(s.equals(null))
                        {
                            Toast.makeText(getActivity(), "Field cannot be empty!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref1 = ref.child("potprice");
                            ref1.setValue(s);
                            Toast.makeText(getActivity(),"Price successfully updated!",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            nopot.setText("₹"+s);
                        }
                    }
                });
                dialog.show();

            }
        });



        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);




    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
