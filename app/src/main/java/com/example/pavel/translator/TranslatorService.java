package com.example.pavel.translator;

import android.app.IntentService;
import android.content.Intent;
import  com.example.pavel.translator.api.ApiTranslator;


public class TranslatorService extends IntentService {

    public ApiTranslator Translator;
    public String BROADCAST_ACTION_ACTIVITY = "Activity_broadcast";

    public TranslatorService() {
        super("TranslatorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Translator = new ApiTranslator();
        Intent broadcastIntent = new Intent();

        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);

        int command = intent.getIntExtra("COMMAND", 0);

        switch (command) {
            case 1: {//получить языки
                broadcastIntent.setAction(BROADCAST_ACTION_ACTIVITY);
                try {
                    String langs = Translator.getLangs();
                    broadcastIntent.putExtra("DATA", langs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {//получить перевод
                broadcastIntent.setAction(BROADCAST_ACTION_ACTIVITY);
                String textIn = intent.getStringExtra("TEXT");
                String dirs = intent.getStringExtra("DIRS");
                String textOut = "";
                try {
                    textOut = Translator.translate(textIn, dirs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                broadcastIntent.putExtra("DATA", textOut);
                break;
            }
        }

        broadcastIntent.putExtra("COMMAND", command);
        sendBroadcast(broadcastIntent);

    }


}
