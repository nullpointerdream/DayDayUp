## javascript笔记（一）

### 变量

- 局部变量  
用 var 操作符定义的变量将成为定义该变量的作用域中的局部变量。也就是说，如果在函数中使用 var 定义一个变量，那么这个变量在函数退出后就会被销毁，例如：  
	
	```
	function test(){
 	var message = "hi"; // 局部变量
	}
	test();
	alert(message); // 错误！
	```
​ 这里，变量 message 是在函数中使用 var 定义的。当函数被调用时，就会创建该变量并为其赋值。而在此之后，这个变量又会立即被销毁，因此例子中的下一行代码就会导致错误。不过，可以像下面这 样省略 var 操作符，从而创建一个全局变量:

	```
	function test(){   
 	message = "hi"; // 全局变量
	}
	test();
	alert(message); // "hi"
	```
​ 这个例子省略了 var 操作符，因而 message 就成了全局变量。这样，只要调用过一次 test()函数，这个变量就有了定义，就可以在函数外部的任何地方被访问到。  
注：虽然省略 var 操作符可以定义全局变量，但这也不是我们推荐的做法。因为在局部作用域中定义的全局变量很难维护，而且如果有意地忽略了 var 操作符，也会由于相应变量不会马上就有定义而导致不必要的混乱。  

### 数据类型
JavaScript有5种原始类型(也称为基本数据类型)：Undefined、Null、Boolean、Number和String。还有1种复杂数据类型：Object。在JavaScript中Function、Arrary、Date...都是Object对象类型。  

null和undefined是不一样的。只声明未赋值的变量，是undefined的。
 
```
var m;
undefined
null == undefined;
true
null === undefined;
false
NaN != NaN;
true
NaN !== NaN;
true
```
有一个isNaN()函数，测试入参是否不能转为数字。

```
isNaN(true);
false
isNaN(false);
false
isNaN("");
false
isNaN("ss");
true
isNaN(null);
false
isNaN(undefined);
true
isNaN([]);
false
isNaN({});
true
```
可以看到，布尔型，空字符串，null，空数组{}。都是可以转为数字的。  
undefined，空对象是不能转为数字的。  

在JavaScript中Object类型是所有它的实例的基础。像我们知道的Array()、Date()、还有有意思的function()类型，都是从Object继承的。Object 类型所具有的任何属性和方法也同样存在于更具体的对象中。  
Object的每个实例都具有下列属性和方法：  
1.constructor：保存着用于创建当前对象的函数。  
2.hasOwnProperty(propertyName)：用于检查给定的属性在当前对象实例中（而不是在实例的原型中）是否存在。其中，作为参数的属性名（propertyName）必须以字符串形式指定（例如:o.hasOwnProperty("name")）  
3.isPrototypeOf(object)：用于检查传入的对象是否是传入对象的原型  
4.propertyIsEnumerable(propertyName)：用于检查给定的属性是否能够使用 for-in 语句（本章后面将会讨论）来枚举。  
5.toString()：返回对象的字符串表示。  
6.valueOf()：返回对象的字符串、数值或布尔值表示。  


### 转化为布尔
简单来说就是以下六个值转化结果为false,其他的值全部转换为true  
1. undefined  
2. null  
3. ""(空字符串)  
4. 0  
5. NaN  
6. false  
特别注意：所有对象的布尔值都是true   

```
Boolean({})
true
```
注意，js是区分大小写的，不要写错成boolean。

### 转换为数字
- Number()转换任意类型参数为数值
	
	```
	Number(true)
	1
	Number(false)
	0
	Number(undefined)
	NaN
	Number(null)
	0
	Number("")
	0
	Number("1.1")
	1.1
	Number("0xA")
	10       --------自动进行进制转换
	Number("2a")
	NaN
	Number({})
	NaN
	Number([])
	0
	```
	Number()转换对象：  
	1. 先调用对象自身的valueOf方法，如果该方法返回原始类型的值（数值、字符串和布尔值），则直接对该值使用Number方法，不再进行后续步骤。  
   2. 如果valueOf方法返回复合类型的值，再调用对象自身的toString方法，如果toString方法返回原始类型的值，则对该值使用Number方法，不再进行后续步骤。
   3. 如果toString方法返回的是复合类型的值，则报错。  
- parseInt()用于将字符串转换为整数

	```
	parseInt("1.2")
	1
	parseInt("1.7")
	1     -----------截断，非四舍五入
	parseInt("a222")
	NaN
	parseInt("2a")
	2     ----------匹配最长数
	parseInt("")
	NaN   ----------是NaN
	parseInt("0xA")
	10     ---------自动进制转换
	
	```
	
- parseFloat()将字符串转为小数

	```
	parseFloat("1.7")
	1.7
	parseFloat("1.7e3")
	1700
	parseFloat("")
	NaN
	```	
	
- 关于空字符串

	```
	Number("")
	0
	parseInt("")
	NaN
	isNaN("")
	false
	```
parseInt()会返回NaN。isNaN()却又返回false。

### 自动类型转换
- “+” 转为字符串拼接
-  “-”，“*”，“/”,"%" 转为数值运算。调用Number()
-  "+","-"做亿元运算时，转为数值
-  布尔运算符，转为布尔型
-  == 和 != 运算，如果类型不同，先进行类型转换。  
	原则是：
	- 如果有一个操作数是布尔值，则在比较相等性之前先将其转换为数值——false 转换为 0，而true 转换为 1；
	- 如果一个操作数是字符串，另一个操作数是数值，在比较相等性之前先将字符串转换为数值；
	- 如果一个操作数是对象，另一个操作数不是，则调用对象的 valueOf()方法，用得到的基本类型值按照前面的规则进行比较；
   - 这两个操作符在进行比较时则要遵循下列规则。  
null 和 undefined 是相等的。
要比较相等性之前，不会将 null 和 undefined 转换成其他任何值。

	```
	null == undfined       //true
	"NaN" == NaN           //false
	NaN == NaN             //false
	5 == NaN              //false
	NaN != NaN            //true
	false == 0           //true
	true == "1"            //true
	undefined == 0        // false
	```
	
### 类型检测
typeof()  
> (1) "undefined"——如果这个值未定义；  
(2) "boolean"——如果这个值是布尔值；  
(3) "string"——如果这个值是字符串；  
(4) "number"——如果这个值是数值；  
(5) "object"——如果这个值是对象或 null；  
(6) "function"——如果这个值是函数。  

### 相等与严格相等
相等运算符（==）比较两个“值”是否相等，严格相等运算符（===）比较它们是否为“同一个值”。两者的一个重要区别是，如果两个值不是同一类型，严格相等运算符（===）直接返回false，而相等运算符（==）会将它们转化成同一个类型，再用严格相等运算符进行比较。

相等运算符隐藏的类型转换，会带来一些违反直觉的结果。  

建议不要使用相等运算符（==），最好只使用严格相等运算符（===）。

### void运算符
void运算符的作用是执行一个表达式，然后返回undefined。

```
void 0 // undefined
void (0) // undefined
```
上面是void运算符的两种写法，都正确。建议采用后一种形式，即总是使用括号。因为void运算符的优先性很高，如果不使用括号，容易造成错误的结果。比如，`void 4+7` 实际上等同于 `(void 4) +7 `。
下面是void运算符的一个例子。

```
var x = 3;
void (x = 5) //undefined
x // 5
```
这个运算符主要是用于书签工具（bookmarklet）或者用于在超级链接中插入代码，目的是返回undefined可以防止网页跳转。  
比如，下面是常用于网页链接的触发鼠标点击事件的写法。  
`<a href="#" onclick="f();">文字</a>`  
上面代码有一个问题，函数f必须返回false，或者onclick事件必须返回false，否则会引起浏览器跳转到另一个页面。  

```
function f(){
    // some code
    return false;
}
```
void运算符可以取代上面两种写法。  
`<a href="javascript:void(0)" onclick="f();">文字</a>`




