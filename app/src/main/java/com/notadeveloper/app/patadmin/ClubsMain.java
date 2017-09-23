package com.notadeveloper.app.patadmin;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class ClubsMain extends AppCompatActivity {

  static final int IMAGE_CAPTURE = 101;
  private static final int REQUEST_CODE_CHOOSE = 420;
  private static final int REQUEST_CODE_CHOOSE_MENU = 666;
  private static final String TAG = ClubsMain.class.getSimpleName();
  private static final int IMAGE_CAPTURE_2 = 102;
  private final int PLACE_PICKER_REQUEST = 1010;
  @BindView(R.id.picture) ImageButton picture;
  @BindView(R.id.recyclerview) RecyclerView recyclerview;
  @BindView(R.id.camerabutton) Button camerabutton;
  @BindView(R.id.timet) TextView timet;
  @BindView(R.id.to) TextView to;
  @BindView(R.id.recyclerview1) RecyclerView recyclerview1;
  @BindView(R.id.addmenuimages) Button addmenuimages;
  @BindView(R.id.menucamerabutton) Button menucamerabutton;
  @BindView(R.id.showfacilities) TextView showfacilities;
  @BindView(R.id.select_facilities) Button selectFacilities;
  private String locationurl;
  final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
  Users u = new Users();
  @BindView(R.id.clubname) AutoCompleteTextView mClubname;
  @BindView(R.id.clubname1) TextInputLayout mClubname1;
  @BindView(R.id.email) AutoCompleteTextView mEmail;
  @BindView(R.id.emaila) TextInputLayout mEmaila;
  @BindView(R.id.number) AutoCompleteTextView mNumber;
  @BindView(R.id.number1) TextInputLayout mNumber1;
  @BindView(R.id.text) TextView mText;
  @BindView(R.id.confirm) Button mConfirm;
  @BindView(R.id.time) TextView time;
  @BindView(R.id.activity_signup) LinearLayout activitySignup;
  @BindView(R.id.time1) TextView time1;
  @BindView(R.id.location) Button location;
  @BindView(R.id.address1) EditText address1;
  @BindView(R.id.add1lt) TextInputLayout add1lt;
  @BindView(R.id.address2) AutoCompleteTextView address2;
  @BindView(R.id.add2lt) TextInputLayout add2lt;
  @BindView(R.id.imageView2) ImageView imageView2;
  @BindView(R.id.address3) AutoCompleteTextView address3;
  @BindView(R.id.add3lt) TextInputLayout add3lt;
  @BindView(R.id.imageView1) ImageView imageView1;
  @BindView(R.id.pincode) EditText pincode;
  @BindView(R.id.pinlt) TextInputLayout pinlt;
  long esttime;
  @BindView(R.id.addimages) Button addimages;
  @BindView(R.id.progressbar) ProgressBar mprogressbar;
  private String st;
  private int index = 0;
  private int index2 = 0;
  private long estimatedServerTimeMs;
  private FirebaseStorage storage;
  private DatabaseReference ref;
  private String mCurrentPhotoPath;
  private StorageReference storageRef;
  private StorageReference imagesRef;
  private String photoUrl;
  private Uri mImageUri;
  private ArrayList<Uri> mArrayUri;
  private ArrayList<Uri> mArrayUriMenu;
  private ArrayList<String> mSelectedItems = new ArrayList<>();
  private String[] facilities;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_clubs_main);
    ButterKnife.bind(this);
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());
    mArrayUri = new ArrayList<>();
    mArrayUriMenu = new ArrayList<>();
    facilities=getResources().getStringArray(R.array.available_facilities);
    getUser();

    MultiplePermissionsListener dialogPermissionListener =
        DialogOnAnyDeniedMultiplePermissionsListener.Builder.withContext(this)
            .withTitle("Camera & Storage permission")
            .withMessage("Camera & Storage permission is for uploading images!")
            .withButtonText(android.R.string.ok)
            .withIcon(R.mipmap.ic_launcher)
            .build();
    Dexter.withActivity(this)
        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(dialogPermissionListener)
        .check();
    selectFacilities.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        mSelectedItems.clear();
        AlertDialog.Builder facilitieslistdialog = new AlertDialog.Builder(ClubsMain.this);
        // Set the dialog title
        facilitieslistdialog.setTitle("Select Available Facilities")
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
            .setMultiChoiceItems(facilities, null, new DialogInterface.OnMultiChoiceClickListener() {
              @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                  // If the user checked the item, add it to the selected items
                  mSelectedItems.add(facilities[which]);
                } else if (mSelectedItems.contains(facilities[which])) {
                  // Else, if the item is already in the array, remove it
                  mSelectedItems.remove(facilities[which]);
                }
              }
            })
            // Set the action buttons
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                if (mSelectedItems != null && mSelectedItems.size() == 0) {
                  Toast.makeText(ClubsMain.this, "Select Atleast One Club Facility!",
                      Toast.LENGTH_SHORT).show();
                } else {
                  if (mSelectedItems != null)
                  showfacilities.setText(TextUtils.join(",", mSelectedItems));
                  dialog.dismiss();
                }
              }
            });
        facilitieslistdialog.show();
      }
    });


    time.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker =
            new TimePickerDialog(ClubsMain.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time.setText(String.format(Locale.UK, "%02d:%02d", selectedHour, selectedMinute));
              }
            }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
      }
    });
    time1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker =
            new TimePickerDialog(ClubsMain.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time1.setText(String.format(Locale.UK, "%02d:%02d", selectedHour, selectedMinute));
              }
            }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
      }
    });
    storage = FirebaseStorage.getInstance();
    ref = FirebaseDatabase.getInstance().getReference();
    addimages.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE);
      }
    });
    camerabutton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        saveImage(IMAGE_CAPTURE);
      }
    });

    addmenuimages.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mArrayUri != null && mArrayUri.size() != 0) {
          Intent intent = new Intent();
          intent.setType("image/*");
          intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
          intent.setAction(Intent.ACTION_GET_CONTENT);
          startActivityForResult(Intent.createChooser(intent, "Select Picture"),
              REQUEST_CODE_CHOOSE_MENU);
        } else {
          Toast.makeText(ClubsMain.this, "Add Club Pictures First before uploading Menu",
              Toast.LENGTH_SHORT).show();
        }
      }
    });
    menucamerabutton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        saveImage(IMAGE_CAPTURE_2);
      }
    });

    storage = FirebaseStorage.getInstance();
    storageRef = storage.getReference();
    ref = FirebaseDatabase.getInstance().getReference();

    new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/MikeLau/Pictures",
        Environment.getExternalStorageDirectory().getPath());
    CroperinoFileUtil.setupDirectory(ClubsMain.this);

    picture.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mUser.getUid();

        getEstimatedServerTimeMs();
        imagesRef = storageRef.child("clubspictures").child(uid);

        if (CroperinoFileUtil.verifyStoragePermissions(ClubsMain.this)) {
          prepareChooser();
        }
      }
    });
  }

  private void saveImage(int code) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      //Create the File where the photo should go
      File photoFile = null;
      try {
        photoFile = createImageFile();
      } catch (IOException ex) {
        // Error occurred while creating the File
        ex.printStackTrace();
      }
      // Continue only if the File was successfully created
      if (photoFile != null) {
        Uri photoURI = Uri.fromFile(photoFile);
        Log.i(TAG, "Image saved at " + photoURI);
        mImageUri = photoURI;
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, code);
      }
    }
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */);

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    Log.i(TAG, "image path: " + mCurrentPhotoPath);
    return image;
  }

  @OnClick(R.id.location) public void onClickloc() {
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    try {
      startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
      e.printStackTrace();
    }
  }

  @OnClick(R.id.confirm) public void onClickcon() {

    boolean cancel = false;
    View focusView = null;

    final String a = mClubname.getText().toString();
    final String c = time.getText().toString();
    final String d = time1.getText().toString();
    final String e = mEmail.getText().toString();
    final String f = mNumber.getText().toString();
    final String g = address1.getText().toString();
    final String h = address2.getText().toString();
    final String i = address3.getText().toString();
    final String j = pincode.getText().toString();
    final String l = mText.getText().toString();


    if (isEmpty(a)) {
      mClubname.setError("Field cannot be empty");
      focusView = mClubname;
      cancel = true;
    } else {
      mClubname.setError(null);
    }
    if (c.equals("MM:HH")) {

      Snackbar.make(findViewById(android.R.id.content), "Please fill from time first",
          Snackbar.LENGTH_SHORT)
          // .setActionTextColor(ContextCompat.getColor(ClubsMain.this, R.color.tw__composer_red))

          .show();
      focusView = null;
      cancel = true;
    }
        /*if (d.equals("MM:HH"))
        {

            Snackbar.make(findViewById(android.R.id.content), "Please fill to time first", Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.mdtp_red))
                    .show();
            focusView = null;
            cancel = true;
        }*/
    if (isEmpty(e)) {
      mEmaila.setError("Field cannot be empty");
      focusView = mEmaila;
      cancel = true;
    } else {
      mEmaila.setError(null);
    }
    if (isEmpty(f)) {
      mNumber1.setError("Field cannot be empty");
      focusView = mNumber1;
      cancel = true;
    } else {
      mNumber1.setError(null);
    }
    if (isEmpty(g)) {
      add1lt.setError("Field cannot be empty");
      focusView = add1lt;
      cancel = true;
    } else {
      add1lt.setError(null);
    }
    if (isEmpty(h)) {
      add2lt.setError("Field cannot be empty");
      focusView = add2lt;
      cancel = true;
    } else {
      add2lt.setError(null);
    }
    if (isEmpty(i)) {
      add3lt.setError("Field cannot be empty");
      focusView = add3lt;
      cancel = true;
    } else {
      add3lt.setError(null);
    }
    if (isEmpty(j)) {
      pinlt.setError("Field cannot be empty");
      focusView = pinlt;
      cancel = true;
    } else {
      pinlt.setError(null);
    }

    if (l.equals("Enter description here...")) {
      Snackbar.make(findViewById(android.R.id.content), "Please give a description",
          Snackbar.LENGTH_SHORT)

          .show();
      focusView = null;
      cancel = true;
    }
    if (!isValidPhone(f)) {
      mNumber1.setError("Invalid Phone");
      focusView = mNumber1;
      cancel = true;
    } else {
      mNumber1.setError(null);
    }
    if (!isValidEmail(e)) {
      mEmaila.setError("Invalid Email");
      focusView = mEmaila;
      cancel = true;
    } else {
      mEmaila.setError(null);
    }
    if (photoUrl == null || photoUrl.isEmpty()) {
      Toast.makeText(this, "Main club picture not uploaded!!", Toast.LENGTH_SHORT).show();
      cancel = true;
    } else {

    }
    if (mArrayUri != null && mArrayUri.size() != 0) {

    } else {
      Toast.makeText(this, "No Images Selected", Toast.LENGTH_SHORT).show();
      cancel = true;
    }
    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      if (focusView != null) {
        focusView.requestFocus();
      }
    } else {

      final Calendar cal = Calendar.getInstance();
      showprogressbar();
      DatabaseReference offsetRef =
          FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
      offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot snapshot) {
          long offset = snapshot.getValue(Long.class);
          if (u != null && u.getMyclub() != null) {
            estimatedServerTimeMs = Long.parseLong(u.getMyclub().getClubid());
          } else {
            estimatedServerTimeMs = (System.currentTimeMillis() + offset);
          }
          cal.setTimeInMillis(estimatedServerTimeMs);
          SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
          final DatabaseReference mDatabase =
              ref.child("clubs").child(String.valueOf(estimatedServerTimeMs));
          final ArrayList<Uri> tempUri = new ArrayList<Uri>();
          final ArrayList<String> clublist = new ArrayList<>();
          final ArrayList<String> menulist = new ArrayList<>();
          for (Uri pic : mArrayUri) {
            if (pic.toString().startsWith("https://")) {
              clublist.add(pic.toString());
            } else {
              tempUri.add(pic);
            }
          }
          index2 = tempUri.size();
          for (Uri pic : mArrayUriMenu) {
            if (pic.toString().startsWith("https://")) {
              menulist.add(pic.toString());
            } else {
              tempUri.add(pic);
            }
          }

          Log.e(TAG, "onDataChange: " + index2);
          final ArrayList<String> imagesUri = new ArrayList<String>();
          if (tempUri.size() != 0) {

            for (index = 0; index < tempUri.size(); index++) {

              StorageReference storageRef = storage.getReference()
                  .child("clubs")
                  .child(String.valueOf(estimatedServerTimeMs))
                  .child(String.valueOf(index));
              UploadTask uploadTask = storageRef.putFile(tempUri.get(index));

              // Register observers to listen for when the download is done or if it fails
              uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override public void onFailure(@NonNull Exception exception) {
                  hideprogressbar();
                  Snackbar.make(findViewById(android.R.id.content), "Upload Failed Try Again",
                      Snackbar.LENGTH_LONG);
                  // Handle unsuccessful uploads
                }
              }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                  if (downloadUrl != null) {
                    imagesUri.add(downloadUrl.toString());
                  }

                  if (index == tempUri.size() && imagesUri.size() == tempUri.size()) {
                    Log.e(TAG, "onSuccess:\n " + imagesUri.toString() + "\n" + tempUri.toString());

                    clublist.addAll(imagesUri.subList(0, index2));

                    menulist.addAll(imagesUri.subList(index2, imagesUri.size()));

                    Club cl =
                        new Club(String.valueOf(estimatedServerTimeMs), photoUrl, clublist, a, c, d,
                            e, f, g, h, i, j, l, menulist, mSelectedItems, locationurl);
                    mDatabase.setValue(cl);
                    ref.child("users").child(uid).child("myclub").setValue(cl);
                    hideprogressbar();

                    Toast.makeText(ClubsMain.this, "Redirect to payment gateway", Toast.LENGTH_LONG)
                        .show();
                    //  DatabaseReference df = ref.child("users").child(uid).child("myclub").child("clubid");
                    //  df.addListenerForSingleValueEvent(new ValueEventListener() {
                    //      @Override
                    //      public void onDataChange(DataSnapshot dataSnapshot) {
                    //          st = dataSnapshot.getValue(String.class);
                    //      }
                    //
                    //      @Override
                    //      public void onCancelled(DatabaseError databaseError) {
                    //
                    //      }
                    //  });
                    //Intent myIntent =
                    //        new Intent(ClubsMain.this, EditDetailedClubActivity.class);
                    //  myIntent.putExtra("Club_id", st);
                    //startActivity(myIntent);
                    //finish();
                  }
                }
              });
            }
          } else {
            Club cl =
                new Club(String.valueOf(estimatedServerTimeMs), photoUrl, clublist, a, c, d, e, f,
                    g, h, i, j, l, menulist, mSelectedItems, locationurl);
            mDatabase.setValue(cl);
            ref.child("users").child(uid).child("myclub").setValue(cl);
            hideprogressbar();

            Toast.makeText(ClubsMain.this, "Redirect to payment gateway", Toast.LENGTH_LONG).show();
          }
        }

        @Override public void onCancelled(DatabaseError error) {
          hideprogressbar();
          System.err.println("Listener was cancelled");
        }
      });
    }
  }

  private boolean isValidEmail(String email) {

    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return !isEmpty(email) && pattern.matcher(email).matches();
  }

  private boolean isValidPhone(CharSequence target) {
    return !isEmpty(target) && Patterns.PHONE.matcher(target).matches() && target.length() == 10;
  }

  private void getEstimatedServerTimeMs() {

    DatabaseReference offsetRef =
        FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
    offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot snapshot) {
        long offset = snapshot.getValue(Long.class);
        esttime = System.currentTimeMillis() + offset;
      }

      @Override public void onCancelled(DatabaseError error) {
        System.err.println("Listener was cancelled");
      }
    });
  }

  void getUser() {
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = mUser.getUid();
    final DatabaseReference mDatabase =
        FirebaseDatabase.getInstance().getReference().child("users").child(uid);

    mDatabase.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        u = dataSnapshot.getValue(Users.class);
        if (u != null && u.getMyclub() != null) {
          mArrayUri.clear();
          mArrayUriMenu.clear();
          Club c = u.getMyclub();
          mClubname.setText(c.getClubname());
          time.setText(c.getTime());
          time1.setText(c.getTime1());
          mEmail.setText(c.getEmail());
          mNumber.setText(c.getNumber());
          address1.setText(c.getAddress1());
          address2.setText(c.getAddress2());
          address3.setText(c.getAddress3());
          pincode.setText(c.getPin());
          mText.setText(c.getDescription());
          if (c.getUtils()!=null)
            mSelectedItems=c.getUtils();
          if (c.getPicture() != null) GlideApp.with(ClubsMain.this).load(c.getPicture()).into(picture);
          if (c.getClubpicture() != null) {
            for (String pic : c.getClubpicture())
              mArrayUri.add(Uri.parse(pic));
            RecyclerView.Adapter adapter = new MultipleImagesAdapter(mArrayUri, ClubsMain.this);
            recyclerview.setAdapter(adapter);
          }
          if (c.getMenupicture() != null) {
            for (String pic : c.getMenupicture())
              mArrayUriMenu.add(Uri.parse(pic));
            RecyclerView.Adapter adapter2 =
                new MultipleImagesAdapter(mArrayUriMenu, ClubsMain.this);
            recyclerview1.setAdapter(adapter2);
          }

          if (c.getUtils() != null) {
            ArrayList<String> ar = c.getUtils();
            showfacilities.setText(TextUtils.join(",", ar));//;ar.toString());
          }
          mConfirm.setText("Update Details");
        }
      }

      @Override public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
      Place place = PlacePicker.getPlace(this, data);
      locationurl = String.format("https://www.google.com/maps/search/?api=1&query=%s,%s&query_place_id=%s",
          place.getLatLng().latitude, place.getLatLng().longitude,place.getId());
      Log.e("loc", "onActivityResult: " + location);
    }
    RecyclerView.Adapter adapter;
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
      if (data.getClipData() != null) {
        ClipData mClipData = data.getClipData();
        for (int i = 0; i < mClipData.getItemCount(); i++) {
          ClipData.Item item = mClipData.getItemAt(i);
          Uri uri = item.getUri();
          mArrayUri.add(uri);
        }
        adapter = new MultipleImagesAdapter(mArrayUri, this);
        recyclerview.setAdapter(adapter);
        Log.e(TAG, "onActivityResult: " + mArrayUri.toString());
      }
    }
    RecyclerView.Adapter menuadapter;
    if (requestCode == REQUEST_CODE_CHOOSE_MENU && resultCode == RESULT_OK) {
      if (data.getClipData() != null) {
        ClipData mClipData = data.getClipData();
        for (int i = 0; i < mClipData.getItemCount(); i++) {
          ClipData.Item item = mClipData.getItemAt(i);
          Uri uri = item.getUri();
          mArrayUriMenu.add(uri);
        }
        menuadapter = new MultipleImagesAdapter(mArrayUriMenu, this);
        recyclerview1.setAdapter(menuadapter);
        Log.e(TAG, "onActivityResult: " + mArrayUriMenu.toString());
      }
    }
    if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {

      mArrayUri.add(mImageUri);
      adapter = new MultipleImagesAdapter(mArrayUri, this);
      recyclerview.setAdapter(adapter);
      Log.e(TAG, "onActivityResult: " + mImageUri + mArrayUri.toString());
    }
    if (requestCode == IMAGE_CAPTURE_2 && resultCode == RESULT_OK) {

      mArrayUriMenu.add(mImageUri);
      menuadapter = new MultipleImagesAdapter(mArrayUriMenu, this);
      recyclerview1.setAdapter(menuadapter);
      Log.e(TAG, "onActivityResult: " + mImageUri + mArrayUriMenu.toString());
    }
    switch (requestCode) {
      case CroperinoConfig.REQUEST_TAKE_PHOTO:
        if (resultCode == Activity.RESULT_OK) {
          Croperino.runCropImage(CroperinoFileUtil.getmFileTemp(), ClubsMain.this, true, 16, 9, 0,
              0);
        }
        break;
      case CroperinoConfig.REQUEST_PICK_FILE:
        if (resultCode == Activity.RESULT_OK) {
          CroperinoFileUtil.newGalleryFile(data, ClubsMain.this);
          Croperino.runCropImage(CroperinoFileUtil.getmFileTemp(), ClubsMain.this, true, 16, 9, 0,
              0);
        }
        break;
      case CroperinoConfig.REQUEST_CROP_PHOTO:
        if (resultCode == Activity.RESULT_OK) {
          Uri i = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
          Glide.with(this).load(i).into(picture);
          UploadTask uploadTask = imagesRef.putFile(i);

          // Register observers to listen for when the download is done or if it fails
          uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
              // Handle unsuccessful uploads
            }
          }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
              Uri downloadUrl = taskSnapshot.getDownloadUrl();
              photoUrl = downloadUrl.toString();
            }
          });
          //Do saving / uploading of photo method here.
        }
        break;
      default:
        break;
    }
  }

  void showprogressbar() {
    if (mprogressbar != null) {
      mprogressbar.setVisibility(View.VISIBLE);
      mprogressbar.setIndeterminate(true);
      activitySignup.setVisibility(View.GONE);
    }
  }

  void hideprogressbar() {
    if (mprogressbar != null) {
      mprogressbar.setVisibility(View.GONE);
      mprogressbar.setIndeterminate(false);
      activitySignup.setVisibility(View.VISIBLE);
    }
  }

  @Override public void onBackPressed() {

    final AlertDialog.Builder builder = new AlertDialog.Builder(ClubsMain.this, R.style.pop);
    builder.setMessage("Are You Sure you want to exit?");
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialogInterface, int i) {
        FirebaseAuth.getInstance().signOut();
        Process.killProcess(Process.myPid());
        System.exit(1);
      }
    });
    builder.setNegativeButton("No", null);
    builder.show();
    //  super.onBackPressed();
  }

  private void prepareChooser() {
    Croperino.prepareChooser(ClubsMain.this, "Change Picture",
        ContextCompat.getColor(ClubsMain.this, android.R.color.background_dark));
  }
}
