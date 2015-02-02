package com.adapp.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abhidhar on 1/29/15.
 */
public class ImageResult implements Serializable {

    private static final long serialVersionUID = -8681656166565609171L;
    public String fullUrl;
    public String thumbUrl;
    public String title;


    public ImageResult(JSONObject json) {

        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray arr) {

        ArrayList<ImageResult> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                list.add(new ImageResult(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
