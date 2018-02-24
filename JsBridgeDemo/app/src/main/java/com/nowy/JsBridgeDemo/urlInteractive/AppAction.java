package com.nowy.JsBridgeDemo.urlInteractive;

import android.app.Activity;

/**
 * Created by Nowy on 2018/2/12.
 *
 * int ACTION_OPEN = 31;
 * int ACTION_CLOSE = 32;
 * int ACTION_DISMISS = 33;
 * int ACTION_REGISTERED = 34;
 * int ACTION_SHARE = 35;
 * int ACTION_UPDATE = 36;
 * int ACTION_COLLECTION = 37;
 * int ACTION_OPEN_SERVICE = 38;
 * int ACTION_DEF = 39;
 * int ACTION_PAY = 40;
 */
public class AppAction {

    public static final int WEB_NATIVE = 0;//原生webView
    public static final int WEB_X5 = 1;//X5webView
    public static final int WEB_OTHER = 2;//其他第三方webView


    public void open(Activity activity, int webType, String url, int reqCode){
        if(WEB_X5 == webType){
            //开启x5 webView的activity
        }else if(WEB_OTHER == webType){
            //开启其他内核的浏览器activity
        }else{
//            WebNativeAty.start(activity,url,reqCode);
        }
    }


    public void close(Activity activity, boolean isRefresh){
        activity.setResult(isRefresh ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        activity.finish();
    }




    public void dismiss(){

    }


    public void registered(){

    }


    public void share(){

    }


    public void update(){
        
    }

    public void collection(){

    }

    public void open_service(){

    }


    public void pay(){}

}
