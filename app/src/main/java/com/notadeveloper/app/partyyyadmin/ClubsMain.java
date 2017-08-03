package com.notadeveloper.app.partyyyadmin;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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
  private static final String TAG = ClubsMain.class.getSimpleName();
  static String datetxt;
  final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
  Users u = new Users();
  @BindView(R.id.clubname)
  AutoCompleteTextView mClubname;
  @BindView(R.id.clubname1)
  TextInputLayout mClubname1;
  @BindView(R.id.email)
  AutoCompleteTextView mEmail;
  @BindView(R.id.emaila)
  TextInputLayout mEmaila;
  @BindView(R.id.number)
  AutoCompleteTextView mNumber;
  @BindView(R.id.number1)
  TextInputLayout mNumber1;
  @BindView(R.id.text)
  TextView mText;
  @BindView(R.id.confirm)
  Button mConfirm;
  @BindView(R.id.time)
  TextView time;
  @BindView(R.id.activity_signup)
  LinearLayout activitySignup;
  @BindView(R.id.time1)
  TextView time1;
  @BindView(R.id.location)
  Button location;
  @BindView(R.id.address1)
  EditText address1;
  @BindView(R.id.add1lt)
  TextInputLayout add1lt;
  @BindView(R.id.address2)
  AutoCompleteTextView address2;
  @BindView(R.id.add2lt)
  TextInputLayout add2lt;
  @BindView(R.id.imageView2)
  ImageView imageView2;
  @BindView(R.id.address3)
  AutoCompleteTextView address3;
  @BindView(R.id.add3lt)
  TextInputLayout add3lt;
  @BindView(R.id.imageView1)
  ImageView imageView1;
  @BindView(R.id.pincode)
  EditText pincode;
  @BindView(R.id.pinlt)
  TextInputLayout pinlt;
  int index = 0;
  long esttime;
  @BindView(R.id.addimages)
  Button addimages;
  @BindView(R.id.progressbar) ProgressBar mprogressbar;
  private long estimatedServerTimeMs;
  private String photoUrl;
  private FirebaseStorage storage;
  private StorageReference storageRef;
  private StorageReference imagesRef;
  private DatabaseReference ref;
  private FirebaseAuth mAuth;
  private FirebaseUser mUser;
  private TextInputLayout clubname1;
  private AutoCompleteTextView clubname;
  private CheckBox parking;
  private CheckBox swimming;
  private RecyclerView recyclerView;
  private RecyclerView.Adapter adapter;
  private RecyclerView recyclerViewmenu;
  private RecyclerView.Adapter menuadapter;
  private Button camerabutton;
  private Button menucamerabutton;
  private Button addmenuimages;
  private String mCurrentPhotoPath;
  private Uri mImageUri;
  private ArrayList<String> pathList;
  private ArrayList<String> temp;
  private ArrayList<Uri> mArrayUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_clubs_main);
      mArrayUri = new ArrayList<Uri>();
    MultiplePermissionsListener dialogPermissionListener =
        DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(this)
            .withTitle("Camera & Storage permission")
            .withMessage("Camera & Storage permission is for uploading images!")
            .withButtonText(android.R.string.ok)
            .withIcon(R.mipmap.ic_launcher)
            .build();
    Dexter.withActivity(this)
        .withPermissions(Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(dialogPermissionListener).check();
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());

    ButterKnife.bind(this);
    getUser();

    pathList = new ArrayList<>();
    temp = new ArrayList<>();

    clubname1 = (TextInputLayout) findViewById(R.id.clubname1);
    clubname = (AutoCompleteTextView) findViewById(R.id.clubname);
    parking = (CheckBox) findViewById(R.id.parking);
    swimming = (CheckBox) findViewById(R.id.swimming);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    camerabutton = (Button) findViewById(R.id.camerabutton);

    recyclerViewmenu = (RecyclerView) findViewById(R.id.recyclerview1);
    recyclerViewmenu.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    menucamerabutton = (Button) findViewById(R.id.menucamerabutton);
    addmenuimages = (Button) findViewById(R.id.addmenuimages);

    time.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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
      @Override
      public void onClick(View v) {
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
    storageRef = storage.getReferenceFromUrl("gs://partyyy-5e773.appspot.com");
    ref = FirebaseDatabase.getInstance().getReference();

    new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/MikeLau/Pictures",
        Environment.getExternalStorageDirectory().getPath());
    CroperinoFileUtil.setupDirectory(ClubsMain.this);

    //        mProfile.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //
    //                imagesRef = storageRef.child("images").child("parties");
    //
    //                getEstimatedServerTimeMs();
    //
    //                multiImagePicker();
    //
    //
    //            }
    //        });
    addimages.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        multiImagePicker();
      }
    });
    camerabutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveImage();
      }
    });

    addmenuimages.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        multiImagePicker();
      }
    });
    menucamerabutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveImage();
      }
    });
  }

  private void saveImage() {
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
                        Log.i(TAG, "Image saved at "+ photoURI);
                        mImageUri = photoURI;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
                    }
              }
        }
     private File createImageFile() throws IOException {
           // Create an image file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
           File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                                    imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                            );

                    // Save a file: path for use with ACTION_VIEW intents
                            mCurrentPhotoPath = image.getAbsolutePath();
            Log.i(TAG, "image path: "+mCurrentPhotoPath);
            return image;
        }

  @OnClick(R.id.location)
  public void onClickloc() {

  }

  @OnClick(R.id.confirm)
  public void onClickcon() {

    boolean cancel = false;
    View focusView = null;

    final String a = clubname.getText().toString();
    final String c = time.getText().toString();
    final String d = time1.getText().toString();
    final String e = mEmail.getText().toString();
    final String f = mNumber.getText().toString();
    final String g = address1.getText().toString();
    final String h = address2.getText().toString();
    final String i = address3.getText().toString();
    final String j = pincode.getText().toString();
    final String l = mText.getText().toString();
    final ArrayList<String> utils = new ArrayList<>();

    if (parking.isChecked()) {
      utils.add(parking.getText().toString());
    }
    if (swimming.isChecked()) {
      utils.add(swimming.getText().toString());
    }

    if (isEmpty(a)) {
      clubname1.setError("Field cannot be empty");
      focusView = clubname1;
      cancel = true;
    } else {
      clubname1.setError(null);
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

      final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
      final String nam = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
      final Calendar cal = Calendar.getInstance();
      mprogressbar.setIndeterminate(true);
      DatabaseReference offsetRef =
          FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
      offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
          long offset = snapshot.getValue(Long.class);
          estimatedServerTimeMs = (System.currentTimeMillis() + offset);
          cal.setTimeInMillis(estimatedServerTimeMs);
          SimpleDateFormat formatter =
              new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
          String date = formatter.format(new Date(cal.getTimeInMillis()));
          final DatabaseReference mDatabase =
              ref.child("clubs").child(String.valueOf(estimatedServerTimeMs));

          final ArrayList<String> imagesUri = new ArrayList<String>();
          for (index = 0; index < mArrayUri.size(); index++) {

            StorageReference storageRef = storage.getReference()
                .child("clubs")
                .child(String.valueOf(estimatedServerTimeMs))
                .child(String.valueOf(index));
            UploadTask uploadTask = storageRef.putFile(mArrayUri.get(index));

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception exception) {
                Snackbar.make(findViewById(android.R.id.content), "Upload Failed Try Again",
                    Snackbar.LENGTH_LONG);
                // Handle unsuccessful uploads
              }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
              @Override public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =
                    100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                mprogressbar.setIndeterminate(false);
                mprogressbar.setProgress(currentprogress);
              }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) {
                  imagesUri.add(downloadUrl.toString());
                }

                if (index == mArrayUri.size()) {
                  Log.e(TAG, "onSuccess: " + imagesUri.toString());
                  Club cl = new Club(imagesUri, a, c, d, e, f, g, h, i, j, l, null, utils);
                  mDatabase.setValue(cl);
                  ref.child("users").child(uid).child("myclub").setValue(cl);
                  mprogressbar.setProgress(100);
                  Toast.makeText(ClubsMain.this, "Redirect to payment gateway",
                      Toast.LENGTH_LONG).show();
                }
              }
            });
          }
        }

        @Override
        public void onCancelled(DatabaseError error) {
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
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        long offset = snapshot.getValue(Long.class);
        esttime = System.currentTimeMillis() + offset;
        imagesRef = storageRef.child("images").child("parties").child(String.valueOf(esttime));
      }

      @Override
      public void onCancelled(DatabaseError error) {
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
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        u = dataSnapshot.getValue(Users.class);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  private void multiImagePicker() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
      if (data.getClipData() != null) {
        ClipData mClipData = data.getClipData();

        for (int i = 0; i < mClipData.getItemCount(); i++) {
          ClipData.Item item = mClipData.getItemAt(i);
          Uri uri = item.getUri();
          mArrayUri.add(uri);
        }
        adapter = new MultipleImagesAdapter(mArrayUri, this);
        recyclerView.setAdapter(adapter);
        Log.e(TAG, "onActivityResult: " + mArrayUri.toString());
      }
    }
    if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {
      if (data.getClipData() != null) {
        ClipData mClipData = data.getClipData();
        mArrayUri = new ArrayList<Uri>();
        for (int i = 0; i < mClipData.getItemCount(); i++) {
          ClipData.Item item = mClipData.getItemAt(i);
          Uri uri = item.getUri();
          mArrayUri.add(uri);
        }
        Log.e(TAG, "onActivityResult: " + mArrayUri.toString());
      }
    }
    if(requestCode == IMAGE_CAPTURE && data!=null)
    {
        mArrayUri.add(Uri.parse(mCurrentPhotoPath));
        
    }
  }
}
