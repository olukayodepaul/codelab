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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.helper.UploadHelper;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.listeners.onSaveTappedListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryBoardFragment extends Fragment {


    onFragmentActivatedListener onFragmentActivatedListener;
    MaterialCardView uploadCardView;

    TextView uploadTextView;

    String scriptUrl = "";

    private static final int SELECT_PHOTO = 100;
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


    public StoryBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        onFragmentActivatedListener.onFragmentEntered();

        View view =  inflater.inflate(R.layout.fragment_story_board, container, false);
        uploadCardView = view.findViewById(R.id.uploadCardView);
        uploadTextView = view.findViewById(R.id.uploadTextView);

        checkPermissions();

        uploadCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAImage(v);
            }
        });


        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    onSaveTappedListener.onStoryBoardRequested(scriptUrl);
                }
            });
        }

        return view;

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
                    UploadHelper.getInstance().uploadImageToStorage(yourSelectedImage, "flyers/" + selectedImage.getLastPathSegment(), new UploadHelper.OnUploadListener() {
                        @Override
                        public void onFailure(String message) {
                            uploadReset();
                            Snackbar.make(uploadCardView, message, Snackbar.LENGTH_LONG)
                                    .show();
                        }

                        @Override
                        public void onSuccess(String fileUrl) {
                            scriptUrl = fileUrl;
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
