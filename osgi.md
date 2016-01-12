## osgi学习笔记

1. bundle依赖解析规则：
  当多个bundle的导出包满足该bundle的import-package依赖时，优先级如下：

      1. 已解析的（resolved）bundle优先级高，未解析的（installed）bundle优先级低。
      2. bundle状态一样，版本高者优先，版本也相同，择选最先安装的bundle。
      
2. 一般bundle都会是resolved状态，这样如果bundleA导入了bundleB导出的版本为1的包，然后bundleC又导出了同名的版本为2的包，
这样如果BundleA的代码中有显式import或者继承bundleB的代码，需要用uses限制export-package
[uses具体链接](http://course.tianmaying.com/osgi-toturial/lesson/osgi-module-layer#22)
