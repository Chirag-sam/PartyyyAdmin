package com.notadeveloper.app.patadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

public class LoginActivity extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  private static final int RC_SAVE = 1;
  private static final int RC_HINT = 2;
  private static final int RC_READ = 3;
  private static final String KEY_IS_RESOLVING = "is_resolving";
  private AutoCompleteTextView email;
  private AutoCompleteTextView password;
  private TextInputLayout email1;
  private TextInputLayout password1;
  private DatabaseReference ref;
  private FirebaseAuth mAuth;
  private Button proceed;
  private ProgressDialog mprogressDialog;
    private String s;
  private GoogleApiClient mCredentialsApiClient;
  private Credential mCurrentCredential;
  private boolean mIsResolving = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    if (savedInstanceState != null) {
      mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
    }
    mCredentialsApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .enableAutoManage(this, this)
        .addApi(Auth.CREDENTIALS_API)
        .build();
    mprogressDialog = new ProgressDialog(this);
    mprogressDialog.setMessage("Authenticating...");
    ref = FirebaseDatabase.getInstance().getReference();
    proceed = findViewById(R.id.proceed);
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
                        saveCredentialClicked();
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
                                        else if(u.getMyclub() == null)
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

  @Override public void onConnected(@Nullable Bundle bundle) {
    Log.d("LoginAct", "onConnected");

    // Request Credentials once connected. If credentials are retrieved
    // the user will either be automatically signed in or will be
    // presented with credential options to be used by the application
    // for sign in.

    Auth.CredentialsApi.disableAutoSignIn(mCredentialsApiClient);
    //requestCredentials();
  }

  @Override public void onConnectionSuspended(int i) {

  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  private void saveCredentialClicked() {
    String emailt = email.getText().toString();
    String passwordt = password.getText().toString();

    // Create a Credential with the user's email as the ID and storing the password.  We
    // could also add 'Name' and 'ProfilePictureURL' but that is outside the scope of this
    // minimal sample.
    if  (!emailt.isEmpty()||!passwordt.isEmpty()){
    final Credential credential = new Credential.Builder(emailt)
        .setPassword(passwordt)
        .build();

    showProgress();

    // NOTE: this method unconditionally saves the Credential built, even if all the fields
    // are blank or it is invalid in some other way.  In a real application you should contact
    // your app's back end and determine that the credential is valid before saving it to the
    // Credentials backend.
    showProgress();
    Auth.CredentialsApi.save(mCredentialsApiClient, credential).setResultCallback(
        new ResolvingResultCallbacks<Status>(this, RC_SAVE) {
          @Override
          public void onSuccess(@NonNull Status status) {
            showToast("Credential Saved");
            hideProgress();
          }

          @Override
          public void onUnresolvableFailure(@NonNull Status status) {
            Log.d("Credentials", "Save Failed:" + status);
            showToast("Credential Save Failed");
            hideProgress();
          }
        });}
  }

  private void requestCredentials() {
    // Request all of the user's saved username/password credentials.  We are not using
    // setAccountTypes so we will not load any credentials from other Identity Providers.
    proceed.setEnabled(false);
    CredentialRequest request = new CredentialRequest.Builder()
        .setPasswordLoginSupported(true)
        .setIdTokenRequested(true)
        .build();

    Auth.CredentialsApi.request(mCredentialsApiClient, request).setResultCallback(
        new ResultCallback<CredentialRequestResult>() {
          @Override
          public void onResult(@NonNull CredentialRequestResult credentialRequestResult) {
            hideProgress();
            Status status = credentialRequestResult.getStatus();
            if (status.isSuccess()) {
              // Successfully read the credential without any user interaction, this
              // means there was only a single credential and the user has auto
              // sign-in enabled.
              processRetrievedCredential(credentialRequestResult.getCredential());
            } else if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
              // This is most likely the case where the user has multiple saved
              // credentials and needs to pick one
              resolveResult(status, RC_READ);
            } else if (status.getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED) {
              // This means only a hint is available, but we are handling that
              // elsewhere so no need to act here.
              proceed.setEnabled(true);
            } else {
              Log.w("Request", "Unexpected status code: " + status.getStatusCode());
              proceed.setEnabled(true);
            }
          }
        });
  }

  private void resolveResult(Status status, int requestCode) {
    // We don't want to fire multiple resolutions at once since that can result
    // in stacked dialogs after rotation or another similar event.
    if (mIsResolving) {
      Log.w("Login", "resolveResult: already resolving.");
      return;
    }

    Log.d("Login", "Resolving: " + status);
    if (status.hasResolution()) {
      Log.d("Login", "STATUS: RESOLVING");
      try {
        status.startResolutionForResult(LoginActivity.this, requestCode);
        mIsResolving = true;
      } catch (IntentSender.SendIntentException e) {
        Log.e("Login", "STATUS: Failed to send resolution.", e);
        hideProgress();
      }
    } else {
      Log.e("Login", "STATUS: FAIL");
      showToast("Could Not Resolve Error");
      hideProgress();
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (!mIsResolving) {
      // Attempt auto-sign in.
      requestCredentials();
    }
  }

  private void showProgress() {
    if (mprogressDialog == null) {
      mprogressDialog = new ProgressDialog(this);
      mprogressDialog.setIndeterminate(true);
      mprogressDialog.setMessage("Loading...");
    }

    mprogressDialog.show();
  }

  private void hideProgress() {
    if (mprogressDialog != null && mprogressDialog.isShowing()) {
      mprogressDialog.dismiss();
    }
  }

  private void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d("LoginAct", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
    hideProgress();

    switch (requestCode) {
      case RC_HINT:
        // Drop into handling for RC_READ
      case RC_READ:
        if (resultCode == RESULT_OK) {
          Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
          processRetrievedCredential(credential);
        } else {
          Log.e("LoginAct", "Credential Read: NOT OK");
          showToast("Credential Read Failed");
          proceed.setEnabled(true);
        }

        mIsResolving = false;
        break;
      case RC_SAVE:
        if (resultCode == RESULT_OK) {
          Log.d("LoginAct", "Credential Save: OK");
          showToast("Credential Save Success");
        } else {
          Log.e("LoginAct", "Credential Save: NOT OK");
          showToast("Credential Save Failed");
          proceed.setEnabled(true);
        }

        mIsResolving = false;
        break;
    }
  }

  private void processRetrievedCredential(Credential credential) {
    Log.d("Login", "Credential Retrieved: " + credential.getId() + ":");

    // If the Credential is not a hint, we should store it an enable the delete button.
    // If it is a hint, skip this because a hint cannot be deleted.

    showToast("Credential Retrieved");
    mCurrentCredential = credential;
    validateadmin(mCurrentCredential.getId(), mCurrentCredential.getPassword());
  }
}
