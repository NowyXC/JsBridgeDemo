package com.nowy.JsBridgeDemo.test;

import android.webkit.WebView;
import android.widget.Toast;

import com.nowy.JsBridgeDemo.WebBridge.IBridge;
import com.nowy.JsBridgeDemo.WebBridge.JSHandler;
import com.nowy.JsBridgeDemo.WebBridge.JsMethod;

import java.util.Map;

/**
 * Created by Nowy on 2018/2/23.
 */

public class JSBridgeImpl implements IBridge {

    @JsMethod
    public static void T(WebView webView, Map<String,Object> param, JSHandler callback){
        String msg = null;
        if(param != null){
            msg = traverseMap(param);
            Toast.makeText(webView.getContext().getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
            if(callback != null){
                callback.apply("{'msg':'native response'}");
            }
        }
    }



    public static String traverseMap(Map<String,Object> map){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
