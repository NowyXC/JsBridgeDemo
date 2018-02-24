package com.nowy.JsBridgeDemo.urlUtil;

import android.net.Uri;
import android.text.TextUtils;

import com.nowy.JsBridgeDemo.util.ConvertUtil;
import com.nowy.JsBridgeDemo.util.JsonUtil;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nowy on 2017/11/3.
 * URL操作类，主要用于与HTML5的URL交互
 * 支持两种格式解析
 * app://share?userId=10010&data='http://shareUrl.com?a=b'
 * app://share?{...}
 *
 * URI结构：[scheme:][//authority][path][?query][#fragment]
 * authority = host:port
 */

public class UrlOperation {
    public static final String TAG = UrlOperation.class.getSimpleName();
    private static final String CONSTANTS_SPLICING = "#c_param_";
    private static final String FORMAT_CONSTANTS_SPLICING = CONSTANTS_SPLICING +"%d";//用来替换字符串的格式
    private Map<String, String> mParamMap = new LinkedHashMap<>();
    private Uri mBaseUri;
    private String mBaseUrl;
    public UrlOperation(String url) {
        this.mBaseUrl = url;
        this.mBaseUri = Uri.parse(url);
    }

    public Uri getBaseUri() {
        return mBaseUri;
    }

    //    String className = uri.getHost();
//    String param = uri.getQuery();
//    String port = uri.getPort() + "";



    public String getSchemeSpecificPart(){
        return mBaseUri == null ? null :  this.mBaseUri.getSchemeSpecificPart();
    }



    public String getScheme(){
        return mBaseUri == null ? null :  this.mBaseUri.getScheme();
    }


    public String getAuthority(){
        return mBaseUri == null ? null :  this.mBaseUri.getAuthority();
    }

    public String getHost(){
        return mBaseUri == null ? null :  this.mBaseUri.getHost();
    }

    public int getPort(){
        return mBaseUri == null ? -1 :  this.mBaseUri.getPort();
    }


    public String getQuery(){
        return mBaseUri == null ? null :  this.mBaseUri.getQuery();
    }

    public String getQueryParameter(String key){
        return mBaseUri == null ? null : this.mBaseUri.getQueryParameter(key);
    }



    public String getPath(){
        return mBaseUri == null ? null : this.mBaseUri.getPath();
    }


    /**
     * 不带斜杠的路径名称
     * @return
     */
    public String getPathNotSeparator(){
        if(!TextUtils.isEmpty(getPath())){
           return getPath().replace("/", "");
        }


        return null;
    }


    /**
     * 解析出url参数中的键值对
     * 如 "index.php?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @return  url请求参数部分
     */
    public Map<String, Object> getRequestParam(){
        Map<String, Object> mapRequest = new HashMap<>();
        if(mBaseUri == null) return mapRequest;
        String strUrlParam = mBaseUri.getQuery();
        if(strUrlParam==null){
            return mapRequest;
        }
        if(isJson(strUrlParam)){
            mapRequest = json2Map(strUrlParam);
        }else{
            mapRequest = urlParam2Map(strUrlParam);
        }
        return mapRequest;
    }


    /**
     * URL的参数转化为map
     * @param urlParam
     * @return
     */
    private Map<String, Object> urlParam2Map(String urlParam){
        Map<String, Object> mapRequest = new HashMap<>();
        String paramStr = replaceParams(urlParam);
        String[] arrSplit = paramStr.split("[&]");
        for(String strSplit:arrSplit){
            String[] arrSplitEqual;
            arrSplitEqual= strSplit.split("[=]");
            //解析出键值
            if(arrSplitEqual.length>1){
                String key = getRealParam(urlParam,arrSplitEqual[0]);
                String value = getRealParam(urlParam,arrSplitEqual[1]);
                mapRequest.put(key, value);
            }else{
                if(!TextUtils.isEmpty(arrSplitEqual[0])){
                    String key = getRealParam(urlParam,arrSplitEqual[0]);
                    mapRequest.put(key, "");
                }
            }
        }
        return mapRequest;
    }


    /**
     * JSON数据转map
     */
    private Map<String, Object> json2Map(String json){
        return JsonUtil.parseJson2Map(json);
    }

    /**
     * 修改参数字符串
     * @param strUrlParam
     * @return
     */
    private String replaceParams(String strUrlParam){
        Pattern pattern = Pattern.compile("'(.*?)(?<![^\\\\]\\\\)'");
        Matcher matcher = pattern.matcher(strUrlParam);
        mParamMap.clear();
        String tmp = strUrlParam;
        while(matcher.find()){
            String value = matcher.group();
            int index = strUrlParam.indexOf(value);
            String key = replaceParam(index);
            mParamMap.put(key,value);
            tmp = tmp.replace(value,key);
        }
        Logger.t(TAG).e("UrlOperation replaceParams tmp:"+tmp);
//        LogUtils.e("matcher strUrlParam:"+strUrlParam);
        return tmp;
    }


    /**
     * 根据“{}”判断是否JSON，可能比较笼统
     * 暂时不校验“[]”类型的JSON
     * @param urlParam
     * @return
     */
    private boolean isJson(String urlParam){
        if(TextUtils.isEmpty(urlParam)
                || !urlParam.startsWith("{")
                || !urlParam.endsWith("}")) return false;
        return true;
    }


    /**
     * 根据index生成规格字符串
     * 用于生成替换URL里面单引号内容的字符串
     * 格式含义：[#c_param_10]：参数10是URL的参数部分里面的起始位置
     * @param index
     * @return
     */
    private String replaceParam(int index){
        String newParam  = String.format(Locale.CHINA,FORMAT_CONSTANTS_SPLICING,index);
        return newParam;
    }


    /**
     * 获取真实的参数内容
     * 修复此类型URL无法解析参数BUG:http://www.baidu.com?data={"url":"www.qq.com"}
     * @param strUrlParam
     * @param param
     * @return
     */
    private String getRealParam(String strUrlParam, String param){
        if(mParamMap == null ) return param;

        String value = param;
        for(String key : mParamMap.keySet()){
            if(param.contains(key)){
                String realValue = mParamMap.get(key);
                int index = strUrlParam.indexOf(realValue);
                int paramIndex = ConvertUtil.stringToInt(key.replace(CONSTANTS_SPLICING,""));
                if(index == paramIndex){
                    value = value.replace(key,realValue);
                }
            }
        }

//        String value = mParamMap.get(param);
//        int index = strUrlParam.indexOf(value);
//        int paramIndex = ConvertUtil.stringToInt(param.replace(CONSTANTS_SPLICING,""));
//        if(index == paramIndex) return value;
        return value;
    }

    @Override
    public String toString() {
        return "scheme: "+getScheme()
                +"\nhost: "+getHost()
                +"\nauthority: "+getAuthority()
//                +"\nschemeSpecificPart: "+getSchemeSpecificPart()
                +"\nparam: "+getQuery()
                +"\nport: "+getPort();
    }
}
