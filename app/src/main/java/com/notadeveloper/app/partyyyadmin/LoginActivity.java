package com.notadeveloper.app.partyyyadmin;

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
import java.util.HashMap;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {

  private Button proceed;
  private AutoCompleteTextView email;
  private AutoCompleteTextView password;
  private TextInputLayout email1;
  private TextInputLayout password1;
  private HashMap<String, String> admins;
  private DatabaseReference ref;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ref = FirebaseDatabase.getInstance().getReference();
    proceed = (Button) findViewById(R.id.proceed);
    email = (AutoCompleteTextView) findViewById(R.id.email);
    password = (AutoCompleteTextView) findViewById(R.id.password);
    email1 = (TextInputLayout) findViewById(R.id.email1);
    password1 = (TextInputLayout) findViewById(R.id.password1);
    admins = new HashMap<>();
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
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();


                      } else {
                        // If sign in fails, display a message to the user.
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                  // Sign in success, update UI with the signed-in user's information

                                  FirebaseUser user = mAuth.getCurrentUser();
                                  Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                  startActivity(myIntent);
                                  finish();
                                } else {
                                  // If sign in fails, display a message to the user.

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

              Toast.makeText(LoginActivity.this, "Wrong Credentials Try Again", Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(LoginActivity.this, "Database Try Again", Toast.LENGTH_SHORT)
                .show();
          }
        });
  }
}
