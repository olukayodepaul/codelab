package com.mobbile.paul.shrine.fragments;


import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.helper.UploadHelper;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.listeners.onSaveTappedListener;
import com.mobbile.paul.shrine.models.BrandingRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandingFragment extends Fragment {


    ArrayList<String> spinnerlist;

    ArrayAdapter spinneradapter;

    View view;

    onFragmentActivatedListener onFragmentActivatedListener;

    TextInputEditText contentEditText, fontEditText, colorEditText, dimensionEditText, amountCopiesEditText, deliveryEditText;

    MaterialCardView uploadCardView;

    TextView uploadTextView;

    private static final int SELECT_PHOTO = 100;


    private int type = 0; //0 for Online, 1 for Through the line;


    String logoUrl = "";

    onSaveTappedListener onSaveTappedListener;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean checkPermissions() {

        if (getContext() == null)
            return false;

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            if (getActivity() != null)
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_branding, container, false);

        onFragmentActivatedListener.onFragmentEntered();

        Spinner s = (Spinner) view.findViewById(R.id.spin);


        dimensionEditText = view.findViewById(R.id.dimensionEditText);
        fontEditText = view.findViewById(R.id.fontEditText);
        colorEditText = view.findViewById(R.id.colorEditText);
        contentEditText = view.findViewById(R.id.contentEditText);

        uploadCardView = view.findViewById(R.id.uploadCardView);
        uploadTextView = view.findViewById(R.id.uploadTextView);

        amountCopiesEditText = view.findViewById(R.id.amountCopiesEditText);

        deliveryEditText = view.findViewById(R.id.deliveryEditText);

        spinnerlist = new ArrayList<>();
        spinnerlist.add("Online");
        spinnerlist.add("Through the line");

        checkPermissions();

        uploadCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAImage(v);
            }
        });


        if (getContext() != null) {
            spinneradapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_activated_1
                    , spinnerlist);

            s.setAdapter(spinneradapter);


            spinneradapter.notifyDataSetChanged();


            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                                           long arg3) {

                    switch (position) {
                        case 0:
                            type = 0;
                            hidethroughLineView(false);
                            break;
                        case 1:
                            type = 1;
                            hidethroughLineView(true);
                            break;
                        default:
                            type = 0;
                            hidethroughLineView(false);
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }


        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    BrandingRequest brandingRequest = new BrandingRequest();

                    brandingRequest.setContent(contentEditText.getText().toString());
                    brandingRequest.setLogoUrl(logoUrl);

                    brandingRequest.setDimension(contentEditText.getText().toString());
                    brandingRequest.setFonts(fontEditText.getText().toString());
                    brandingRequest.setColors(colorEditText.getText().toString());

                    if (type == 0) {
                        brandingRequest.setBrandingType("Online");
                    } else {
                        brandingRequest.setAmountOfCopies(amountCopiesEditText.getText().toString());
                        brandingRequest.setDeliveryInformation(deliveryEditText.getText().toString());
                        brandingRequest.setBrandingType("Through the line");
                    }
                    onSaveTappedListener.onBrandingRequested(brandingRequest, type);
                }
            });
        }


        return view;
    }


    private void uploadingStarted() {
        int colorFrom = getResources().getColor(R.color.colorAccent);
        int colorTo = getResources().getColor(R.color.colorPrimaryDark);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                uploadCardView.setBackgroundColor((int) animator.getAnimatedValue());
                uploadTextView.setText("uploading...");
                uploadTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            }

        });
        colorAnimation.start();
    }

    private void setUploadProgress(int value) {
        uploadTextView.setText(String.format(Locale.getDefault(), "uploading...%d%%", value));
    }

    private void uploadReset() {
        int colorFrom = getResources().getColor(R.color.colorPrimaryDark);
        int colorTo = getResources().getColor(R.color.colorAccent);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                uploadCardView.setBackgroundColor((int) animator.getAnimatedValue());
                uploadTextView.setText("kindly select logo to upload");
                uploadTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

        });
        colorAnimation.start();
    }

    private void uploadingFinished() {
        int colorFrom = getResources().getColor(R.color.colorPrimaryDark);
        int colorTo = getResources().getColor(R.color.colorSuccess);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                uploadCardView.setBackgroundColor((int) animator.getAnimatedValue());
                uploadTextView.setText("upload complete");
                uploadTextView.setTextColor(getResources().getColor(R.color.colorSuccessDark));
            }

        });
        colorAnimation.start();
    }

    private void hidethroughLineView(Boolean isThroughLine) {
        if (isThroughLine) {

            view.findViewById(R.id.throughLineView).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.throughLineView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFragmentActivatedListener = (onFragmentActivatedListener) context;
            onSaveTappedListener = (onSaveTappedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        if (getActivity() != null) {
                            assert selectedImage != null;
                            imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    uploadingStarted();
                    assert selectedImage != null;
                    UploadHelper.getInstance().uploadImageToStorage(yourSelectedImage, "branding/" + selectedImage.getLastPathSegment(), new UploadHelper.OnUploadListener() {
                        @Override
                        public void onFailure(String message) {
                            uploadReset();
                            Snackbar.make(uploadCardView, message, Snackbar.LENGTH_LONG)
                                    .show();
                        }

                        @Override
                        public void onSuccess(String fileUrl) {
                            logoUrl = fileUrl;
                            uploadingFinished();
                        }

                        @Override
                        public void onProgress(int progress) {
                            setUploadProgress(progress);
                        }
                    });
                }
        }
    }


    public void pickAImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

}
