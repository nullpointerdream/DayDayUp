## javascript笔记（六）

### 正则

`var expression = / pattern / flags`  
flags是标记，有三种：  

- g: 表示全局模式，即模式将被应用于所有字符串，而非在发现第一个匹配项时立即停止
- i: 表示不区分大小写模式，即在确定匹配项时忽略模式与字符串的大小写
- m: 表示多行模式，即在到达一行文本末尾时还会继续查找下一行中是否存在模式匹配的项

和其他语言一样，元字符要转义：    
`( [ { \ ^ $ | ) ? * + . ] }`  

主要使用正则实例的两种方法：

- exec()exec()接受一个参数，即要应用模式的字符串，然后返回包含第一个匹配信息的数组；或者在没有匹配项的情况下返回null。
- test()接收一个字符串参数。模式与该参数匹配的情况下返回true，否则返回false。

支持正则的String方法：

- search
- match
- replace
- split


### JSON
用于序列化与反序列化。  
特点：  

- 属性名称必须用双引号包裹；最后一个属性后面不能有逗号。
- 前导0不能使用（在 JSON.stringify 中将会被忽略，在 JSON.parse 会抛出错误）；小数点后面至少有一个数字。
- 只有有限的字符能够被转义；不允许某些控制字符；但允许使用Unicode 行分隔符 (U+2028) 和段落分隔符 (U+2029) ; 字符串必须用双引号括起来

#### JSON.parse()
JSON.parse() 方法将一个 字符串解析成一个 JSON 对象。在解析过程中，还可以选择性的修改某些属性的原始解析值。  

- 语法
JSON.parse(text[, reviver])
- 参数
	- text
	要被解析成JSON对象的字符串，查看 JSON 对象学习的JSON 语法的说明。
	- reviver
	可选，如果是一个函数，则规定了原始值如何被解析改造，在被返回之前。
	如果 reviver 返回 undefined，则当前属性会从所属对象中删除，如果返回了其他值，则返回的值会成为当前属性新的属性值。  
	  reviver(name, value)
		- 参数
			- name
			属性名，当遍历到最顶层的值（解析值）时，传入 reviver 函数的参数会是空字符串 ""
			- value
			属性值
			
	```
	// 使用reviver
	JSON.parse('{"p": 5}', function (k, v) {
	    if(k === '') return v;     // 如果到了最顶层，则直接返回属性值，
	    return v * 2;              // 否则将属性值变为原来的 2 倍。
	});                            // { p: 10 }

	```

			
#### JSON.stringify
JSON.stringify() 方法可以将任意的 **JavaScript 值**序列化成 JSON 字符串。若转换的函数被指定，则被序列化的值的每个属性都会经过该函数的转换和处理；若转换的数组被指定，只有包含在这个数组中的属性名才会被序列化到最终的 JSON 字符串中。  

- 语法  
JSON.stringify(value[, replacer [, space]])
- 参数  
	- value  
	将要序列化成 JSON 字符串的值。  
	- replacer 可选  
	如果该参数是一个函数，则在序列化过程中，被序列化的值的每个属性都会经过该函数的转换和处理；  
	如果该参数是一个数组，则只有包含在这个数组中的属性名才会被序列化到最终的 JSON 字符串中；  
	如果该参数为null或者未提供，则对象所有的属性都会被序列化；   
	- space 可选  
	指定缩进用的空白字符串，用于美化输出（pretty-print）；  
	如果参数是个数字，它代表有多少的空格；上限为10。改值若小于1，则意味着没有空格；  
	如果该参数为字符串(字符串的前十个字母)，该字符串将被作为空格；  
	如果该参数没有提供（或者为null）将没有空格。  
	
		
- 注意点
描述  
关于序列化，有下面五点注意事项：  
	1. 非数组对象的属性不能保证以特定的顺序出现在序列化后的字符串中。
	2. 布尔值、数字、字符串的包装对象在序列化过程中会自动转换成对应的原始值。
	3. undefined、任意的函数以及 symbol 值，在序列化过程中会被忽略（出现在非数组对象的属性值中时）或者被转换成 null（出现在数组中时）。
	4. 所有以 symbol 为属性键的属性都会被完全忽略掉，即便 replacer 参数中强制指定包含了它们。
	5. 不可枚举的属性会被忽略

	```
	JSON.stringify({});                        // '{}'
	JSON.stringify(true);                      // 'true'
	JSON.stringify("foo");                     // '"foo"'
	JSON.stringify([1, "false", false]);       // '[1,"false",false]'
	JSON.stringify({ x: 5 });                  // '{"x":5}'
	
	JSON.stringify({x: 5, y: 6});              
	// '{"x":5,"y":6}' 或者 '{"y":6,"x":5}' 都可能
	JSON.stringify([new Number(1), new String("false"), new Boolean(false)]); 
	// '[1,"false",false]'
	JSON.stringify({x: undefined, y: Object, z: Symbol("")}); 
	// '{}'
	JSON.stringify([undefined, Object, Symbol("")]);          
	// '[null,null,null]' 
	JSON.stringify({[Symbol("foo")]: "foo"});                 
	// '{}'
	JSON.stringify({[Symbol.for("foo")]: "foo"}, [Symbol.for("foo")]);
	// '{}'
	JSON.stringify({[Symbol.for("foo")]: "foo"}, function (k, v) {
	  if (typeof k === "symbol"){
	    return "a symbol";
	  }
	});
	// '{}'  
	
	// 不可枚举的属性默认会被忽略：
	JSON.stringify( Object.create(null, { x: { value: 'x', enumerable: false }, y: { value: 'y', enumerable: true } }) );
	// '{"y":"y"}'
	```
	
#### toJSON方法
如果一个被序列化的对象拥有 toJSON 方法，那么该 toJSON 方法就会覆盖该对象默认的序列化行为：不是那个对象被序列化，而是调用 toJSON 方法后的返回值会被序列化，例如：

```
var obj = {
  foo: 'foo',
  toJSON: function () {
    return 'bar';
  }
};
JSON.stringify(obj);      // '"bar"'
JSON.stringify({x: obj}); // '{"x":"bar"}'
```