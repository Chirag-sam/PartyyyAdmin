package com.notadeveloper.app.partyyyadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.TextUtils.isEmpty;

public class BecomeOrganiser extends AppCompatActivity {

  final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
  @BindView(R.id.register)
  Button register;
  @BindView(R.id.orgname)
  AutoCompleteTextView orgname;
  @BindView(R.id.orgname1)
  TextInputLayout orgname1;
  @BindView(R.id.check)
  CheckBox check;
  @BindView(R.id.terms)
  TextView terms;
  @BindView(R.id.number) AutoCompleteTextView number;
  @BindView(R.id.number1) TextInputLayout number1;
  @BindView(R.id.name) AutoCompleteTextView name;
  @BindView(R.id.name1) TextInputLayout name1;
  @BindView(R.id.pass) AutoCompleteTextView pass;
  @BindView(R.id.pass1) TextInputLayout pass1;
  String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_become_organiser);
    ButterKnife.bind(this);

    terms.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BecomeOrganiser.this);
        builder.setTitle("Terms and conditions");
        builder.setMessage("T & C");
        builder.setPositiveButton("OK", null);
        // builder.setNegativeButton("Cancel", null);
        builder.show();
      }
    });
  }

  @OnClick(R.id.register)
  public void onViewClicked() {

    mDatabase = FirebaseDatabase.getInstance().getReference();

    boolean cancel = false;
    View focusView = null;

    final String a = orgname.getText().toString();
    final String b = number.getText().toString();
    final String c = name.getText().toString();
    final String passw = pass.getText().toString();
    if (isEmpty(a)) {
      orgname1.setError("Field cannot be empty");
      focusView = orgname1;
      cancel = true;
    } else {
      orgname1.setError(null);
    }
    if (isEmpty(passw)) {
      pass1.setError("Field cannot be empty");
      focusView = pass1;
      cancel = true;
    } else {
      pass1.setError(null);
    }
    if (isEmpty(c)) {
      name1.setError("Field cannot be empty");
      focusView = name1;
      cancel = true;
    } else {
      name1.setError(null);
    }
    if (isEmpty(b) || b.length() != 10) {
      number1.setError("Field cannot be empty");
      focusView = number1;
      cancel = true;
    } else {
      number1.setError(null);
    }
    if (!check.isChecked()) {
      cancel = true;
      focusView = null;
      Toast.makeText(BecomeOrganiser.this, "Accept the terms and conditions first!",
          Toast.LENGTH_LONG).show();
    }

    if (cancel) {
      // form field with an error.
      if (focusView != null) {
        focusView.requestFocus();
      }
    } else {
      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

      user.updatePassword(passw)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                mDatabase.child("admins").child(email.replace('.', '-')).setValue(passw);
              }
            }
          });
      mDatabase.child("users").child(uid).child("orgname").setValue(a);
      mDatabase.child("users").child(uid).child("isorganizer").setValue(true);
      mDatabase.child("users").child(uid).child("number").setValue(b);
      mDatabase.child("users").child(uid).child("uid").setValue(uid);
      mDatabase.child("users").child(uid).child("name").setValue(c);
      mDatabase.child("users").child(uid).child("email").setValue(email);

        String s;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
             s = bundle.getString("name");
        }
        else s=" ";

      if(s.equals("party"))
      {
        Intent i = new Intent(BecomeOrganiser.this, OrganizerActivity.class);
        startActivity(i);
      }
      else
      {
        Intent i = new Intent(BecomeOrganiser.this, ClubsMain.class);
        i.putExtra("First time","True");
          i.putExtra("clubid"," ");
        startActivity(i);
      }

    }
  }
}
