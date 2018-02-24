package com.nowy.JsBridgeDemo.urlInteractive;

import android.text.TextUtils;

import com.nowy.JsBridgeDemo.urlUtil.UrlOperation;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Nowy on 2017/11/3.
 * 本类对URL进行解析处理，并返回相应的schemeMode和actionModel
 * 返回一个处理回调事件的handler
 *
 * 使用方式为：
 * UrlManager.resolveUrl(url,new IActionHandler(){...})
 * 其中 IActionHandler拥有默认实现的适配抽象类{@link SimpleActionHandler}
 *
 */

public class UrlManager {
    /**
     * 协议头
     */
    public interface SchemeModel {
        int MODEL_DEF = 0;
        int MODEL_TEL = 1;
        int MODEL_REFRESH = 2;
        int MODEL_APP = 3;
        int MODEL_JUMP = 4;
    }

    /**
     * APP协议的方法
     */
    public interface AppActionModel{
        int ACTION_DEF = 30;//默认
        int ACTION_OPEN = 31;//开启界面
        int ACTION_CLOSE = 32;//关闭界面
        int ACTION_DISMISS = 33;
        int ACTION_REGISTERED = 34;//注册
        int ACTION_SHARE = 35;//分享
        int ACTION_UPDATE = 36;//更新
        int ACTION_COLLECTION = 37;//收藏
        int ACTION_OPEN_SERVICE = 38;//开启服务
        int ACTION_OPEN_ALBUM = 39;//打开相册，选择图片
        int ACTION_PAY = 40;//支付
    }

    /**
     * tel协议的参数
     */
    public interface TelActionModel{
        int ACTION_TEL_CALL = 1;//直接拨打电话
        int ACTION_TEL_CALL_DIAL = 0;//拨号界面
        int ACTION_TEL_ERROR = -1;//没有号码
    }

    /**
     * 匹配协议头
     * @param url
     * @return
     */
    public static int matchScheme(String url) {
        if (url.startsWith("tel:")) {
            return SchemeModel.MODEL_TEL;
        } else if (url.startsWith("refreshshoppingmall")) {
            return SchemeModel.MODEL_REFRESH;
        } else if (url.startsWith("app:")) {
            return SchemeModel.MODEL_APP;
        }else if("jump://ad.jolyvia".equals(url)){
            return SchemeModel.MODEL_JUMP;
        }
        return SchemeModel.MODEL_DEF;
    }


    /**
     * 匹配APP协议的方法
     * @param url
     * @return
     */
    public static int matchAppAction(String url){
        String data = url.substring("app://".length(),url.length());
        if(TextUtils.isEmpty(data)){
            return AppActionModel.ACTION_DEF;
        }
        if(data.startsWith("open")){//开启
            return AppActionModel.ACTION_OPEN;
        }else if(data.startsWith("close")){
            return AppActionModel.ACTION_CLOSE;
        }else if(data.startsWith("registered")){
            return AppActionModel.ACTION_REGISTERED;
        }else if(data.startsWith("share")){
            return AppActionModel.ACTION_SHARE;
        }else if(data.startsWith("dismiss")){
            return AppActionModel.ACTION_DISMISS;
        }else if(data.startsWith("update")){
            return AppActionModel.ACTION_UPDATE;
        }else if(data.startsWith("pay")){
            return AppActionModel.ACTION_PAY;
        }else if(data.startsWith("photo")){
            return AppActionModel.ACTION_OPEN_ALBUM;
        }else{
            return AppActionModel.ACTION_DEF;
        }
    }

    /**
     * 拨打电话
     * @param isCall 是否直接拨打电话
     *               注意：直接拨打电话需要权限
     * @return
     */
    public static int getTelAction(boolean isCall){
        return isCall ? TelActionModel.ACTION_TEL_CALL : TelActionModel.ACTION_TEL_CALL_DIAL;
    }

    /**
     * 解析URL的请求方法，并返回一个handler
     * @param url
     * @param handler
     */
    public static void resolveUrl(String url, IActionHandler handler){
        int action = 0;
        int schemeMode = matchScheme(url);
        Map data = null;
        switch (schemeMode){
            case SchemeModel.MODEL_TEL:
                if(handler != null){
                    String phone=getTelNum(url);
                    if(TextUtils.isEmpty(phone)){
                        action = TelActionModel.ACTION_TEL_ERROR;
                    }else{
                        action = getTelAction(false);
                    }
                    handler.handleTel(action,url);
                }

                break;
            case SchemeModel.MODEL_REFRESH:
                if(handler != null){
                    handler.handleRefresh(url);
                }
                break;
            case SchemeModel.MODEL_APP:
                UrlOperation urlOperation = new UrlOperation(url);
                data = urlOperation.getRequestParam();
                action = matchAppAction(url);
                if(handler != null){
                    handler.handleApp(action,data);
                }
                break;
            case SchemeModel.MODEL_JUMP:
                if(handler != null){
                    handler.handleJump(url);
                }
                break;
            case SchemeModel.MODEL_DEF:
            default:
                if(handler != null){
                    handler.handleDef(url);
                }
                break;
        }
        if(handler != null){
            handler.handle(url,schemeMode,action,data);
        }
    }


    /**
     * 获取手机号
     * @param url
     * @return
     */
    public static String getTelNum(String url){
        String data = "";
        if(url.contains("tel://")){
            data = url.substring("tel://".length(),url.length());
        }else{
            data = url.substring("tel:".length(),url.length());
        }
        return data;
    }


    /**
     * 检测是否网页链接
     * @param url
     * @return
     */
    public static boolean checkURL(String url){
        if(TextUtils.isEmpty(url)){
            return false;
        }
        String regular="(http|https)://[\\S]*";
        return Pattern.matches(regular, url);
    }

}
