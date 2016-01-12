## osgi学习笔记

1. bundle依赖解析规则：
  当多个bundle的导出包满足该bundle的import-package依赖时，优先级如下：

      1. 已解析的（resolved）bundle优先级高，未解析的（installed）bundle优先级低。
      2. bundle状态一样，版本高者优先，版本也相同，择选最先安装的bundle。
      
2. 
