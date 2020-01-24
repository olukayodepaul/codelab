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


import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.listeners.onSaveTappedListener;
import com.mobbile.paul.shrine.models.SocialMediaRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialMediaFragment extends Fragment {


    public SocialMediaFragment() {
        // Required empty public constructor
    }

    Chip pageIncreaseChip;
    Chip pageEngagementChip;
    onFragmentActivatedListener onFragmentActivatedListener;

    Chip facebook_chip, twitter_chip, snapchat_chip, instagram_chip;


    String socialMedia = "";

    String achievement = "";

    String achievementGoal = "";

    TextInputEditText brandInfoEditText;

    RadioButton first, second, third, firstEngageRadio, secondEngageRadio, thirdEngageRadio;

    private onSaveTappedListener onSaveTappedListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        onFragmentActivatedListener.onFragmentEntered();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_social_media, container, false);
        pageIncreaseChip = view.findViewById(R.id.pageIncreaseChip);
        pageEngagementChip = view.findViewById(R.id.pageEngagementChip);

        facebook_chip = view.findViewById(R.id.facebook_chip);
        twitter_chip = view.findViewById(R.id.twitter_chip);
        snapchat_chip = view.findViewById(R.id.snapchat_chip);
        instagram_chip = view.findViewById(R.id.instagram_chip);
        brandInfoEditText = view.findViewById(R.id.brandInfoEditText);

        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        third = view.findViewById(R.id.third);
        firstEngageRadio = view.findViewById(R.id.firstEngageRadio);
        secondEngageRadio = view.findViewById(R.id.secondEngageRadio);
        thirdEngageRadio = view.findViewById(R.id.thirdEngageRadio);


        pageIncreaseChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    view.findViewById(R.id.pageIncreaseRadioGroup).setVisibility(View.VISIBLE);
                    achievement = "Page Increase";
                } else {
                    view.findViewById(R.id.pageIncreaseRadioGroup).setVisibility(View.GONE);
                    achievement = "";

                }

            }
        });

        pageEngagementChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    view.findViewById(R.id.pageEngagementRadioGroup).setVisibility(View.VISIBLE);
                    achievement = "Page Engagement";

                } else {
                    view.findViewById(R.id.pageEngagementRadioGroup).setVisibility(View.GONE);
                    achievement = "";
                }

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

        first.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.build_up_to_1k_followers_in_3_months);

            }
        });
        second.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.get_up_to_5k_followers_in_6_months);

            }
        });
        third.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.get_over_20k_followers_in_1_year);

            }
        });

        firstEngageRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string._10_000_impressions_in_3_months);

            }
        });


        secondEngageRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.over_25k_impressions_in_6_months);

            }
        });


        thirdEngageRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.over_50k_impressions_in_1_year);

            }
        });

        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    SocialMediaRequest socialMediaRequest = new SocialMediaRequest();
                    socialMediaRequest.setBrandInfo(brandInfoEditText.getText().toString());
                    socialMediaRequest.setSocialMedia(socialMedia);
                    socialMediaRequest.setAchievement(achievement);
                    socialMediaRequest.setAcheivementGoal(achievementGoal);

                    onSaveTappedListener.onSocialMediaRequested(socialMediaRequest);
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
