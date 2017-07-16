package com.notadeveloper.app.partyyyadmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class detailedpartyactivity extends AppCompatActivity {

  @BindView(R.id.iv)
  ImageView iv;
  @BindView(R.id.month)
  TextView month;
  @BindView(R.id.date)
  TextView date;
  @BindView(R.id.day)
  TextView day;
  @BindView(R.id.line)
  RelativeLayout line;
  @BindView(R.id.pname)
  TextView pname;
  @BindView(R.id.loct)
  TextView loct;
  @BindView(R.id.timet)
  TextView timet;
  @BindView(R.id.descrip)
  TextView descrip;
  @BindView(R.id.addt)
  TextView addt;
  @BindView(R.id.add1)
  TextView add1;
  @BindView(R.id.add2)
  TextView add2;
  @BindView(R.id.city)
  TextView city;
  @BindView(R.id.pin)
  TextView pin;
  @BindView(R.id.contactt)
  TextView contactt;
  @BindView(R.id.email)
  TextView email;
  @BindView(R.id.phoneicon)
  ImageView phoneicon;
  @BindView(R.id.phone)
  TextView phone;
  @BindView(R.id.book)
  Button book;
  Users u = new Users();
  DatabaseReference ref;
  Party p = new Party();
  @BindView(R.id.noticket)
  TextView noticket;
  @BindView(R.id.stagprice)
  TextView stagprice;
  @BindView(R.id.coupleprice)
  TextView coupleprice;
  private String photoUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detailedpartyactivity);
    ButterKnife.bind(this);
    getUser();
    String s = getIntent().getStringExtra("party_id");

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ref = mDatabase.child("parties").child(s);
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        p = dataSnapshot.getValue(Party.class);

        Glide.with(detailedpartyactivity.this).load(p.getPicture()).into(iv);

        final String d = p.getDates();

        String a[] = d.split("/");

        if (a[0].length() < 2) {
          a[0] = "0" + a[0];
        }

        date.setText(a[0]);
        if (a[1].equals("01")) {
          month.setText("JAN");
        } else if (a[1].equals("02")) {
          month.setText("FEB");
        } else if (a[1].equals("03")) {
          month.setText("MAR");
        } else if (a[1].equals("04")) {
          month.setText("APR");
        } else if (a[1].equals("05")) {
          month.setText("MAY");
        } else if (a[1].equals("06")) {
          month.setText("JUN");
        } else if (a[1].equals("07")) {
          month.setText("JLY");
        } else if (a[1].equals("08")) {
          month.setText("AUG");
        } else if (a[1].equals("09")) {
          month.setText("SEP");
        } else if (a[1].equals("10")) {
          month.setText("OCT");
        } else if (a[1].equals("11")) {
          month.setText("NOV");
        } else if (a[1].equals("12")) {
          month.setText("DEC");
        }

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date datee = null;
        try {
          datee = (Date) formatter.parse(d);
        } catch (ParseException e) {
          e.printStackTrace();
        }
        long l = datee.getTime();

        Date date3 = new Date(l);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(datee);

        if (dayOfTheWeek.equals("Monday")) {
          dayOfTheWeek = "MON";
        } else if (dayOfTheWeek.equals("Tuesday")) {
          dayOfTheWeek = "TUE";
        } else if (dayOfTheWeek.equals("Wednesday")) {
          dayOfTheWeek = "WED";
        } else if (dayOfTheWeek.equals("Thursday")) {
          dayOfTheWeek = "THU";
        } else if (dayOfTheWeek.equals("Friday")) {
          dayOfTheWeek = "FRI";
        } else if (dayOfTheWeek.equals("Saturday")) {
          dayOfTheWeek = "SAT";
        } else if (dayOfTheWeek.equals("Sunday")) {
          dayOfTheWeek = "SUN";
        }

        day.setText(dayOfTheWeek);

        pname.setText(p.getTitle());
        loct.setText(p.getAddress3());
        timet.setText(p.getTime() + " to " + p.getTime1());
        descrip.setText(p.getDescription());
        add1.setText(p.getAddress1());
        add2.setText(p.getAddress2());
        city.setText(p.getAddress3());
        pin.setText(p.getPincode());
        phone.setText(p.getNumber());
        email.setText(p.getEmail());
        noticket.setText(p.getTickets() + " Available");
        stagprice.setText("(Stag) ₹" + p.getPricestag());
        coupleprice.setText("(Couple) ₹" + p.getPricecouple());
        book.setText("Remove Party");
        book.setOnClickListener(new View.OnClickListener() {

          @Override public void onClick(View view) {

          }
        });
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  void getUser() {
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = mUser.getUid();
    final DatabaseReference mDatabase =
        FirebaseDatabase.getInstance().getReference().child("users").child(uid);

    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        u = dataSnapshot.getValue(Users.class);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }
}
