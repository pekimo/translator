package com.example.pavel.translator;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import  com.example.pavel.translator.api.ApiTranslator;

public class TranslatorService extends IntentService {

    public ApiTranslator Translator;
    public String langs;

    public TranslatorService() {
        super("TranslatorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Translator = new ApiTranslator();
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
    }

}
