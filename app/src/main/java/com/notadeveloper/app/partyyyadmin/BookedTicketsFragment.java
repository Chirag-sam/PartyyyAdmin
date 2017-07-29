package com.notadeveloper.app.partyyyadmin;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookedTicketsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookedTicketsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycl)
    RecyclerView recycl;
    List<Party.BookedTickets> lc = new ArrayList<>();
    TicketAdapter o;
    LinearLayoutManager mLayoutManager;
    Unbinder unbinder;


    // TODO: Rename and change types of parameters
    private String mParam1;


    public BookedTicketsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BookedTicketsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookedTicketsFragment newInstance(String param1) {
        BookedTicketsFragment fragment = new BookedTicketsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked_tickets, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recycl.setHasFixedSize(true);
        recycl.setLayoutManager(mLayoutManager);
        setupadapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void setupadapter() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


        mDatabase.child("parties")
                .child(mParam1)
                .child("ticketsBooked")
                .orderByValue()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot datasnapshot) {
                        lc = new ArrayList<Party.BookedTickets>();
                        for (DataSnapshot postSnapshot : datasnapshot.getChildren()) {
                            Party.BookedTickets u = postSnapshot.getValue(Party.BookedTickets.class);
                            if (u != null) {
                                if (!lc.contains(u)) {
                                    lc.add(u);
                                }
                            }
                        }
                        o = new TicketAdapter(lc, getContext());
                        recycl.setAdapter(o);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
