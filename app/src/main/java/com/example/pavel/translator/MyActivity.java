package com.example.pavel.translator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class MyActivity extends Activity {

    private Fragment PreloaderFragment;
    private Fragment TranslatorFragment;
    private FragmentTransaction TransFragment;
    private Receiver Broadcast;
    private NetworkStateReceiver NetworkState;
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
        NetworkState = new NetworkStateReceiver();

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkFilter.addCategory(Intent.CATEGORY_DEFAULT);

        registerReceiver(Broadcast, filter);
        registerReceiver(NetworkState, networkFilter);

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
        langs.clear();
        while (it.hasNext()) {
            String n = (String)it.next();
            langs.add((String)jo.get(n));
            langsReductions.put((String)jo.get(n), n);
        }
        Log.d("LOG_TAG", "Reciever 333");
        Collections.sort(langs);
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
//                    Toast toast = Toast.makeText(context, "Возникла какая-то ошибка", Toast.LENGTH_LONG);
//                    toast.show();
                    break;
                }
            }

        }
    }

    public class NetworkStateReceiver extends BroadcastReceiver {
        private static final String TAG = "NetworkStateReceiver";
        private int previousState = 0;
        public NetworkStateReceiver() {}
        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.d(TAG, "Network connectivity change");

            if (intent.getExtras() != null) {
                final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

                if (ni != null && ni.isConnectedOrConnecting() && previousState == 0) {
                    Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                    startService(new Intent(MyActivity.this, TranslatorService.class).putExtra("COMMAND", 1));
                    previousState = 1;
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)  && previousState == 1) {
                    Toast toast = Toast.makeText(context, "Отсутствует соединение с интернетом", Toast.LENGTH_LONG);
                    toast.show();
                    previousState = 0;
                    Log.d(TAG, "There's no network connectivity");
                }
            }
        }
    }

}
