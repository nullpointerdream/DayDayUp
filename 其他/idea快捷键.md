> 个人不太喜欢设置快捷键，自定义多了记不过来  
> idea已经很方便了

#### 文件操作：  
新建 ： alt + insert 在包下操作

#### 编辑
删除行 ： ctrl + y  
修复建议 ： alt + enter  
格式化 ： ctrl + alt + l    
清理import ： ctrl + alt + o  
移动 ： ctrl + shift + 上下

#### 查看
跳到父类方法处 ： ctrl + u
跳到方法实现处 ： ctrl + alt + b
方法的使用和定义处来回切换 ctrl + b  
显示类结构 ： alt + 7

#### 视图
alt + v 打开快捷菜单
下面有个全屏的，还有免分心模式
 
#### tomcat jsp热部署
在idea tomcat 中server的配置里，
有个on frame deactivation，选择update classes and resources。  
这是由于服务器添加的Artifact类型问题，一般一个module对应两种类型的Artifact，一种是war，一种是war explored。
war就是已war包形式发布，当前项目是这种形式，在这种形式下on frame deactivation配置没有update classes 
and resources选项。war explored是发布文件目录，选择这  种形式，on frame deactivation中就出现update classes and resources选项了。


  
