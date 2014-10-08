package com.example.pavel.translator;

import android.app.IntentService;
import android.content.Intent;
import  com.example.pavel.translator.api.ApiTranslator;


public class TranslatorService extends IntentService {

    public ApiTranslator Translator;

    public TranslatorService() {
        super("TranslatorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Translator = new ApiTranslator();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MyActivity.Receiver.ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        int command = intent.getIntExtra("COMMAND", 0);

        switch (command) {
            case 1: {//получить языки
                try {
                    String langs = Translator.getLangs();
                    broadcastIntent.putExtra("DATA", langs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {//получить перевод

                break;
            }
        }

        broadcastIntent.putExtra("COMMAND", command);
        sendBroadcast(broadcastIntent);

    }




}
