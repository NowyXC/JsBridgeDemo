package com.nowy.JsBridgeDemo.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nowy on 2017/12/27.
 */

public class JsonUtil {
    public static final String TAG = JsonUtil.class.getSimpleName();



    private static Gson gson = null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private JsonUtil(){
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static <T> T parseJson(String data, Class<T> clazz){
        return gson.fromJson(data,clazz);
    }

    public static <T> T parseJson(String data, TypeToken<T> typeToken){
        return gson.fromJson(data,typeToken.getType());
    }


    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) throws Exception {
        List<T> lst =  new ArrayList<>();

        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            lst.add(new Gson().fromJson(elem, clazz));
        }

        return lst;
    }


    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     * @param data
     * @return
     */
    public static <T> List<T> parseListJson(String data) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(data, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }





    /**
     * @dec JSON格式数据转MAP
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJson2Map(String jsonStr){
        Map<String, Object> map = null;
        try {
            map = gson.fromJson(jsonStr,new TypeToken<HashMap<String,Object>>(){}.getType());
        }catch (Exception e){
            Logger.t(TAG).e(e.getMessage());
        }
        return map;
    }






}
