package com.notadeveloper.app.partyyyadmin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import java.util.List;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static android.content.ContentValues.TAG;
import static com.notadeveloper.app.partyyyadmin.R.id.barcode;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link QrScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrScanFragment extends Fragment implements BarcodeRetriever {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public QrScanFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment QrScanFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static QrScanFragment newInstance(String param1, String param2) {
    QrScanFragment fragment = new QrScanFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
    BarcodeCapture barcodeCapture = (BarcodeCapture) getFragmentManager().findFragmentById(barcode);
    barcodeCapture.setRetrieval(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_qr_scan, container, false);
  }

  @Override public void onRetrieved(final Barcode barcode) {
    Log.d(TAG, "Barcode read: " + barcode.displayValue);

    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
        .setTitle("code retrieved")
        .setMessage(barcode.displayValue);
    builder.show();
  }

  @Override public void onRetrievedMultiple(Barcode barcode, List<BarcodeGraphic> list) {

  }

  @Override public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

  }

  @Override public void onRetrievedFailed(String s) {

  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */

}
