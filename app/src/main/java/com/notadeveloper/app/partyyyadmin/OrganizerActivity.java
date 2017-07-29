package com.notadeveloper.app.partyyyadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import java.util.ArrayList;

import static android.text.Html.fromHtml;

public class OrganizerActivity extends AppCompatActivity {

  @BindView(R.id.recyclerview)
  RecyclerView mRecyclerview;
  String uid;
  LinearLayoutManager mLayoutManager;
  PartyAdapter mPartyAdapter;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.fab1) FabSpeedDial fab;
  private Users u = new Users();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_organizer);
    ButterKnife.bind(this);
    loadadapter();
    fab.setMenuListener(new SimpleMenuListenerAdapter() {
      @Override
      public boolean onMenuItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.privateparty:
            Intent i = new Intent(OrganizerActivity.this, AddAParty.class);
            startActivity(i);
            break;
          case R.id.publicparty:
            Intent x = new Intent(OrganizerActivity.this, AddAParty.class);
            startActivity(x);
            break;
        }

        return false;
      }
    });

    toolbar.setTitle(fromHtml("<font color='#D4AF37'>    Partyyy</font>"));

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.parseColor("#000000"));
    }

    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerview.setLayoutManager(mLayoutManager);
    mRecyclerview.setHasFixedSize(true);
  }

  void loadadapter() {
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    uid = mUser.getUid();
    mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        u = dataSnapshot.getValue(Users.class);
        ArrayList<String> ls;
        ls = u.getMyparties();
        if (ls != null) {
          final ArrayList<String> finalLs = ls;
          mDatabase.child("parties").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              ArrayList<Party> partls = new ArrayList<Party>();
              for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (finalLs.contains(ds.getKey())) {
                  partls.add(ds.getValue(Party.class));
                }
              }
              mPartyAdapter = new PartyAdapter(partls, OrganizerActivity.this);
              mRecyclerview.setAdapter(mPartyAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          });
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }
  @Override
  public void onBackPressed() {

    final AlertDialog.Builder builder = new AlertDialog.Builder(OrganizerActivity.this, R.style.pop);
    builder.setMessage("Are You Sure you want to exit?");
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
      }
    });
    builder.setNegativeButton("No", null);
    builder.show();
    //  super.onBackPressed();
  }
}
