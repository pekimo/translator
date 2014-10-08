package com.example.pavel.translator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


public class TranslatorFragment extends Fragment {

    public ImageButton BtnSwap;
    public Spinner SpnFrom;
    public Spinner SpnOn;
    public TextView TextViewString;
    public EditText EditTextString;
    public String data[] = new String[] { "one", "two", "three", "four", "one", "two", "three",
            "four","one", "two", "three", "four","one", "two", "three", "four" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.translator, null);

        TextViewString = (TextView) view.findViewById(R.id.textViewString);
        EditTextString = (EditText) view.findViewById(R.id.editTextString);

        SpnFrom = (Spinner) view.findViewById(R.id.spn_language_from);
        SpnOn = (Spinner) view.findViewById(R.id.spn_language_on);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
