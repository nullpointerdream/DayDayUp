## JVM优化

###1、对象优先在Eden分配
-XX:SurvivorRatio=8，默认的Eden和Survivor比为8:1
###2、大对象直接进入老年代
通过-XX:PretenureSizeThreshold令大于这个设置值的对象直接在老年代分配，
可以避免在Eden和两个Survivor区域之间发生大量的内存复制操作
###3、长期存活的对象将进入老年代
通过参数-XX:MaxTenuringThreshold （默认为15岁）设置对象晋升老年代的年龄阈值。
###4、动态对象年龄判定
为了更好地适应不同程序的内存状况，虚拟机并不是永远的要求对象的年龄必须达到了MaxTenuringThreshold才能晋升老年代；如果在Survivor空间中相同年龄所有对象的大小总和大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无需等到MaxTenuringThreshold中要求的年龄。
###5、控件分配担保
在Jdk1.6 update24之前，通过-XX:+HandlePromotionFailure设置，避免频繁的FullGC；  
在Jdk1.6 update24之后，只要老年代最大连续空间大于新生代对象总和或者大于历次晋升平均大小，都将进行MinorGC，否则将进行Full GC。
