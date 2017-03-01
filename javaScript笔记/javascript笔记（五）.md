## javascript笔记（五）

### 面向对象
javascript中没有类的概念。
#### 创建对象
1. 对面字面量方式

	```
	var person = {
	    name: '张三',
	    age: 27,
	
	    walk: function() {
	        console.log('走路');
	    }
	}
	```
缺点很明显，只适合临时创建对象。  

2. 工厂模式  
自然我们会想到，使用工厂模式批量生产对象。

	```
	function createPerson(name, age) {
	    var o = new Object();
	    o.name = name;
	    o.age = age;
	    o.walk = function() {
	        console.log('走路');
	    }
	    return o;
	}
	
	var person1 = createPerson('zhangsan', 29);
	var person2 = createPerson('lisi', 19);
	```
	但是却没有解决对象识别的问题（即怎样知道一个对象的类型）。
	
3. 构造函数模式  

	```
	function Person(name, age) {
	    this.name = name;
	    this.age = age;
	    this.walk = function() {
	        console.log('走路');
	    }
	}
	
	var zhangsan = new Person('张三', 27);
	var lisi = new Person('李四', 34);
	```

	注意不同点：  
	- 没有显示创建对象，new Object()
	- 直接将属性和方法赋给了this对象
	- 没有return语句
   - 函数名使用大写字母。  

   使用构造函数创建一个对象的步骤是这样的：  
   
   1. 一个新的person对象被创建，它继承自Person.prototype  
	2. 构造函数 Person 被执行。执行的时候，相应的传参会被传入，同时上下文(this)会被指定为这个新实例。new Person 等同于 new Person(), 只能用在不传递任何参数的情况。
	3. 如果构造函数返回了一个“对象”，那么这个对象会取代整个new出来的结果。如果构造函数没有返回对象，那么new出来的结果为步骤1创建的对象，也就是说可以加上return语句返回一个对象，但是会不该掉new出来的对象实例。  

	存在的问题：该方式存在的一个问题是，每次创建的示例都会有自己的walk方法。从逻辑上讲，walk方法应该是可以共用的，但是现在每个实例都有一个walk()方法实例，这是内存上的浪费。  
	
4. 原型模式  
这就要讲到prototype了，我们创建的每个函数都有一个prototype(原型)属性，这个属性是一个指针，指向一个对象，而这个对象的用途是包含可以由特定类型的所有实例共享的属性和方法。  


	```
	function Person() {}

	Person.prototype.name = 'zhangsan';
	Person.prototype.age = 25;
	
	Person.prototype.walk = function() {
	    console.log('走路');
	}；
	
	var zhangsan = new Person();
	zhangsan.walk();        // 走路
	
	var lisi = new Person();
	lisi.walk();            // 走路
	
	alert(zhangsan.walk == lisi.walk); // true
	```
无论什么时候，只要创建了一个新函数，就会根据一组特定的规则为该函数创建一个prototype属性，这个属性指向函数的原型对象。在默认情况下，所有原型对象都会自动获得一个constructor（构造函数）属性，这个属性包含一个指向prototype属性所在函数的指针。就拿前面的例子来说，Person.prototype.constructor指向Person。  

	虽然可以通过对象实例访问保存在原型中的值，但却不能通过对象实例重写原型中的值。如果我们的实例添加了一个属性，而该属性与实例原型中的一个属性同名，那我们就在实例中创建该属性，该属性将会屏蔽原型中的那个属性。  

   更简单的原型语法
前面的例子每添加一个属性和方法就要敲一遍Person.prototype。为减少不必要的输入，也为了从视觉上更好地封装原型功能，更常见的做法是用一个包含所有属性和方法的对象字面量来重写整个原型对象，如下面的例子所示。
	
	```
	function Person() {}
	
	Person.prototype = {
	    name: 'zhangsan',
	    age: 29,
	    walk: function() {
	        console.log('走路');
	    }
	}; 
	``` 
	
	原型模式的问题，在于多个实例共享原型中的值，一个实例的属性改变，会影响其他实例。  
	
5. 组合使用构造函数模式和原型模式  
原则：该实例私有的私有，该公有的公有。
	
	```
	function Person(name, age) {
	    this.name = name;
	    this.age = age;
	    this.friends = ['Jack', 'Lily'];
	}
	
	Person.prototype.walk = function() {
	    console.log('走路');
	};
	
	var p1 = new Person('zhangsan', 22);
	var p2 = new Person('lisi', 33);
	
	p1.friends.push('Steven');
	
	alert(p1.friends);        // 'Jack,Lily,Steven'
	alert(p2.friends);        // 'Jack,lily'
	alert(p1.friends === p2.friends);    // false
	alert(p1.walk === p2.walk);        // true
	```
	
	