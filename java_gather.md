## java积累

### 1，list的toArray方法
        public <T> T[] toArray(T[] paramArrayOfT)
        {
            int i = size();
            if (paramArrayOfT.length < i)
                return Arrays.copyOf(this.a, i, paramArrayOfT.getClass());
            System.arraycopy(this.a, 0, paramArrayOfT, 0, i);
            if (paramArrayOfT.length > i)
                paramArrayOfT[i] = null;
            return paramArrayOfT;
        }
根据源码来看，  
如果toArray的入参的size比list小，会以入参数组的类型重新创建一个数组返回。  
如果相等，返回入参数组，此时其实可以不接受返回值，直接用入参。  
如果入参数组size大，大的以null填充。

### 2，数组转list
要将数组转成list，我们都知道用**Arrays.asList(arr)**;  
但是，这里有一个要注意的问题，**这个arr不能是基本类型数组**，  
比如对一个int[],进行Arrays.asList(),返回值是List<int[]>。asList()只能识别对象类型。

### 3，数组的查找
Arrays提供了binarySearch方法进行查找。  
但是，注意这只能用于对已经排好序的数组进行查找，对一个乱序数组使用binarySearch会导致不可预料的后果。
