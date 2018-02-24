##说明
这是一个JavaScript和Android webView原生交互的代码示例

主要采用了重写**WebViewClient**和**WebChromeClient**两种方式实现。


> WebViewClient主要重写了shouldOverrideUrlLoading方法进行URL拦截处理

> WebChromeClient主要重写了较为少用的onJsPrompt方法，对JS的window.prompt(uri, "")进行捕获，
> 此例子中WebChromeClient交互实现了异步响应给js端的

