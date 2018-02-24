package com.nowy.JsBridgeDemo.urlInteractive;

import java.util.Map;

/**
 * Created by Nowy on 2017/11/3.
 * HTML5通过url方式与native进行交互的接口
 * 定义了5种处理模式
 *
 */

public interface IActionHandler {

    int ACTION_TEL_CALL = 1;//直接拨打电话
    int ACTION_TEL_CALLDIAL = 0;//拨号界面


    /**
     * 执行带app前缀的请求，action为{app://}后面带的方法
     * 例如: app://share?中的share
     * @param action
     * @param data
     */
    void handleApp(int action, Map data);

    /**
     * 拨打电话
     * @param action 直接拨打电话或者打开拨号窗口，注意：直接拨打电话需要权限
     * @param tel 电话号码
     */
    void handleTel(int action, String tel);

    /**
     * 刷新当前页面
     */
    void handleRefresh(String url);

    /**
     * 跳转首页。历史遗留API，如果要使用建议优先使用app://open
     */
    void handleJump(String url);



    /**
     * 默认处理
     */
    void handleDef(String url);


    /**
     * 这个方法是基础调用方法，在上面的每个调用后面都会继续调用此方法
     * 可以通过此方法实现其他类型的操作
     * @param url
     * @param schemeModel 协议头(http://、app://等)
     * @param action
     * @param data
     */
    void handle(String url, int schemeModel, int action, Map data);
}
