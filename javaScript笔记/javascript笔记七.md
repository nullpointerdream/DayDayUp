## javascript笔记七

### ajax
#### XMLHttpRequest
js能实现无需装载整个页面便能向服务器发送请求，靠的就是这个XMLHttpRequest。  
XMLHttpRequest 是一个 JavaScript 对象。  
`var req = new XMLHttpRequest();`  

- 常用属性  
	- onreadystatechange  
	Function，当readyState属性改变时会进行调用。回调函数会在UI线程中调用。   
	- readyState  
	unsigned short，请求的5种状态  
	0 | UNSENT (未打开) | open()方法还未被调用.   
	1 | OPENED (未发送) | send()方法还未被调用.  
	2 | HEADERS_RECEIVED (已获取响应头)| send()方法已经被调用, 响应头和响应状态已经返回.    
	3 | LOADING (正在下载响应体) | 响应体下载中; responseText中已经获取了部分数据.   
	4 | DONE (请求完成) | 整个请求过程已经完毕.  
	- status
	unsigned short ，只读该请求的响应状态码 (例如, 状态码200 表示一个成功的请求)  
	- responseText
	DOMString， 只读此次请求的响应为文本，或是当请求未成功或还未发送时为 null。只读。

- 常用方法
	- req.abort()
	如果请求已经被发送,则立刻中止请求.
	- req.open()
	初始化一个请求. 该方法用于JavaScript代码中;如果是本地代码, 使用openRequest()方法代替.  
		- 参数
			- method
			请求所使用的HTTP方法; 例如 "GET", "POST", "PUT", "DELETE"等. 如果下个参数是非HTTP(S)的URL,则忽略该参数.
			- url
			该请求所要访问的URL
			- async
			一个可选的布尔值参数，默认为true,意味着是否执行异步操作，如果值为false,则send()方法不会返回任何东西，直到接受到了服务器的返回数据。如果为值为true，一个对开发者透明的通知会发送到相关的事件监听者。这个值必须是true,如果multipart 属性是true，否则将会出现一个意外。
			- user
			用户名,可选参数,为授权使用;默认参数为空string.
			- password
			密码,可选参数,为授权使用;默认参数为空string.
- req.overrideMimeType()
重写由服务器返回的MIME type。这个可用于, 例如，强制把一个响应流当作“text/xml”来处理和解析,即使服务器没有指明数据是这个类型。注意，这个方法必须在send()之前被调用。
- req.send()
发送请求. 如果该请求是异步模式(默认),该方法会立刻返回. 相反,如果请求是同步模式,则直到请求的响应完全接受以后,该方法才会返回.
所有相关的事件绑定必须在调用send()方法之前进行.
- req.setRequestHeader()
给指定的HTTP请求头赋值.在这之前,你必须确认已经调用 open() 方法打开了一个url.
	- 参数
		- header
		将要被赋值的请求头名称.
		- value
		给指定的请求头赋的值.
		
#### 发送请求 
XMLHttpRequest 让发送一个HTTP请求变得非常容易。你只需要简单的创建一个请求对象实例，打开一个URL，然后发送这个请求。当传输完毕后，结果的HTTP状态以及返回的响应内容也可以从请求对象中获取。  

建立一个 XMLHttpRequest 的实例

```
var http_request;
if (window.XMLHttpRequest) { // 现代浏览器
    http_request = new XMLHttpRequest();
} else if (window.ActiveXObject) { // IE
    http_request = new ActiveXObject("Microsoft.XMLHTTP");
}
```

如果服务器的响应没有 XML mime-type header,某些Mozilla浏览器可能无法正常工作. 为了解决这个问题, 如果服务器响应的header不是text/xml,可以调用其它方法修改该header.

```
http_request = new XMLHttpRequest();
http_request.overrideMimeType('text/xml');
```

接下来要决定当收到服务器的响应后,需要做什么.这需要告诉HTTP请求对象用哪一个JavaScript函数处理这个响应.可以将对象的onreadystatechange属性设置为要使用的JavaScript的函数名,如下所示:  

```
http_request.onreadystatechange = nameOfTheFunction;
```
注意:在函数名后没有括号,也无需传递参数.
或者，你也可以使用一个匿名函数来描述那些要对服务器返回的响应内容所进行的操作,如下所示:

```
http_request.onreadystatechange = function(){
    // do the thing
};
```

在定义了如何处理响应后,就要发送请求了.可以调用HTTP请求类的open()和send()方法, 如下所示:

```
http_request.open('GET', 'http://www.example.org/some.file', true);
http_request.send(null);
```

#### 处理响应

首先，我们要去检查请求的状态，即是 readyState ，如果为4，就意味着一个完整的服务器响应已经收到了,您将可以处理该响应.

```
if (http_request.readyState == 4) {
    // everything is good, the response is received
} else {
    // still not ready
}
```
readyState的取值如下:
0 (未初始化)
1 (正在装载)
2 (装载完毕)
3 (交互中)
4 (完成)
接着,函数会检查HTTP服务器响应的状态值. 一般来说，我们着重处理为 200 的响应

```
if (http_request.status == 200) {
    // perfect!
} else {
    // there was a problem with the request,
    // for example the response may be a 404 (Not Found)
    // or 500 (Internal Server Error) response codes
}
```


#### 完整代码
```

function makeRequest(url, callback) {

    var http_request;

     // 创建 XMLHttpRequest 实例
    if (window.XMLHttpRequest) { // 现代浏览器
        http_request = new XMLHttpRequest();
        // 重写服务器的 MIME type
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    } else if (window.ActiveXObject) { // IE
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
        }
    }

     // 如果创建实例失败，则提示错误
    if (!http_request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    // 处理服务器响应
    http_request.onreadystatechange = function() {

        if (http_request.readyState == 4) {
            if (http_request.status == 200) {
                var data = http_request.responseText;
                callback && typeof callback == 'Function' && callback(data);
            } else {
                alert('There was a problem with the request.');
            }
        }

    };

    http_request.open('GET', url, true);
    http_request.send(null);
}
```