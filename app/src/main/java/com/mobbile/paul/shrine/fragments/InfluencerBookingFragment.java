package com.mobbile.paul.shrine.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.listeners.onFragmentActivatedListener;
import com.google.codelabs.mdc.java.shrine.listeners.onSaveTappedListener;
import com.google.codelabs.mdc.java.shrine.models.InfluencerRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfluencerBookingFragment extends Fragment {

    onFragmentActivatedListener onFragmentActivatedListener;

    TextInputEditText influencerEditText;

    Chip facebook_chip, twitter_chip, snapchat_chip, instagram_chip;

    RadioButton first;

    String socialMedia = "";
    String mode = "images";

    onSaveTappedListener onSaveTappedListener;

    public InfluencerBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        onFragmentActivatedListener.onFragmentEntered();

        View view = inflater.inflate(R.layout.fragment_infuencer_booking, container, false);
        influencerEditText = view.findViewById(R.id.influencerEditText);
        facebook_chip = view.findViewById(R.id.facebook_chip);
        twitter_chip = view.findViewById(R.id.twitter_chip);
        snapchat_chip = view.findViewById(R.id.snapchat_chip);
        instagram_chip = view.findViewById(R.id.instagram_chip);
        first = view.findViewById(R.id.first);

        first.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mode = "images";
                else
                    mode = "videos";
            }
        });

        facebook_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia = "facebook";
            }
        });

        twitter_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia = "twitter";
            }
        });
        snapchat_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia = "snapchat";
            }
        });

        instagram_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia = "instagram";
            }
        });

        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    InfluencerRequest influencerRequest = new InfluencerRequest();
                    influencerRequest.setInfluencer(influencerEditText.getText().toString());
                    influencerRequest.setMode(mode);
                    influencerRequest.setSocialMedia(socialMedia);
                    onSaveTappedListener.onInfluencerRequested(influencerRequest);

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

}