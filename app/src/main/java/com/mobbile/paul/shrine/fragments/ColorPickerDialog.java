package com.mobbile.paul.shrine.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.mobbile.paul.codelab.R;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;


public class ColorPickerDialog extends DialogFragment {

    // Library for picker view at:
    // https://github.com/duanhong169/ColorPicker

    public interface OnColorSelectedCallback {
        void onColorSelected(int color);
    }

    private OnColorSelectedCallback callback;

    public void addOnColorSelectedCallback(OnColorSelectedCallback c) {
        callback = c;
    }

    private static final String COLOR = "COLOR";
    private int mColor;

    public static ColorPickerDialog newInstance(int initialColor) {
        ColorPickerDialog f = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt(COLOR, initialColor);
        f.setArguments(args);
        return f;
    }

    public ColorPickerDialog() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if( b != null ) {
            mColor = b.getInt(COLOR);
        }
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    @NonNull
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Activity a = requireActivity();
        AlertDialog.Builder b = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();
        final View dv = inflater.inflate(R.layout.color_picker_dialog, null);

        final View colorSample = dv.findViewById(R.id.current_color_sample);
        ColorPickerView cpv = dv.findViewById(R.id.color_picker);
        cpv.setInitialColor(mColor);
        cpv.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                mColor = color;
                colorSample.setBackgroundColor(color);
            }
        });

        b.setView(dv).
                setPositiveButton(R.string.choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( callback != null ) {
                            callback.onColorSelected(mColor);
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return b.create();
    }
}