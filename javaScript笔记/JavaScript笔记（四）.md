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

#### 属性
- closed
返回窗口是否已被关闭。
- defaultStatus
设置或返回窗口状态栏中的默认文本。
- document
对 Document 对象的只读引用
- history 对 History
对象的只读引用。请参数 History 对象。
- innerheight
返回窗口的文档显示区的高度。
- innerwidth
返回窗口的文档显示区的宽度。
- length
设置或返回窗口中的框架数量。
- location
用于窗口或框架的 Location 对象
- name
设置或返回窗口的名称。
- Navigator
对 Navigator 对象的只读引用
- opener
返回对创建此窗口的窗口的引用。
- outerheight
返回窗口的外部高度。
- outerwidth
返回窗口的外部宽度。
- pageXOffset
设置或返回当前页面相对于窗口显示区左上角的 X 位置。
- pageYOffset
设置或返回当前页面相对于窗口显示区左上角的 Y 位置。
- parent
返回父窗口。
- Screen
对 Screen 对象的只读引用
- self
返回对当前窗口的引用。等价于 Window 属性。
- status
设置窗口状态栏的文本。
- top
返回最顶层的先辈窗口。
- window
window 属性等价于 self 属性，它包含了对窗口自身的引用。
- screenLeft、screenTop、screenX、screenY
只读整数。声明了窗口的左上角在屏幕上的的 x 坐标和 y 坐标。IE、Safari 和 Opera 支持 screenLeft 和 screenTop，而 Firefox 和 Safari 支持 screenX 和 screenY。

#### 方法
- window.alert()
显示带有一段消息和一个确认按钮的警告框。
- window.blur()
把键盘焦点从顶层窗口移开。
- window.clearInterval()
取消由 setInterval() 设置的 timeout。
- window.clearTimeout()
取消由 setTimeout() 方法设置的 timeout。
- window.close()
关闭浏览器窗口。
- window.confirm()
显示带有一段消息以及确认按钮和取消按钮的对话框。
- window.createPopup()
创建一个 pop-up 窗口。
- window.focus()
把键盘焦点给予一个窗口。
- window.moveBy()
可相对窗口的当前坐标把它移动指定的像素。
- window.moveTo()
把窗口的左上角移动到一个指定的坐标。
- window.open()
打开一个新的浏览器窗口或查找一个已命名的窗口。
- window.print()
打印当前窗口的内容。
- window.prompt()
显示可提示用户输入的对话框。
- window.resizeBy()
按照指定的像素调整窗口的大小。
- window.resizeTo()
把窗口的大小调整到指定的宽度和高度。
- window.scrollBy()
按照指定的像素值来滚动内容。
- window.scrollTo()
把内容滚动到指定的坐标。
- window.setInterval()
按照指定的周期（以毫秒计）来调用函数或计算表达式。
- window.setTimeout()
在指定的毫秒数后调用函数或计算表达式。

#### Window 对象描述
Window 对象表示一个浏览器窗口或一个框架。在客户端 JavaScript 中，Window 对象是全局对象，所有的表达式都在当前的环境中计算。也就是说，要引用当前窗口根本不需要特殊的语法，可以把那个窗口的属性作为全局变量来使用。例如，可以只写 document，而不必写 window.document。  
同样，可以把当前窗口对象的方法当作函数来使用，如只写 alert()，而不必写 Window.alert()。  
除了上面列出的属性和方法，Window 对象还实现了核心 JavaScript 所定义的所有全局属性和方法。  
Window 对象的 window 属性和 self 属性引用的都是它自己。当你想明确地引用当前窗口，而不仅仅是隐式地引用它时，可以使用这两个属性。除了这两个属性之外，parent 属性、top 属性以及 frame[] 数组都引用了与当前 Window 对象相关的其他 Window 对象。  


### location
#### 属性
- hash
设置或返回从井号 (#) 开始的 URL（锚）。
- host
设置或返回主机名和当前 URL 的端口号。
- hostname
设置或返回当前 URL 的主机名。
- href
设置或返回完整的 URL。
- pathname
设置或返回当前 URL 的路径部分。
- port
设置或返回当前 URL 的端口号。
- protocol
设置或返回当前 URL 的协议。
- search
设置或返回从问号 (?) 开始的 URL（查询部分）。

#### 方法
- assign()
加载新的文档。
- reload()
重新加载当前文档。
- replace()
用新的文档替换当前文档。

### history
#### 属性
- length
返回浏览器历史列表中的 URL 数量。
#### 方法
- History.back()
加载 history 列表中的前一个 URL。
- History.forward()
加载 history 列表中的下一个 URL。
- History.go()
加载 history 列表中的某个具体页面。如参数-2表示后退2个  

### navigator
#### 属性
- appCodeName
返回浏览器的代码名。
- appMinorVersion
返回浏览器的次级版本。
- appName
返回浏览器的名称。
- appVersion
返回浏览器的平台和版本信息。
- browserLanguage
返回当前浏览器的语言。
- cookieEnabled
返回指明浏览器中是否启用 cookie 的布尔值。
- cpuClass
返回浏览器系统的 CPU 等级。
- onLine
返回指明系统是否处于脱机模式的布尔值。
- platform
返回运行浏览器的操作系统平台。
- systemLanguage
返回 OS 使用的默认语言。
- userAgent
返回由客户机发送服务器的 user-agent 头部的值。
- userLanguage
返回 OS 的自然语言设置。

#### Navigator 对象描述
Navigator 对象包含的属性描述了正在使用的浏览器。可以使用这些属性进行平台专用的配置。  
虽然这个对象的名称显而易见的是 Netscape 的 Navigator 浏览器，但其他实现了 JavaScript 的浏览器也支持这个对象。  
Navigator 对象的实例是唯一的，可以用 Window 对象的 navigator 属性来引用它。  

### screen
#### 属性
- availHeight
返回显示屏幕的高度 (除 Windows 任务栏之外)。
- availWidth
返回显示屏幕的宽度 (除 Windows 任务栏之外)。
- bufferDepth
设置或返回调色板的比特深度。
- colorDepth
返回目标设备或缓冲器上的调色板的比特深度。
- deviceXDPI
返回显示屏幕的每英寸水平点数。
- deviceYDPI
返回显示屏幕的每英寸垂直点数。
- fontSmoothingEnabled
返回用户是否在显示控制面板中启用了字体平滑。
- height
返回显示屏幕的高度。
- logicalXDPI
返回显示屏幕每英寸的水平方向的常规点数。
- logicalYDPI
返回显示屏幕每英寸的垂直方向的常规点数。
- pixelDepth
返回显示屏幕的颜色分辨率（比特每像素）。
- updateInterval
设置或返回屏幕的刷新率。
- width
返回显示器屏幕的宽度。


  