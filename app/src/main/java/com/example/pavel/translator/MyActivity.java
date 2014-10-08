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
import android.widget.Toast;

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
    public static final String Key = "Dirs";
    public static final String Value = "Langs";
    //private static HashSet<String> dirs = new HashSet<String>();
    public static ArrayList<HashMap<String, String>> langs = new ArrayList<HashMap<String, String>>();

    public static final String Error = "ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        PreloaderFragment = new PreloaderFragment();
        TransFragment = getFragmentManager().beginTransaction();

        TransFragment.add(R.id.mainActivity, PreloaderFragment);
        TransFragment.commit();

        startService(new Intent(this, TranslatorService.class).putExtra("COMMAND", 1));

        Broadcast = new Receiver();

        IntentFilter filter = new IntentFilter(Receiver.ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(Broadcast, filter);

    }

    private void parseDirsLangs(String str) throws Exception {
        JSONObject obj = new JSONObject(str);
//        JSONArray dirs_ = obj.getJSONArray("dirs");
//        JSONArrayToHashSet(dirs_, dirs);
        JSONObject langs_ = obj.getJSONObject("langs");
        JSONObjectToHashMap(langs_);
    }

//    private void JSONArrayToHashSet(JSONArray ja) throws JSONException {
//        for (int i = 0; i < ja.length(); i++) {
//            hs.add((String)ja.get(i));
//        }
//    }

    private void JSONObjectToHashMap(JSONObject jo) throws JSONException {
        Iterator it = jo.keys();
        while (it.hasNext()) {
            HashMap<String, String> item = new HashMap<String, String>();
            String n = (String)it.next();
            item.put(Key, n);
            item.put(Value, (String)jo.get(n));
            langs.add(item);
        }
        Log.d("LOG_TAG", "Reciever 333");
    }


    public static ArrayList<HashMap<String, String>> getLangs() {
        return langs;
    }

    public class Receiver extends BroadcastReceiver {

        public static final String ACTION = "received";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG_TAG", "Reciever 111");

            int command = intent.getIntExtra("COMMAND", 0);

            switch (command) {
                case 1: {
                    Log.d("LOG_TAG", "Reciever 222");
                    String langs = intent.getStringExtra("LANGS");
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
                case -1: {
                    Toast toast = Toast.makeText(context, "Проверьте сооединение с интернетом", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
            }

        }
    }

}
