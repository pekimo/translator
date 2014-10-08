package com.example.pavel.translator;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class TranslatorFragment extends Fragment {

    public ImageButton BtnSwap;
    public Spinner SpnFrom;
    public Spinner SpnOn;
    public TextView TextViewString;
    public EditText EditTextString;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.translator, null);

        TextViewString = (TextView) view.findViewById(R.id.textViewString);
        EditTextString = (EditText) view.findViewById(R.id.editTextString);

        SpnFrom = (Spinner) view.findViewById(R.id.spn_language_from);
        SpnOn = (Spinner) view.findViewById(R.id.spn_language_on);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), MyActivity.getLangs(), android.R.layout.simple_spinner_item, new String[] {"Langs"}, new int[] {android.R.layout.simple_spinner_dropdown_item} );

        SpnFrom.setAdapter(adapter);
        SpnOn.setAdapter(adapter);

        BtnSwap = (ImageButton) view.findViewById(R.id.btn_swap);
        BtnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int from = SpnFrom.getSelectedItemPosition();
                int on = SpnOn.getSelectedItemPosition();
                SpnFrom.setSelection(on);
                SpnOn.setSelection(from);
            }
        });

        return view;

    }




}
