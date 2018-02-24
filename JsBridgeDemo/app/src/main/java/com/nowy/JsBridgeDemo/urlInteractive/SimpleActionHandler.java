package com.nowy.JsBridgeDemo.urlInteractive;

import android.content.Context;


import java.util.Map;

/**
 * Created by Nowy on 2017/11/3.
 * 处理HTML5与native通过URL交互的抽象类
 * 默认实现拨号功能
 * 其他实现都是空方法，可以重写实现
 * handleDef(Url)方法中需要实现webView.loadUrl(url)方法。
 * 此处不实现主要考虑可能引用的是第三方的webView,所以让调用者自己实现
 */

public abstract class SimpleActionHandler implements IActionHandler {
    private Context mContext;
    public SimpleActionHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void handleApp(int action, Map data) {

    }

    @Override
    public void handleTel(int action, String tel) {
        if(IActionHandler.ACTION_TEL_CALL == action){//直接拨打电话
//            IntentUtil.call(mContext,tel);
        }else{
//            IntentUtil.toCallAty(mContext,tel);
        }

    }

    @Override
    public void handleRefresh(String url) {

    }

    @Override
    public void handleJump(String url) {

    }

    @Override
    public void handleDef(String url) {

    }

    @Override
    public void handle(String url, int schemeModel, int action, Map data) {

    }
}
