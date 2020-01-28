package com.mobbile.paul.shrine.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.listeners.onSaveTappedListener;
import com.mobbile.paul.shrine.models.LogoRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogoRequestFragment extends Fragment {


    View colorViewOne;
    //    private ColorPickerView colorPickerView;
    private AlertDialog alertDialog;


    private String colorOne = "";

    private String colorTwo = "";

    private String colorThree = "";


    onFragmentActivatedListener onFragmentActivatedListener;
    onSaveTappedListener onSaveTappedListener;

    public LogoRequestFragment() {
        // Required empty public constructor
    }

    TextInputEditText brandNameEditText;
    TextInputEditText brandColorEditText;
    TextInputEditText brandInspirationEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logo_request_2, container, false);
        brandNameEditText = view.findViewById(R.id.brandNameEditText);
        brandColorEditText = view.findViewById(R.id.brandColorEditText);
        brandInspirationEditText = view.findViewById(R.id.brandInspirationEditText);

        onFragmentActivatedListener.onFragmentEntered();

        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(v -> {

                LogoRequest logoRequest = new LogoRequest();
                logoRequest.setBrandName(brandNameEditText.getText().toString());
                logoRequest.setBrandInspiration(brandInspirationEditText.getText().toString());
                logoRequest.setBrandColor(brandColorEditText.getText().toString());
                onSaveTappedListener.onLogoRequestd(logoRequest);

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
