package com.example.pavel.translator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class MyActivity extends Activity {

    public Fragment PreloaderFragment;
    public Fragment TranslatorFragment;
    public FragmentTransaction TransFragment;
    public Receiver Broadcast;

    public static ArrayList<String> langs = new ArrayList<String>();
    public static ArrayList<String> dirs = new ArrayList<String>();
    public static HashMap<String, String> langsReductions = new HashMap<String, String>();

    public static final String Error = "ERROR";
    public String BROADCAST_ACTION = "Activity_broadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Log.d("INFO","Activity created");

        PreloaderFragment = new PreloaderFragment();
        TransFragment = getFragmentManager().beginTransaction();

        TransFragment.add(R.id.mainActivity, PreloaderFragment);
        TransFragment.commit();

        startService(new Intent(this, TranslatorService.class).putExtra("COMMAND", 1));

        Broadcast = new Receiver();

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(Broadcast, filter);

    }

    private void parseDirsLangs(String str) throws Exception {
        JSONObject obj = new JSONObject(str);
        JSONArray dirs_ = obj.getJSONArray("dirs");
        JSONArrayToHashSet(dirs_);
        JSONObject langs_ = obj.getJSONObject("langs");
        JSONObjectToHashMap(langs_);
    }

    private void JSONArrayToHashSet(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            dirs.add((String) ja.get(i));
        }
    }

    private void JSONObjectToHashMap(JSONObject jo) throws JSONException {
        Iterator it = jo.keys();
        while (it.hasNext()) {
            String n = (String)it.next();
            langs.add((String)jo.get(n));
            langsReductions.put((String)jo.get(n), n);
        }
        Log.d("LOG_TAG", "Reciever 333");
    }

    public static String getReductions(String key) {
        return langsReductions.get(key);
    }

    public static int getPositionLand(String lang) {
        return langs.indexOf(lang);
    }

    public static boolean isDirs(String d) {
        return dirs.contains(d);
    }

    public static HashMap<String, String> getLangsReductions() {
        return langsReductions;
    }

    public static ArrayList<String> getDirs() {
        return dirs;
    }

    public static ArrayList<String> getLangs() {
        return langs;
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG_TAG", "Reciever 111");

            int command = intent.getIntExtra("COMMAND", 0);

            switch (command) {
                case 1: {
                    Log.d("LOG_TAG", "Reciever 222");
                    String langs = intent.getStringExtra("DATA");
                    try {
                        parseDirsLangs(langs);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(context, "Ошибка данных", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    TranslatorFragment = new TranslatorFragment();
                    TransFragment = getFragmentManager().beginTransaction();
                    TransFragment.replace(R.id.mainActivity, TranslatorFragment);
                    TransFragment.commit();
                    break;
                }
                case 2: {
                    String text = intent.getStringExtra("DATA");
                        ((TextView) TranslatorFragment.getView().findViewById(R.id.textViewString)).setText(text);
                        break;
                    }
                case -1: {
                    Toast toast = Toast.makeText(context, "Проверьте сооединение с интернетом", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
            }

        }
    }

}
