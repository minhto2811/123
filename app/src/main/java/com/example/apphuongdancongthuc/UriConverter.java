package com.example.apphuongdancongthuc;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UriConverter {
    private static final Gson gson = new Gson();

    public static String fromUriList(List<Uri> uriList) {
        ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            String str = uriList.get(i).getPath();
            paths.add(str);
        }
        return gson.toJson(paths);
    }

    public static List<Uri> toUriList(String jsonString) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> stringList = gson.fromJson(jsonString, listType);
        List<Uri> uriList = new ArrayList<>();
        for (String path : stringList) {
            uriList.add(Uri.parse(path));
        }
        return uriList;
    }
}
