package com.nowy.JsBridgeDemo.WebBridge;

import android.text.TextUtils;
import android.webkit.WebView;

import com.nowy.JsBridgeDemo.urlUtil.UrlOperation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nowy on 2018/2/23.
 */

public class JSBridge {
    private static Map<String,HashMap<String,Method>> exposedMethods = new HashMap<>();
    private static final String EXPOSED_VERSION_DEF = "version_1_0";
    private static final String SCHEME_PREFIX = "app";//检测协议头
    /**
     * 注册相关类到缓存的Map中，保存JS交互集合
     * @param exposedName 调用相关的KEY值
     * @param clazz 包含JS交互方法的的类
     */
    public static void register(String exposedName, Class <? extends IBridge> clazz) {
        if (!exposedMethods.containsKey(exposedName)) {
            try {
                exposedMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void register(Class <? extends IBridge> clazz) {
        try {
            exposedMethods.put(EXPOSED_VERSION_DEF, getAllMethod(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static HashMap<String,Method> getAllMethod(Class injectedCls) throws Exception {
        HashMap<String,Method> mMethodsMap = new HashMap<>();
        Method[] methods = injectedCls.getDeclaredMethods();
        for (Method method : methods) {
            String name =  method.getName();
            if (!Modifier.isPublic(method.getModifiers()) ||  name == null) {//方法名为空或者非公有方法跳过
                continue;
            }
            JsMethod jsMethod = getJsMethod(method);
            if(jsMethod != null){
                if(!TextUtils.isEmpty(jsMethod.methodName())){
                    name = jsMethod.methodName();
                }
                mMethodsMap.put(name, method);
            }
        }
        return mMethodsMap;
    }



    //根据注解判断是否JS交互方法
//    private static boolean isJsMethod(Method method){
//        if(method == null) return false;
//        JsMethod methodAnnotation = method.getAnnotation(JsMethod.class);
//        return methodAnnotation != null;
//    }
//


    private static JsMethod getJsMethod(Method method){
        if(method == null) return null;
        return method.getAnnotation(JsMethod.class);
    }

    //app://className:123?methodName?param
    //app://methodName:123?param
    public static boolean callJava(WebView view, String data){
        if(TextUtils.isEmpty(data) || !data.startsWith(SCHEME_PREFIX)){
            return false;
        }
        UrlOperation operation = new UrlOperation(data);
        String className = operation.getHost();
        String port = String.valueOf(operation.getPort());
        String methodName = operation.getPathNotSeparator();
        Map<String,Object> param = operation.getRequestParam();

        return executeMethod(view,className,methodName,port,param);
    }

    /**
     * 在指定的方法集合里查找方法，并执行。
     * 如果方法不存在于指定的方法集合中，或者methodName为空，调用executeMethodDef(...)
     *
     * @param view
     * @param className URI上面的存在className和methodName（host和part同时存在）,则去特定的方法集合(exposedMethods.get(className))中查找
     * @param methodName
     * @param port
     * @param param
     * @return
     */
    private static boolean executeMethod(WebView view,String className,String methodName,String port,Map<String,Object> param){
        if (exposedMethods.containsKey(className)) {//查询已注册的交互map
            HashMap<String, Method> methodHashMap = exposedMethods.get(className);
            if (methodHashMap != null && methodHashMap.size() != 0) {
                Method method = null;
                if(methodHashMap.containsKey(methodName)){//如果存在methodName，优先采用
                    method = methodHashMap.get(methodName);
                }
                if (method != null) {
                    try {//JS方法暂时只处理，，methodName/className(WebView,Map<String,Object>,JSHandler)
                        method.invoke(null, view, param, new JSHandler(view, port));
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /**
         * //如果methodName为空，采用className代替
         * 兼容eg:
         * app://className:port/methodName?param
         * app://methodName:port?param
         */
        return executeMethodDef(view,TextUtils.isEmpty(methodName) ? className : methodName,port,param);
    }


    /**
     * 在默认的方法集合里查找方法，并执行
     * @param view
     * @param methodName
     * @param port
     * @param param
     * @return
     */
    private static boolean executeMethodDef(WebView view,String methodName,String port,Map<String,Object> param){
//        boolean isExecuted = false;
        if (exposedMethods.containsKey(EXPOSED_VERSION_DEF)) {//查询已注册的交互map
            HashMap<String, Method> methodHashMap = exposedMethods.get(EXPOSED_VERSION_DEF);
            if (methodHashMap != null && methodHashMap.size() != 0) {
                Method method = null;
                if(!TextUtils.isEmpty(methodName) && methodHashMap.containsKey(methodName)){
                    method = methodHashMap.get(methodName);
                }
                if (method != null) {
                    try {//JS方法暂时只处理，，methodName/className(WebView,Map<String,Object>,JSHandler)
                        method.invoke(null, view, param, new JSHandler(view, port));
//                        isExecuted =  true;
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
