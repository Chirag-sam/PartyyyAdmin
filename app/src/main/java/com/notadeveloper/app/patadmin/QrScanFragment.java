package com.notadeveloper.app.patadmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link QrScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrScanFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  @BindView(R.id.scanbutton) Button scanbutton;
  @BindView(R.id.camera_view) SurfaceView cameraView;
  Unbinder unbinder;
  private QREader qrEader;
  // TODO: Rename and change types of parameters
  private String mParam1;

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
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    qrEader = new QREader.Builder(getContext(), cameraView, new QRDataListener() {
      @Override
      public void onDetected(final String data) {
        Log.d("QREader", "Value : " + data);
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            // Your dialog code.
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("code retrieved")
                    .setMessage(data);
            builder.show();
            //To Stop Showing endless Dialogs
            scanbutton.setText("Start QREader");
            qrEader.stop();
          }
        });
      }
    }).facing(QREader.BACK_CAM)
            .enableAutofocus(true)
            .height(cameraView.getHeight())
            .width(cameraView.getWidth())
            .build();
    //This Does Not Work!
    //BarcodeCapture barcodeCapture = (BarcodeCapture) getFragmentManager().findFragmentById(barcode);
    //barcodeCapture.setRetrieval(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_qr_scan, container, false);
    unbinder = ButterKnife.bind(this, view);

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void onPause() {
    super.onPause();
    qrEader.releaseAndCleanup();
  }

  @Override public void onResume() {
    super.onResume();
    qrEader.initAndStart(cameraView);
  }

  @OnClick(R.id.scanbutton) public void onViewClicked() {
    if (qrEader.isCameraRunning()) {
      scanbutton.setText("Start QREader");
      qrEader.stop();
    } else {
      scanbutton.setText("Stop QREader");
      qrEader.start();
    }
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
