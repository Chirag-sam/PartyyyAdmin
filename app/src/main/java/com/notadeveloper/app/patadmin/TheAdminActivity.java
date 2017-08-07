package com.notadeveloper.app.patadmin;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.isEmpty;

public class TheAdminActivity extends AppCompatActivity {

    private TextInputLayout title1;
    private AutoCompleteTextView title;
    private TextInputLayout title2;
    private AutoCompleteTextView title22;
    private Button add;
    private TextInputLayout tit1;
    private AutoCompleteTextView tit;
    private TextInputLayout tit2;
    private AutoCompleteTextView tit22;
    private Button add1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_admin);

        title1 = findViewById(R.id.title1);
        title = findViewById(R.id.title);
        title2 = findViewById(R.id.title2);
        title22 = findViewById(R.id.title22);
        add = findViewById(R.id.add);
        tit1 = findViewById(R.id.tit1);
        tit = findViewById(R.id.tit);
        tit2 = findViewById(R.id.tit2);
        tit22 = findViewById(R.id.tit22);
        add1 = findViewById(R.id.add1);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;

                final String a = title.getText().toString();
                final String b = title22.getText().toString();

                if (isEmpty(a))
                {
                    title1.setError("Field cannot be empty");
                    focusView = title1;
                    cancel = true;
                }
                else
                {
                    title1.setError(null);
                }
                if (isEmpty(b))
                {
                    title2.setError("Field cannot be empty");
                    focusView = title2;
                    cancel = true;
                }
                else
                {
                    title2.setError(null);
                }
                if (cancel)
                {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    if (focusView != null)
                    {
                        focusView.requestFocus();
                    }
                }
                else
                {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    final String g = a+"@club-com";
                    final DatabaseReference users = root.child("admins");
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.child(g).exists()) {
                                Toast toast = Toast.makeText(TheAdminActivity.this, "This EmailID Already Exists",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                users.child(g).setValue(b);
                                Toast toast = Toast.makeText(TheAdminActivity.this, "Successfully Added!!",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;

                final String a = tit.getText().toString();
                final String b = tit22.getText().toString();

                if (isEmpty(a))
                {
                    tit1.setError("Field cannot be empty");
                    focusView = tit1;
                    cancel = true;
                }
                else
                {
                    tit1.setError(null);
                }
                if (isEmpty(b))
                {
                    tit2.setError("Field cannot be empty");
                    focusView = tit2;
                    cancel = true;
                }
                else
                {
                    tit2.setError(null);
                }
                if (cancel)
                {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    if (focusView != null)
                    {
                        focusView.requestFocus();
                    }
                }
                else
                {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    final String g = a+"@pat-com";
                    final DatabaseReference users = root.child("admins");
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.child(g).exists()) {
                                Toast toast = Toast.makeText(TheAdminActivity.this, "This EmailID Already Exists",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                users.child(g).setValue(b);
                                Toast toast = Toast.makeText(TheAdminActivity.this, "Successfully Added!!",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }
}
