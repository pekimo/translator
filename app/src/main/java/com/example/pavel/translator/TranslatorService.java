package com.example.pavel.translator;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import  com.example.pavel.translator.api.ApiTranslator;

import java.util.HashMap;

public class TranslatorService extends Service {

    public ApiTranslator Translator;
    public String langs;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LOG_TAG", "IN Tranlator Service 000");
        try {
            Translator = new ApiTranslator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LOG_TAG", "IN Tranlator Service 111");
        try {
            langs = Translator.getLangs();

            Log.d("LOG_Tag", langs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("Langs", langs);
        sendBroadcast(intent);
        Log.d("LOG_TAG", "IN Tranlator Service 222");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG_TAG", "onDestroy");
    }

}
