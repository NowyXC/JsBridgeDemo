(function (win) {
        var hasOwnProperty = Object.prototype.hasOwnProperty;
        var JSBridge = win.JSBridge || (win.JSBridge = {});
        var JS_BRIDGE_PROTOCOL = 'app';
        var Inner = {
                callbacks: {},
                call: function (obj, method, params, callback) {
                        var port = Util.getPort();
                        this.callbacks[port] = callback;
                        var uri=Util.getUri(obj,method,params,port);
                        window.prompt(uri, "");
                    },
                onFinish: function (port, jsonObj){
                        console.log("执行onFinish");
                        var callback = this.callbacks[port];
                        if(callback != null){
                           callback(jsonObj);
                            delete this.callbacks[port];
                           }

                        },
                    };
        var Util = {
            getPort:function () {
                    var time = new Date().getTime() +"";
                    time =  time.substring(5);
                    return time;
                },
            getUri:function(obj, method, params, port){
                    params = this.getParam(params);
                    var uri = JS_BRIDGE_PROTOCOL + '://' + obj + ':' + port;
                    if (method !== null && method !== undefined && method !== '') {
                        uri = uri + "/" +method;
                    }

                    uri = uri + '?' + params;
                    return uri;
                },
            getParam:function(obj){
                    if (obj & typeof obj === 'object') {
                        return JSON.stringify(obj);
                    } else {
                        return obj || '';
                    }
                }
        };
        for (var key in Inner) {
            if (!hasOwnProperty.call(JSBridge, key)) {
                JSBridge[key] = Inner[key];
            }
        }
    })(window);