package com.nowy.JsBridgeDemo.WebBridge;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

public class JSHandler {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static final String CALLBACK_JS_FORMAT = "javascript:JSBridge.onFinish('%s', %s);";
    private String mPort;
    private WeakReference<WebView> mWebViewRef;
 
    public JSHandler(WebView view, String port) {
        mWebViewRef = new WeakReference<>(view);
        mPort = port;
    }
 
    public void apply(String response) {
        final String execJs = String.format(CALLBACK_JS_FORMAT, mPort, response);
        if (mWebViewRef != null && mWebViewRef.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mWebViewRef.get() != null)
                        mWebViewRef.get().loadUrl(execJs);
                }
            });
        }
    }


}