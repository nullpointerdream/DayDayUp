## Dubbo hession2反序列化问题

### 1.反序列化Pattern
如果dubbo接口中的参数包含java.util.regex.Pattern，在hession2反序列化时会报npe异常。
原因：  
Pattern是实现了Serializable接口的，这没问题。  
但是Pattern只有一个私有化构造器。


	private Pattern(String p, int f) {
        pattern = p;
        flags = f;

        // to use UNICODE_CASE if UNICODE_CHARACTER_CLASS present
        if ((flags & UNICODE_CHARACTER_CLASS) != 0)
            flags |= UNICODE_CASE;

        // Reset group index count
        capturingGroupCount = 1;
        localCount = 0;

        if (pattern.length() > 0) {
            compile();
        } else {
            root = new Start(lastAccept);
            matchRoot = lastAccept;
        }
    }
注意`if (pattern.length() > 0) {`这句直接对pattern进行了调用，如果pattern是null，将报空指针。

继续看hession2，在hession中，反序列化是在`com.alibaba.com.caucho.hessian.io.JavaDeserializer`类中。
在构造函数中：

	if (_constructor != null) {
      _constructor.setAccessible(true);
      Class []params = _constructor.getParameterTypes();
      _constructorArgs = new Object[params.length];
      for (int i = 0; i < params.length; i++) {
        _constructorArgs[i] = getParamArg(params[i]);
      }
    }
这上面还有一段代码是选择构造函数的，目的是选择一个cost最小的构造函数，参数越少越好。  
但是Pattern只有一个构造函数。

继续看`getParamArg`方法：

	 protected static Object getParamArg(Class cl)
	  {
	    if (! cl.isPrimitive())
	      return null;
	    else if (boolean.class.equals(cl))
	      return Boolean.FALSE;
	    else if (byte.class.equals(cl))
	      return new Byte((byte) 0);
	    else if (short.class.equals(cl))
	      return new Short((short) 0);
	    else if (char.class.equals(cl))
	      return new Character((char) 0);
	    else if (int.class.equals(cl))
	      return Integer.valueOf(0);
	    else if (long.class.equals(cl))
	      return Long.valueOf(0);
	    else if (float.class.equals(cl))
	      return Float.valueOf(0);
	    else if (double.class.equals(cl))
	      return Double.valueOf(0);
	    else
	      throw new UnsupportedOperationException();
	  }
注意这里如果不是基本类型，直接返回null。

然后

	protected Object instantiate()
	    throws Exception
	  {
	    try {
	      if (_constructor != null)
		return _constructor.newInstance(_constructorArgs);
	      else
		return _type.newInstance();
	    } catch (Exception e) {
	      throw new HessianProtocolException("'" + _type.getName() + "' could not be instantiated", e);
	    }
	  }

在instantiate方法中调用构造器实例化，抛出空指针。

暂时解决办法：
换序列化协议，serialization="java"
看dubbo的2.6.0版本支持了kryo, FST。我这个2.5.3还不行。



### 2， dubbo 2.5.3对于java.sql的反序列化BUG
https://segmentfault.com/a/1190000009482783
这个我看2.5.9的dubbo已经修复了

### 3，子类父类同名属性的覆盖问题
http://blog.csdn.net/yangxs_cn/article/details/52846362  
没碰到，需要知道。
