package com.notadeveloper.app.patadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {

  private AutoCompleteTextView email;
  private AutoCompleteTextView password;
  private TextInputLayout email1;
  private TextInputLayout password1;
  private DatabaseReference ref;
  private FirebaseAuth mAuth;
  private ProgressDialog mprogressDialog;
    private String s;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mprogressDialog = new ProgressDialog(this);
    mprogressDialog.setMessage("Authenticating...");
    ref = FirebaseDatabase.getInstance().getReference();
    Button proceed = findViewById(R.id.proceed);
    email = findViewById(R.id.email);
    password = findViewById(R.id.password);
    email1 = findViewById(R.id.email1);
    password1 = findViewById(R.id.password1);
    mAuth = FirebaseAuth.getInstance();
    proceed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean cancel = false;
        View focusView = null;

        final String a = email.getText().toString();
        final String b = password.getText().toString();

        if (isEmpty(a)) {
          email1.setError("Field cannot be empty");
          focusView = email1;
          cancel = true;
        } else {
          email1.setError(null);
        }

        if (isEmpty(b)) {
          password1.setError("Field cannot be empty");
          focusView = password1;
          cancel = true;
        } else {
          password1.setError(null);
        }

        if (cancel) {
          // There was an error; don't attempt login and focus the first
          // form field with an error.
          if (focusView != null) {
            focusView.requestFocus();
          }
        } else {

          validateadmin(a, b);
        }
      }
    });
  }

  void validateadmin(final String email, final String password) {
    mprogressDialog.show();
    ref.child("admins")
        .child(email.replace('.', '-'))
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            String p = dataSnapshot.getValue(String.class);
            if (p != null && p.equals(password)) {
              mAuth.signInWithEmailAndPassword(email, password)
                  .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                          if (email.equals("sheesha@admin.com")) {
                            mprogressDialog.dismiss();
                            Intent myIntent =
                                new Intent(LoginActivity.this, SheeshaAdminMain.class);
                            startActivity(myIntent);
                            finish();
                          } else if (email.endsWith("@pat.com")) {
                            ref.child("users")
                                .child(user.getUid())
                                .addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                        Users u = dataSnapshot.getValue(Users.class);
                                        if (u == null) {
                                          mprogressDialog.dismiss();
                                          Intent myIntent =
                                              new Intent(LoginActivity.this, BecomeOrganiser.class);
                                          myIntent.putExtra("ID", "party");
                                          startActivity(myIntent);
                                          finish();
                                        } else {
                                          mprogressDialog.dismiss();
                                          Intent myIntent =
                                              new Intent(LoginActivity.this,
                                                  OrganizerActivity.class);
                                          startActivity(myIntent);
                                          finish();
                                        }
                                      }

                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {
                                        mprogressDialog.dismiss();
                                      }
                                    });
                          } else if (email.endsWith("@club.com")) {
                            ref.child("users")
                                .child(user.getUid())
                                .addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                        Users u = dataSnapshot.getValue(Users.class);
                                        if (u == null) {
                                          mprogressDialog.dismiss();
                                          Intent myIntent =
                                              new Intent(LoginActivity.this, BecomeOrganiser.class);
                                          myIntent.putExtra("ID", "club");
                                          startActivity(myIntent);
                                          finish();
                                        }
                                        else if(u.getMyclub()==null)
                                        {
                                            mprogressDialog.dismiss();
                                            Intent myIntent =
                                                    new Intent(LoginActivity.this, ClubsMain.class);
                                            startActivity(myIntent);
                                            finish();
                                        }

                                        else {
                                          mprogressDialog.dismiss();
                                          Intent myIntent =
                                              new Intent(LoginActivity.this, ClubsMain.class);
                                          startActivity(myIntent);
                                          finish();
                                          //  FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                          //  final String uid = mUser.getUid();
                                          //  final DatabaseReference mDatabase =
                                          //          FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("myclub").child("clubid");
                                          //
                                          //  mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                          //      @Override
                                          //      public void onDataChange(DataSnapshot dataSnapshot) {
                                          //          s = dataSnapshot.getValue(String.class);
                                          //      }
                                          //
                                          //      @Override
                                          //      public void onCancelled(DatabaseError databaseError) {
                                          //
                                          //      }
                                          //  });
                                          //mprogressDialog.dismiss();
                                          //Intent myIntent =
                                          //    new Intent(LoginActivity.this, EditDetailedClubActivity.class);
                                          //  myIntent.putExtra("Club_id", s);
                                          //startActivity(myIntent);
                                          //finish();
                                        }
                                      }

                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {
                                        mprogressDialog.dismiss();
                                      }
                                    });
                          }
                          else if (email.equals("admin@admin.com"))
                          {
                            mprogressDialog.dismiss();
                            Intent myIntent =
                                    new Intent(LoginActivity.this, TheAdminActivity.class);
                            startActivity(myIntent);
                            finish();
                          }
                        } else {
                          mprogressDialog.dismiss();
                          Toast.makeText(LoginActivity.this, "Auth Fail", Toast.LENGTH_SHORT)
                              .show();
                        }
                      } else {
                        // If sign in fails, display a message to the user.
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                      // Sign in success, update UI with the signed-in user's information
                                      mprogressDialog.dismiss();
                                      Intent myIntent =
                                          new Intent(LoginActivity.this, BecomeOrganiser.class);
                                      startActivity(myIntent);
                                      finish();
                                    } else {
                                      // If sign in fails, display a message to the user.
                                      mprogressDialog.dismiss();
                                      Toast.makeText(LoginActivity.this, "Authentication failed.",
                                          Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                  }
                                });
                      }

                      // ...
                    }
                  });
            } else {
              mprogressDialog.dismiss();
              Toast.makeText(LoginActivity.this, "Wrong Credentials Try Again", Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override public void onCancelled(DatabaseError databaseError) {
            mprogressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Database Try Again", Toast.LENGTH_SHORT)
                .show();
          }
        });
  }
}
