package com.nowy.JsBridgeDemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.nowy.JsBridgeDemo.WebBridge.JSBridge;
import com.nowy.JsBridgeDemo.WebBridge.JsWebChromeClient;
import com.nowy.JsBridgeDemo.test.JSBridgeImpl;

public class MainActivity extends AppCompatActivity {

    private WebView mWvMain;

    static {
//        JSBridge.register("test", JSBridgeImpl.class);
        JSBridge.register(JSBridgeImpl.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }




    private void initView(){
        mWvMain = findViewById(R.id.main_WvMain);

        initWvSettings();
        mWvMain.setWebChromeClient(new JsWebChromeClient());
        mWvMain.loadUrl("file:///android_asset/jsTest.html");
    }



    private void initWvSettings(){
        WebSettings webSettings = mWvMain.getSettings();
        //设置支持JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        //设置加载进来的页面自适应手机屏幕
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);




        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webSettings.setUserAgentString(webSettings.getUserAgentString() + " VersionCode/" + InstallUtil
//                .getVersionCode(this));
        webSettings.setDefaultTextEncodingName("UTF-8") ;
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);


//        webSettings.setCacheMode(WebSettings.LO);
        webSettings.setSupportZoom(true);
//        webSettings.setDatabaseEnabled(true);
//        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        String dir = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(dir);
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setAllowFileAccess(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


}
