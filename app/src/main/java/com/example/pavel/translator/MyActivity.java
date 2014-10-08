package com.example.pavel.translator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class MyActivity extends Activity {

    public Fragment PreloaderFragment;
    public Fragment TranslatorFragment;
    public FragmentTransaction TransFragment;
    public BroadcastReceiver Broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Log.d("LOG_TAG", "My Activity 000");

        TranslatorFragment = new TranslatorFragment();
        TransFragment = getFragmentManager().beginTransaction();

        TransFragment.add(R.id.mainActivity, TranslatorFragment);
        TransFragment.commit();

//        Broadcast = new BroadcastReceiver() {
//
//            public void onReceive(Context context, Intent intent) {
//                Log.d("LOG_TAG", "My Activity 333");
//                Object langs = intent.getExtras().get("Langs");
//                Log.d("LOG_TAG", "My Activity 334");
//                Log.d("LOG_TAG", langs.toString());
//            }
//        };

        Log.d("LOG_TAG", "My Activity 111");

        startService(new Intent(this, TranslatorService.class));

        Log.d("LOG_TAG", "My Activity 222");


    }

}
