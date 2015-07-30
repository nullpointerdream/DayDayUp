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

