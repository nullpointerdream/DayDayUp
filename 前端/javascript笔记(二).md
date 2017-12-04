##javascript笔记（二）

### 数组
#### length
设置length会导致数组丢掉超长的数据，很危险，要小心。

	
	var arr = ["1",2,3,4]
	arr.length
	4
	arr.length = 1
	1
	arr
	["1"]
	arr.length = 4
	4
	arr
	["1", undefined × 3]

以undefined填充为赋值的元素。  

#### push，pop，shift，unshift
push，pop 在数组的末尾塞/删元素  
unshift，shift 在数组的开头塞/删元素  

他们都会改变数组的length属性值  

#### delete
用delete删除数组的一个元素，不会改变数组的length。使数组变为稀疏数组。    


	arr
	["1", 2, 3, 4]
	delete arr[2]
	true
	arr
	["1", 2, undefined × 1, 4]

以undefined填充。  

#### splice
splice也可以用于删除元素，并且返回被删除的元素。会改变数组的length。  
param1是要删除元素的index，param2是删除的个数。


	arr
	["1", 2, 3, 4]
	arr.splice(2,1)
	[3]
	arr
	["1", 2, 4]


#### 数组复制和连接contact  

	arr
	["1", 2, 4]
	var b = arr.concat()
	undefined
	b
	["1", 2, 4] -------复制
	b.concat(5,6,7)
	["1", 2, 4, 5, 6, 7] -------复制b，并且连接参数返回一个新数组
	b
	["1", 2, 4] -------b不会改变
	b.concat([5,6])
	["1", 2, 4, 5, 6]


#### 截取slice

	arr
	[1, 2, 3, 4]
	b = arr.slice(1)
	[2, 3, 4]
	arr
	[1, 2, 3, 4]
	arr.slice(1,3)
	[2, 3]

slice()截取数组，不会影响原数组。第二个参数是结束为止（不会截取该位置,从0开始算的），没有第二个参数，是截取到最后。所以实际上也可以用来复制数组。  

#### 插入删除 splice
splice()函数接受三个参数：1，定位index 2，要删除的项数  3，要插入的元素。    
会改变原数组。  
要删除的项数大于剩下的总数，不会报错，删除index后面的所有元素。

	```
	arr
	[1, 2, 3, 4]
	arr.slice(1,3)
	[2, 3]
	arr
	[1, 2, 3, 4]
	arr.splice(1,1,"A","B")
	[2]          --------------函数的返回值是被删除的元素数组
	arr
	[1, "A", "B", 3, 4]  ------会改变原数组
	```
#### 排序sort
比较的是字符串，调用toString()方法转换成字符串排序。默认是升序排列。  
会改变原数组。  


#### 数组字符串化 join
使用join可以连接数组的每个元素，返回字符串。   

	arr
	[1, 2, 3, 4]
	arr.join("-")
	"1-2-3-4"
	arr = [1,2,3,[4,5,6]]
	[1, 2, 3, Array[3]]
	arr.join("-")
	"1-2-3-4,5,6"  --------不会递归的
	



#### 再说length
记住：数组的长度是比数组最大索引值多一的数。  

	var a = ["a","b", "c"]
	undefined
	a.length
	3
	a[100] = "c"
	"c"
	a.length
	101
中间的值会被undefined填充。  

#### 循环
循环可以

	for (var i = 0; i < a.length; i++) {
	    // Do something with a[i]
	}

也可以

	for (var i in a) {
	  // Do something with a[i]
	}
注意，如果有人向 Array.prototype 添加了新的属性，使用这样的循环这些属性也同样会被遍历。所以并不推荐这种方法

ECMAScript 5 增加了遍历数组的另一个方法 forEach()：

	["dog", "cat", "hen"].forEach(function(currentValue, index, array) {
	  // Do something with currentValue or array[index]
	});