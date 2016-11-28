## JavaScript笔记（四）

### 事件
#### 事件冒泡和事件捕获  
事件冒泡即事件开始时由最具体的元素接收，然后逐级向上传播到较为不具体的节点。  
与事件冒泡相反，事件捕获中，最具体的元素会在最后接收到事件。  

#### 事件绑定
语法：
`target.addEventListener(type, listener[, useCapture]);`    
`type`    
String，表示监听的事件类型（如click为点击事件）  
`listener`  
Function，当监听的事件类型触发时，会执行的函数  
`useCapture`  
Boolean，为true的情况下会在捕获阶段触发listener，为false的情况下在冒泡阶段触发listener，默认为false。    
由于事件捕获在低版本浏览器下并不支持。所以，一般来说，我们还是采用事件冒泡的情况比较多。  

#### event  
当事件发生时， event 对象就会被创建并依次传递给事件监听器。  
在处理函数（即前文提到的listenr）中，将event对象作为第一个参数参数，可以访问 DOM Event 接口。  
常用属性  
`event.target`
目标元素，即是你当前点击的那个元素  
`event.currentTarget`
正在处理的那个元素，即是你绑定的元素  
`event.type`
事件的类型  
常用方法  
`event.stopPropagation()`  
阻止事件流的传播。即是不会再有下一步的冒泡或者捕获了。
`event.preventDefault()`  
阻止默认事件，比如点击 a 标签会跳转到另外一个页面，那么当执行了该方法之后，就不会进行页面的跳转了。  

### Window
Window 对象表示浏览器中打开的窗口。  
如果文档包含框架（frame 或 iframe 标签），浏览器会为 HTML 文档创建一个 window 对象，并为每个框架创建一个额外的 window 对象。  
注意：没有应用于 window 对象的公开标准，不过所有浏览器都支持该对象。  

  