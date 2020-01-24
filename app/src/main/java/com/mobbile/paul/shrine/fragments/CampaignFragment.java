package com.mobbile.paul.shrine.fragments;


import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.listeners.onSaveTappedListener;
import com.mobbile.paul.shrine.models.CampaignRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampaignFragment extends Fragment {


    onFragmentActivatedListener onFragmentActivatedListener;

    TextInputEditText backgroundEditText, objectivesEditText, consumerInsightEditText, featuresEditText, competitorsEditText, benefitEditText, budgetEditText, timingEditText, deliverableEditText;

    onSaveTappedListener onSaveTappedListener;

    public CampaignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        onFragmentActivatedListener.onFragmentEntered();

        View view = inflater.inflate(R.layout.fragment_creative, container, false);

        backgroundEditText = view.findViewById(R.id.backgroundEditText);
        objectivesEditText = view.findViewById(R.id.objectivesEditText);
        consumerInsightEditText = view.findViewById(R.id.consumerInsightEditText);
        featuresEditText = view.findViewById(R.id.featuresEditText);
        competitorsEditText = view.findViewById(R.id.competitorsEditText);
        benefitEditText = view.findViewById(R.id.benefitEditText);
        budgetEditText = view.findViewById(R.id.budgetEditText);
        timingEditText = view.findViewById(R.id.timingEditText);
        deliverableEditText = view.findViewById(R.id.deliverableEditText);


        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    CampaignRequest campaignRequest = new CampaignRequest();
                    campaignRequest.setBackground(backgroundEditText.getText().toString());
                    campaignRequest.setObjectives(objectivesEditText.getText().toString());
                    campaignRequest.setConsumerInsight(consumerInsightEditText.getText().toString());
                    campaignRequest.setFeatures(featuresEditText.getText().toString());
                    campaignRequest.setCompetitors(competitorsEditText.getText().toString());
                    campaignRequest.setBenefit(benefitEditText.getText().toString());
                    campaignRequest.setBudget(budgetEditText.getText().toString());
                    campaignRequest.setTimings(timingEditText.getText().toString());
                    campaignRequest.setDeliverable(deliverableEditText.getText().toString());

                    onSaveTappedListener.onCampaignRequested(campaignRequest);
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