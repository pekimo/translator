/**
 * Created by nap on 10/5/2014.
 */
package com.example.pavel.translator.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiTranslator {
    private static final String KEY = "?key=trnsl.1.1.20141004T224928Z.a181d852adc7d58a.8cb73e0bbacb87e915f0c848870bbbc28491dd89";
    private static final String URL_GET_LANG = "https://translate.yandex.net/api/v1.5/tr.json/getLangs";
    private static final String URL_DETECT = "https://translate.yandex.net/api/v1.5/tr.json/detect";
    private static final String URL_TRANSLATE = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private static final String APP_LANG = "&ui=ru";
//    private HashSet<String> dirs = new HashSet<String>();
//    private HashMap<String, String> langs = new HashMap<String, String>();
//
//    public HashSet<String> getDirs() throws NullPointerException {
//        return this.dirs;
//    }

    private String sendGet(String url) throws Exception {
        Log.d("_____________", "sendGet1");
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        Log.d("_____________", "sendGet2");
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

//    private void parseDirsLangs(String str) throws Exception {
//        JSONObject obj = new JSONObject(str);
//        throwExceptionIfError(obj);
//        JSONArray dirs = obj.getJSONArray("dirs");
//        JSONArrayToHashSet(dirs, this.dirs);
//        JSONObject langs = obj.getJSONObject("langs");
//        JSONObjectToHashMap(langs, this.langs);
//    }

    private void JSONArrayToHashSet(JSONArray ja, HashSet<String> hs) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            hs.add((String)ja.get(i));
        }
    }

    private void JSONObjectToHashMap(JSONObject jo, HashMap<String, String> hm) throws JSONException {
        Iterator it = jo.keys();
        while (it.hasNext()) {
            String n = (String)it.next();
            hm.put(n, (String)jo.get(n));
        }
    }

    public String detectLang(String text) throws Exception {
        String response = sendGet(URL_DETECT + KEY + "&text=" + URLEncoder.encode(text));
        JSONObject obj = new JSONObject(response);
        throwExceptionIfError(obj);
        return (String)obj.get("lang");
    }

    public String translate(String text, String direction) throws Exception {
        String response = sendGet(URL_TRANSLATE + KEY + "&text=" + URLEncoder.encode(text) + "&lang=" + direction);
        JSONObject obj = new JSONObject(response);
        throwExceptionIfError(obj);
        JSONArray translated = obj.getJSONArray("text");
        return (String)translated.get(0);
    }

    public String getLangs() throws Exception {
        String response = sendGet(URL_GET_LANG + KEY + APP_LANG);
//        Log.d("_____________", response);
//        parseDirsLangs(response);
        return response;
    }


    private void throwExceptionIfError(JSONObject obj) throws Exception {
        if (!obj.has("code")) {
            return;
        }
        Integer code = (Integer) obj.get("code");
        switch (code) {
            case 200:
                return;
            case 401:
                throw new Exception("1");
            case 402:
                throw new Exception("1");
            case 403:
                throw new Exception("2");
            case 404:
                throw new Exception("2");
            case 413:
                throw new Exception("3");
            case 422:
                throw new Exception("4");
            case 501:
                throw new Exception("5");
            default:
                throw new Exception("6");
        }
    }
}
