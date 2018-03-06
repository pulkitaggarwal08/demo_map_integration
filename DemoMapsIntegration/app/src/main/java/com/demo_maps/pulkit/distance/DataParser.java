package com.demo_maps.pulkit.distance;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String, String> getDuration(JSONArray googleDirectionsJson) {

        HashMap<String, String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance = "";

//        Log.d("JSONResponse", googleDirectionsJson.toString());

        try {

            if(googleDirectionsJson != null){
                /*googleDirectionsJson will null if Directions API permission is OFF*/
                duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
                distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

                googleDirectionsMap.put("duration", duration);
                googleDirectionsMap.put("distance", distance);
            }
            else {
                //Todo: print error here
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleDirectionsMap;
    }

    public HashMap<String, String> parseDirections(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
//            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonArray);
    }

}
