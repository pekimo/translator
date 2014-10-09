package com.example.pavel.translator;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class TranslatorFragment extends Fragment {

    public ImageButton BtnSwap;
    public Spinner SpnFrom;
    public Spinner SpnOn;
    public TextView TextViewString;
    public EditText EditTextString;
    public String BROADCAST_ACTION = "Fragment_broadcast";
    public BroadcastReceiver Broadcast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.translator, null);

        TextViewString = (TextView) view.findViewById(R.id.textViewString);
        EditTextString = (EditText) view.findViewById(R.id.editTextString);

        SpnFrom = (Spinner) view.findViewById(R.id.spn_language_from);
        SpnOn = (Spinner) view.findViewById(R.id.spn_language_on);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,  MyActivity.getLangs());

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

        EditTextString.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Integer keyCode = keyEvent.getKeyCode();
                Log.d("Event", keyEvent.toString());
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d("KEY", keyCode.toString());
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(EditTextString.getWindowToken(), 0);
                }

                getActivity().startService(new Intent(getActivity(), TranslatorService.class)
                        .putExtra("COMMAND", 1)
                        .putExtra("TEXT", EditTextString.getText()));

                return false;
            }
        });


        Broadcast = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                int command = intent.getIntExtra("COMMAND", 0);
                switch (command) {
                    case 2: {
                        String text = intent.getStringExtra("DATA");
                        TextViewString.setText(text);
                        break;
                    }
                    case -1: {
                        Toast toast = Toast.makeText(context, "Проверьте сооединение с интернетом", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    }
                }


            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(Broadcast, filter);

        return view;

    }

}
