## JavaScript笔记（三）
### 函数

#### 理解函数
Javascript函数的参数与大多数其他语言中的函数的参数不同。Javascript函数不介意传递进来多少个参数，也不介意传递进来的参数是什么数据类型。  
之所以会这样，原因是Javascript中的参数在内部是用一个数组来表示的。函数接收到的始终是这个数组，而不关心这个数组包含多少参数。  
实际上，在函数体内可以通过arguments对象来访问这个参数数组，从而获取传递给函数的每一个参数。  
其实arguments对象只是与数组类似（它并不是Array的实例），它可以使用方括号来访问它的每一个元素（第一个元素是arguments[0],第二个元素是arguments[1],以此类推），使用length属性来确定传进来多少个参数。  

可以利用这一点让函数能够接受任意个参数并分别实现适当的功能。

所以，可以得到另一个结论：**js的函数没有重载**。  

#### 函数声明与函数表达式
有两种写法：

函数声明：

	function add(a, b) {
	    return a + b;
	}


函数表达式：

	var add = function(a, b) {
	    return a + b;
	}

实际上，解析器在向执行环境中加载数据时，对函数声明和函数表达式并非一视同仁。解析器会率先读取函数声明，并使其在执行任何代码之前可用（可以访问）,这称为一种函数声明提前的过程；至于函数表达式，则必须等到解析器执行到它所在的代码行，才会真正被解释执行。

#### 函数作参数传递
function也是一种object。所以可以作为普通对象进行传递。  
要访问函数的指针而不执行函数的话，必须去掉函数后面那对圆括号。  
也可以作为返回值，return一个function即可。  


	function add(num){
	   return num + 10;
	}
	
	function greeting(f,num){
		console.log(f(num))
	}
	
	greeting(add,10);
	20


#### arguments

在函数内部有两个特殊对象分别是arguments和this。  
arguments前面已经提到过，它是一个类数组的对象，包含着传入函数中的所有参数。虽然arguments的主要用途是保存函数参数，但是这个对象还有一个名叫callee的属性，该属性是一个指针，指向拥有这个arguments对象的函数，请看下面非常经典的阶乘函数。


	function factorial(num) {
	    if (num <= 1) {
	        return 1;
	    } else {
	        return num * factorial(num - 1);
	    }
	}

定义阶乘函数一般都要用到递归算法；如上面代码所示，在函数有名字，而且名字以后也不会变的情况下，这样定义没有问题。但是问题是这个函数的执行与函数名factorial紧紧耦合在了一起。为了消除这种紧密耦合的现象，可以像下面这样使用arguments.callee。


	function factorial(num) {
	    if (num <= 1) {
	        return 1;
	    } else {
	        return num * arguments.callee(num - 1);
	    }
	}

在这个重写后的factorial()函数体内，没有再引用函数名factorial。这样，无论引用函数时使用的是什么名字，都可以保证正常完成递归调用。例如:

	
	var trueFactorial = factorial;
	factorial = function() {
	    return 0;
	}
	
	alert(trueFactorial(5));   // 120
	alert(factorial(5));        // 0

在此，变量trueFactorial获得了factorial的值，实际上是在另一个位置上保存了一个函数的指针。然后，我们又将一个返回了0的函数赋值给factorial变量。如果将原来的factorial()那样不使用arguments.callee，调用trueFactorial会返回0.可是，在解除了函数体内的代码与函数名的耦合状态后，trueFactorial()仍能正常地计算阶乘；至于factorial()，它现在只是个返回0的函数。  

#### this

在JavaScript中，函数的this关键字的行为与其他语言相比有很多不同。在绝大多数情况下，**函数的调用方式决定了this的值**。this不能在执行期间被赋值，在每次函数被调用时this的值也可能会不同。ES5引入了bind方法来设置函数的this值，而不用考虑函数如何被调用的。  

- 全局上下文  
在全局运行上下文中（在任何函数体外部），this 指代全局对象
	
		console.log(this.document === document); // true
	
		// 在浏览器中，全局对象为 window 对象：
		console.log(this === window); // true
		
		this.a = 37;
		console.log(window.a); // 37
	

- 函数上下文  
在函数内部，this的值取决于函数是如何调用的。
直接调用
		
		function f1(){
		  return this;
		}
		
		f1() === window; // true
	在上面的例子中，this的值不是由函数调用设定。this 的值总是一个对象且默认为全局对象。
  
- 对象方法中的 this  
当以对象里的方法的方式调用函数时，它们的 this 是调用该函数的对象. 下面的例子中，当 o.f() 被调用时，函数内的this将绑定到o对象。

		var o = {
		  prop: 37,
		  f: function() {
		    return this.prop;
		  }
		};
		
		console.log(o.f()); // logs 37

注意，在何处或者如何定义调用函数完全不会影响到this的行为。在上一个例子中，我们在定义o的时候为其成员f定义了一个匿名函数。但是，我们也可以首先定义函数然后再将其附属到o.f。这样做this的行为也一致：

	
	```
	var o = {prop: 37};
	
	function independent() {
	  return this.prop;
	}
	
	o.f = independent;
	
	console.log(o.f()); // logs 37
	```
这说明this的值只与函数 f 作为 o 的成员被调用有关系。  
类似的，this 的绑定只受**最靠近的成员引用**的影响。在下面的这个例子中，我们把一个方法g当作对象o.b的函数调用。在这次执行期间，函数中的this将指向o.b。事实上，这与对象本身的成员没有多大关系，最靠近的引用才是最重要的。  
	
	```
	o.b = {
	  g: independent,
	  prop: 42
	};
	console.log(o.b.g()); // logs 42
	```

- 原型链中的 this  
相同的概念在定义在原型链中的方法也是一致的。如果该方法存在于一个对象的原型链上，那么this指向的是调用这个方法的对象，表现得好像是这个方法就存在于这个对象上一样。
	
	
		var o = {
		  f : function(){
		    return this.a + this.b;
		  }
		};
		var p = Object.create(o);
		p.a = 1;
		p.b = 4;
		
		console.log(p.f()); // 5
	在这个例子中，对象p没有属于它自己的f属性，它的f属性继承自它的原型。但是这对于最终在o中找到f属性的查找过程来说没有关系；查找过程首先从p.f的引用开始，所以函数中的this指向p。也就是说，因为f是作为p的方法调用的，所以它的this指向了p。这是JavaScript的原型继承中的一个有趣的特性。

- getter 与 setter 中的 this  
再次，相同的概念也适用函数作为一个 getter 或者 一个setter调用。作为getter或setter函数都会绑定 this 到从设置属性或得到属性的那个对象。
	
	
		function modulus(){
		  return Math.sqrt(this.re * this.re + this.im * this.im);
		}
		
		var o = {
		  re: 1,
		  im: -1,
		  get phase(){
		    return Math.atan2(this.im, this.re);
		  }
		};
		
		Object.defineProperty(o, 'modulus', {
		  get: modulus, enumerable:true, configurable:true});
		
		console.log(o.phase, o.modulus); // logs -0.78 1.4142
	
- 构造函数中的 this  
当一个函数被作为一个构造函数来使用（使用new关键字），它的this与即将被创建的新对象绑定。  
注意：当构造器返回的默认值是一个this引用的对象时，可以手动设置返回其他的对象，如果返回值不是一个对象，返回this。  

		function C(){
		  this.a = 37;
		}
		
		var o = new C();
		console.log(o.a); // logs 37
		
		
		function C2(){
		  this.a = 37;
		  return {a:38};
		}
		
		o = new C2();
		console.log(o.a); // logs 38

在最后的例子中（C2），因为在调用构造函数的过程中，手动的设置了返回对象，与this绑定的默认对象被取消（本质上这使得语句“this.a = 37;”成了“僵尸”代码，实际上并不是真正的“僵尸”，这条语句执行了但是对于外部没有任何影响，因此完全可以忽略它）。  
 
- call 和 apply  
当一个函数的内部使用了this关键字时，通过从Function对象的原型中继承的call()方法和apply()方法调用这个函数时，this的值可以绑定到一个指定的对象上。   


		function add(c, d){
		  return this.a + this.b + c + d;
		}
		
		var o = {a:1, b:3};
		
		// The first parameter is the object to use as 'this', subsequent parameters are passed as
		// arguments in the function call
		add.call(o, 5, 7); // 1 + 3 + 5 + 7 = 16
		
		// The first parameter is the object to use as 'this', the second is an array whose
		// members are used as the arguments in the function call
		add.apply(o, [10, 20]); // 1 + 3 + 10 + 20 = 34
	
使用 call 和 apply 函数的时候要注意，如果传递的 this 值不是一个对象，JavaScript 将会尝试使用内部 ToObject 操作将其转换为对象。因此，如果传递的值是一个原始值比如 7 或 'foo' ，那么就会使用相关构造函数将它转换为对象，所以原始值 7 通过new Number(7)被转换为对象，而字符串'foo'使用 new String('foo') 转化为对象，例如：

	
		unction bar() {
		  console.log(Object.prototype.toString.call(this));
		}
		
		bar.call(7); // [object Number]
	
- bind 方法  
ECMAScript 5 引入了 Function.prototype.bind。调用f.bind(someObject)会创建一个与f具有相同函数体和作用域的函数，但是在这个新函数中，this将**永久地被绑定**到了bind的第一个参数，无论这个函数是如何被调用的。
	
	
		function f(){
		  return this.a;
		}
		
		var g = f.bind({a:"azerty"});
		console.log(g()); // azerty
		
		var o = {a:37, f:f, g:g};
		console.log(o.f(), o.g()); // 37, azerty

- DOM事件处理函数中的 this  
当函数被用作事件处理函数时(以addEventListener绑定事件)，它的this指向触发事件的元素。

	
		// 被调用时，将关联的元素变成蓝色
		function bluify(e){
		  console.log(this === e.currentTarget); // 总是 true
		
		  // 当 currentTarget 和 target 是同一个对象是为 true
		  console.log(this === e.target);
		  this.style.backgroundColor = '#A5D9F3';
		}
		
		// 获取文档中的所有元素的列表
		var elements = document.getElementsByTagName('*');
		
		// 将bluify作为元素的点击监听函数，当元素被点击时，就会变成蓝色
		for(var i=0 ; i<elements.length ; i++){
		  elements[i].addEventListener('click', bluify, false);
		}
	
- 内联事件处理函数中的 this  
当代码被内联处理函数调用时，它的this指向监听器所在的DOM元素：
	
		<button onclick="alert(this.tagName.toLowerCase());">
		  Show this
		</button>
	
上面的alert会显示button。

#### 作用域
- 函数内部，未声明（即没有var）而直接赋值的变量，是全局作用域。  

- 作用域链  
因为全局变量总是存在于运行期上下文作用域链的最末端，因此在标识符解析的时候，查找全局变量是最慢的。所以，在编写代码的时候应尽量少使用全局变量，尽可能使用局部变量。一个好的经验法则是：如果一个跨作用域的对象被引用了一次以上，则先把它存储到局部变量里再使用。例如下面的代码：

		function changeColor(){
		    document.getElementById("btnChange").onclick=function(){
		        document.getElementById("targetCanvas").style.backgroundColor="red";
		    };
		}

这个函数引用了两次全局变量document，查找该变量必须遍历整个作用域链，直到最后在全局对象中才能找到。这段代码可以重写如下：


		function changeColor(){
		    var doc=document;
		    doc.getElementById("btnChange").onclick=function(){
		        doc.getElementById("targetCanvas").style.backgroundColor="red";
		    };
		}

这段代码比较简单，重写后不会显示出巨大的性能提升，但是如果程序中有大量的全局变量被从反复访问，那么重写后的代码性能会有显著改善。  

#### 闭包
- 原理  

闭包是一种特殊的对象。它由两部分构成：函数，以及创建该函数的环境。环境由闭包创建时在作用域中的任何局部变量组成。  

	function makeFunc()
	{
	    var name = 'yunsheng';
	    function theFunc(){
	    console.log(name);
		}
	return theFunc;
	}


makeFunc就是一个闭包，它由函数theFunc和函数的环境--局部变量name组成。  

	function mySum(x){
	return function(y){
	        return x + y;
	    }
	}
	
	var add5 = mySum(5);
	var add10 = mySum(10);
	add5(2);   // 7
	add10(2);  // 12

这个例子更明显，add5 和 add10 都是闭包。它们共享相同的函数定义，但是保存了不同的环境。在 add5 的环境中，x 为 5。而在 add10 中，x 则为 10。  


- 闭包的实际使用案例。  

1. 在 Web 中，您可能想这样做的情形非常普遍。大部分我们所写的 Web JavaScript 代码都是事件驱动的 — 定义某种行为，然后将其添加到用户触发的事件之上（比如点击或者按键）。我们的代码通常添加为回调：响应事件而执行的函数。  
以下是一个实际的示例：假设我们想在页面上添加一些可以调整字号的按钮。一种方法是以像素为单位指定 body 元素的 font-size，然后通过相对的 em 单位设置页面中其它元素（例如页眉）的字号：

		
			body {
			  font-family: Helvetica, Arial, sans-serif;
			  font-size: 12px;
			}
			
			h1 {
			  font-size: 1.5em;
			}
			h2 {
			  font-size: 1.2em;
			}	
我们的交互式的文本尺寸按钮可以修改 body 元素的 font-size 属性，而由于我们使用相对的单位，页面中的其它元素也会相应地调整。
以下是 JavaScript：

		
		function makeSizer(size) {
		  return function() {
		    document.body.style.fontSize = size + 'px';
		  };
		}
		
		var size12 = makeSizer(12);
		var size14 = makeSizer(14);
		var size16 = makeSizer(16);	
size12，size14 和 size16 为将 body 文本相应地调整为 12，14，16 像素的函数。我们可以将它们分别添加到按钮上（这里是链接）。如下所示：

		
		document.getElementById('size-12').onclick = size12;
		document.getElementById('size-14').onclick = size14;
		document.getElementById('size-16').onclick = size16;
		
		<a href="#" id="size-12">12</a>
		<a href="#" id="size-14">14</a>
		<a href="#" id="size-16">16</a>
		
2. 用闭包模拟私有方法  
诸如 Java 在内的一些语言支持将方法声明为私有的，即它们只能被同一个类中的其它方法所调用。
对此，JavaScript 并不提供原生的支持，但是可以使用闭包模拟私有方法。私有方法不仅仅有利于限制对代码的访问：还提供了管理全局命名空间的强大能力，避免非核心的方法弄乱了代码的公共接口部分。  
下面的示例展现了如何使用闭包来定义公共函数，且其可以访问私有函数和变量。这个方式也称为 模块模式（module pattern）：

		
		var Counter = (function() {
		  var privateCounter = 0;
		  function changeBy(val) {
		    privateCounter += val;
		  }
		  return {
		    increment: function() {
		      changeBy(1);
		    },
		    decrement: function() {
		      changeBy(-1);
		    },
		    value: function() {
		      return privateCounter;
		    }
		  }
		})();
		
		console.log(Counter.value()); /* logs 0 */
		Counter.increment();
		Counter.increment();
		console.log(Counter.value()); /* logs 2 */
		Counter.decrement();
		console.log(Counter.value()); /* logs 1 */
		
这里有很多细节。在以往的示例中，每个闭包都有它自己的环境；而这次我们只创建了一个环境，为三个函数所共享：Counter.increment，Counter.decrement 和 Counter.value。  
该共享环境创建于一个匿名函数体内，该函数一经定义立刻执行。环境中包含两个私有项：名为 privateCounter 的变量和名为 changeBy 的函数。 这两项都无法在匿名函数外部直接访问。必须通过匿名包装器返回的三个公共函数访问。   
这三个公共函数是共享同一个环境的闭包。多亏 JavaScript 的词法范围的作用域，它们都可以访问 privateCounter 变量和 changeBy 函数。  
您应该注意到了，我们定义了一个匿名函数用于创建计数器，然后直接调用该函数，并将返回值赋给 Counter 变量。也可以将这个函数保存到另一个变量中，以便创建多个计数器。  
		
		```
		var makeCounter = function() {
		  var privateCounter = 0;
		  function changeBy(val) {
		    privateCounter += val;
		  }
		  return {
		    increment: function() {
		      changeBy(1);
		    },
		    decrement: function() {
		      changeBy(-1);
		    },
		    value: function() {
		      return privateCounter;
		    }
		  }
		};
		
		var Counter1 = makeCounter();
		var Counter2 = makeCounter();
		console.log(Counter1.value()); /* logs 0 */
		Counter1.increment();
		Counter1.increment();
		console.log(Counter1.value()); /* logs 2 */
		Counter1.decrement();
		console.log(Counter1.value()); /* logs 1 */
		console.log(Counter2.value()); /* logs 0 */
		
		```
请注意两个计数器是如何维护它们各自的独立性的。每次调用 makeCounter() 函数期间，其环境是不同的。每次调用中， privateCounter 中含有不同的实例。
这种形式的闭包提供了许多通常由面向对象编程所享有的益处，尤其是数据隐藏和封装。

- 如果不是因为某些特殊任务而需要闭包，在没有必要的情况下，在其它函数中创建函数是不明智的，因为闭包对脚本性能具有负面影响，包括处理速度和内存消耗。  


