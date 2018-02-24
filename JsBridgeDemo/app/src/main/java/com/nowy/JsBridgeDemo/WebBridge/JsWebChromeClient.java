package com.nowy.JsBridgeDemo.WebBridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Nowy on 2018/2/23.
 */

public class JsWebChromeClient extends WebChromeClient {

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if(JSBridge.callJava(view,message)){
            result.confirm();
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }


}
