# Maven

场景：公司要进行一个web项目的开发，决定使用structs2+spring+mybatis框架。那我们
做的第一件事应该就是去各个项目的官网下载jar包了，这样可能项目还没开始，jar包已经下了一堆了，jar之间可能存在依赖，冲突，等等乱七八糟。这个时候我们就可以利用maven管理这个jar包。

## Maven基础
- Maven是什么？  
Maven是一个项目管理工具，通过项目对象模型即POM文件，描述项目的组织，依赖，构建和打包信息等。  

- maven安装  

下载，项目主页：https://maven.apache.org/  

添加环境变量：
![](http://i3.tietuku.com/df9168d4f0225593.jpg)
![](http://i3.tietuku.com/b9371f852065a455.jpg)
查看是否生效：
![](http://i3.tietuku.com/4a20430c986f1dba.jpg)

- maven工程的结构  
![](http://i3.tietuku.com/21ff9f758fe8e30b.jpg)  
			
- 基础名词：  

		构件  
		坐标  
		仓库：  
			本地仓库： 修改本地仓库地址conf下的settings.xml
				 <localRepository>/path/to/local/repo</localRepository>
			远程仓库：
				 maven默认提供了一个全球远程中央仓库的地址，打开这个jar D:\apache-maven-3.3.3\lib\maven-model-builder-3.3.3.jar\  
				找到里面的pom-4.0.0.xml文件，这是maven的超级pom，所有的pom均继承这个pom。打开。
				<repositories> 
					-<repository> 
						<id>central</id> 
						<name>Central Repository</name> 
						<url>https://repo.maven.apache.org/maven2</url> <layout>default</layout> 
						-<snapshots> 
							<enabled>false</enabled> 
						</snapshots> 
					</repository> 
				</repositories>
				可以看到中央仓库的url。
				可以复制到浏览器打开，这个地址存放了我们开发可用到的绝大部分资源。
			镜像仓库：国外的仓库可能比较慢，国内有提供镜像仓库。
				修改镜像仓库地址：conf下的settings.xml
				  <mirrors>
					<mirror>
				      <id>maven.oschina.net</id>
				      <mirrorOf>central</mirrorOf>
				      <name>cnResponsitory</name>
				      <url>http://maven.oschina.net/content/groups/public</url>
				    </mirror>    
				  </mirrors>
				就是使用配置的url镜像掉central(就是上面超级pom中的中央仓库id)仓库，也可以只用*通配符。
 
## pom文件讲解  
 在Maven世界中，project可以什么都没有，甚至没有代码，但是必须包含pom.xml文件。
	<!-- 根元素 -->
	<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<!-- 4.0.0是当前仅有的可以被Maven2&3同时支持的POM版本，它是必须的。-->
    <modelVersion>4.0.0</modelVersion>
    
    <!-- 基本设置 坐标信息 -->
	<!-- 项目包名，常用公司网址反写加项目名-->
	<!-- 模块化 -->
    <groupId>...</groupId>
	<!-- 模块名 -->
    <artifactId>...</artifactId>
	<!-- 版本 一般大版本号+分支版本号+小版本号+snapshot(alpha/bata)/release/GA如1.0.1SNAOSHOT-->
    <version>...</version>
	<!-- 打包方式，默认jar，还可用war,zip等 -->
    <packaging>...</packaging>

	<!-- 依赖列表 -->
    <dependencies>
		<dependency>
		..依赖坐标..
			<scope>依赖范围，如test</scope>
			<!-- 设置依赖是否可选，默认false-->
			<optional>false/true</option>
			<!-- 排除依赖传递列表 -->
			<exclusions>
				<exclusion>
					...可加多个排除的依赖...
				</exclision>
			</exclusions>
		</dependency>
		...
	</dependencies>

	<!-- 继承父pom -->
    <parent>...</parent>
	
	<!-- 依赖的管理 -->
	<!-- 不会真正的运行，作用是用于父pom中，供子类引用 -->
    <dependencyManagement>
		<dependencies>
			...
		</dependencies>
	</dependencyManagement>

	<!-- 聚合 可以一下子编译多个模块-->
    <modules>...</modules>

    <properties>...</properties>
    
    <!-- 构建过程的设置 -->
    <build>...</build>

    <reporting>...</reporting>
    
    <!-- 项目信息设置 -->
    <name>.项目描述名..</name>
    <description>..项目描述.</description>
    <url>..项目地址.</url>
    <inceptionYear>...</inceptionYear>
    <licenses>...</licenses>
    <organization>...</organization>
    <developers>...</developers>
    <contributors>...</contributors>
    
    <!-- 环境设置 -->
    <issueManagement>...</issueManagement>
    <ciManagement>...</ciManagement>
    <mailingLists>...</mailingLists>
    <scm>...</scm>
    <prerequisites>...</prerequisites>
    <repositories>...</repositories>
    <pluginRepositories>...</pluginRepositories>
    <distributionManagement>...</distributionManagement>
    <profiles>...</profiles>
	</project>


## maven命令
- mvn -v：查看maven版本
- mvn compile ：编译，在项目根目录下生产target目录，里面有编译生产的class文件  
- mvn test   ： 运行测试用例
- mvn package：打包。在target下生成，名为artifactId加version的包。
- mvn clean：删除target目录
- mvn install：安装生产的jar到本地仓库中，这样其他依赖该jar的模块就可以使用了。

maven编译流程： 编译代码--->发现需要依赖--->去pom中找依赖的坐标--->根据坐标去本地仓库找,找到将路径加到项目的classpath中--->没找到再去中央仓库找，找打放到本地仓库里,再加classpath


## eclipse中使用maven
- eclipse4.x已经默认集成maven，直接使用即可。低版本安装插件。简单略过。
- 创建一个maven工程。
- run as maven build..   对话框中Goals可以填compile编译，package打包等，clean，install直接执行run as mvn install即可

## maven的生命周期
- maven有三套独立的生命周期
	- clean 清理项目。又包含三个阶段
		- pre clean 执行清理前的工作
		- clean 清理上一次构建生产的所有文件
		- post clean 清理后的工作
	- default 核心 构建项目，常用的compile-->test-->package-->install
	- site 生成站点 可以根据pom中的信息自动生产站点。包含以下几个阶段。
		- pre site
		- site
		- post site
		- site deploy 是发布站点到服务器上

## maven的插件
我们使用的compile，install等等都是maven的插件，只是这些是基本的核心插件，所以自动下载。maven还有很多其他的插件。可以去<http://maven.apache.org/plugins/index.html>查看，只是没有vpn的时候慢的要死。  
使用方法举例：

	  <build>
	  	<!-- 使用插件 -->
	  	<plugins>
	  		<plugin>
	  			<groupId>org.apache.maven.plugins</groupId>
	  			<artifactId>maven-source-plugin</artifactId>
	  			<version>2.4</version>
	  			<!-- 指定使用该插件的时机 -->
	  			<executions>
	  				<execution>
	  					<phase>package</phase>
	  					<goals>
	  						<goal>jar-no-fork</goal>	
	  					</goals>
	  				</execution>
	  			</executions>
	  		</plugin>
	  	</plugins>
	  </build>

在package阶段使用该插件将工程打成jar-no-fork的jar包。可查看<http://maven.apache.org/plugins/index.html>得到更详细的说明。


## 依赖范围
重点看一下pom中的<scope>标签  
具体请参见<http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html>
maven提供了三种classpath，分别是编译，测试，运行。  
这三种classpath又可以组合，maven提供了6中scope的值。分别是：
	- compile。 默认的，对编译，测试，运行时都有效。
	- provided。对编译和测试时有效。例子是servlet的api，因为真正运行时会使用容器的servertlet api。
	- runtime。 测试和运行时有效。如JDBC
	- test。 测试时有效。如junit
	- system。编译和测试时有效。但是这个会和系统强关联，不具有可移植性。
	- import。 只用于<dependencyManagement>标签中，表示这是引入的依赖。

