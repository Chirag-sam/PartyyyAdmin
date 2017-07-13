package com.example.chirag.partyyyadmin;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {

    private Button proceed;
    private AutoCompleteTextView email;
    private AutoCompleteTextView password;
    private TextInputLayout email1;
    private TextInputLayout password1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        proceed = (Button)findViewById(R.id.proceed);
        email = (AutoCompleteTextView)findViewById(R.id.email);
        password = (AutoCompleteTextView)findViewById(R.id.password);
        email1 = (TextInputLayout)findViewById(R.id.email1);
        password1 = (TextInputLayout)findViewById(R.id.password1);
        final admins ad = new admins(null);


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
                } else email1.setError(null);

                if (isEmpty(b)) {
                    password1.setError("Field cannot be empty");
                    focusView = password1;
                    cancel = true;
                } else password1.setError(null);

                HashMap<String,String> k=ad.getAdmins();
                if (k!=null)
                {
                    if (!k.containsKey(a))
                    {
                        cancel = false;
                    }
                    else
                    {
                        cancel = true;
                        focusView = null;
                        Toast.makeText(LoginActivity.this,"Invalid Email ID",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    cancel = false;
                }

                if (cancel)
                {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    if (focusView != null)
                        focusView.requestFocus();
                }
                else
                {

                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();


                }
            }
        });
    }
}
