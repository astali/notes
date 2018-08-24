# Maven的安装配置使用 
1. 下载maven 

    [官方网站](http://maven.apache.org)

    Maven是使用java开发，需要安装JDK1.6以上

2. 安装maven 

- 第一步：安装JDK1.6及以上 

- 第二步：将maven下载的压缩包进行解压缩

- 第三步：配置maven的环境变量MAVEN_HOME

    ​	MAVEN_HOME = D:\apache-maven-3.0.5

- 第四步：配置maven的环境变量PATH

    ​	;%M2_HOME%\bin

- 第五步：测试maven是否安装成功，在系统命令行中执行命令：mvn –v

3. 配置maven 

    在maven中有两个配置文件：用户配置、全局配置（默认） 
- 全局配置 

    在maven安装目录的conf里面有一个settings.xml文件，这个文件就是maven的全局配置文件。

    该文件中配置来maven本地仓库的地址

     `<localRepository>D:\apache-maven-3.0.5\repository</localRepository>`

    默认在系统的用户目录下的m2/repository中，该目录是本地仓库的目录。

4. Maven命令的使用 

    Maven的命令要在pom.xml所在目录中去执行，可在pom.xml所在文件夹中按住shift右键，点击“在此处启动命令窗口” 

- mvn compile 
  编译的命令 

- mvn clean 
  清除命令，清除已经编译好的class文件，具体说清除的是target目录中的文件

- mvn test 
  测试命令，该命令会将test目录中的源码进行编译

- mvn package 
  打包命令 

- Mvn install 
  安装命令，会将打好的包，安装到本地仓库

  **组合命令**
- mvn clean compile 

    先清空再编译

- mvn clean test

    先执行clean，再执行test，通常应用于测试环节

- mvn clean package

    先执行clean，再执行package，将项目打包，通常应用于发布前 
    执行过程： 

    清理————清空环境 

    编译————编译源码 

    测试————测试源码 

    打包————将编译的非测试类打包 
- mvn clean install

    查看仓库，当前项目被发布到仓库中 
    组合指令，先执行clean，再执行install，将项目打包，通常应用于发布前 

    执行过程： 
    清理————清空环境 

    编译————编译源码 

    测试————测试源码 

    打包————将编译的非测试类打包 


mybatis自动生成
mvn mybatis-generator:generate 

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
	<context id="testTables" targetRuntime="MyBatis3">
		<commentGenerator>
			<!-- 是否去除自动生成的注释 true：是 ： false:否 -->
			<property name="suppressAllComments" value="true" />
		</commentGenerator>
		<!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
		<jdbcConnection driverClass="com.mysql.jdbc.Driver"
			connectionURL="jdbc:mysql://localhost:3306/user_log" userId="root"
			password="1234">
		</jdbcConnection>
		<!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和 
			NUMERIC 类型解析为java.math.BigDecimal -->
		<javaTypeResolver>
			<property name="forceBigDecimals" value="false" />
		</javaTypeResolver>

		<!-- targetProject:生成PO类的位置 -->
		<javaModelGenerator targetPackage="com.astali.pojo"
			targetProject=".\src">
			<!-- enableSubPackages:是否让schema作为包的后缀 -->
			<property name="enableSubPackages" value="false" />
			<!-- 从数据库返回的值被清理前后的空格 -->
			<property name="trimStrings" value="true" />
		</javaModelGenerator>
        <!-- targetProject:mapper映射文件生成的位置 -->
		<sqlMapGenerator targetPackage="com.astali.mapper"
			targetProject=".\src">
			<!-- enableSubPackages:是否让schema作为包的后缀 -->
			<property name="enableSubPackages" value="false" />
		</sqlMapGenerator>
		<!-- targetPackage：mapper接口生成的位置 -->
		<javaClientGenerator type="XMLMAPPER"
			targetPackage="com.astali.mapper"
			targetProject=".\src">
			<!-- enableSubPackages:是否让schema作为包的后缀 -->
			<property name="enableSubPackages" value="false" />
		</javaClientGenerator>
		<!-- 指定数据库表 -->
		<table schema="" tableName="tb_panel"></table>
	</context>
</generatorConfiguration>

```

百度云连接下载打包项目

https://pan.baidu.com/s/1-Q9_SJ20TtW3tRAOSRcvcg  提取码：syrc

**项目引用本地jar**

```
 <dependency>
      <groupId>com.muzidao.dubbo</groupId>
      <artifactId>dubbo_interfacer</artifactId>
      <version>0.0.1</version>
      <type>jar</type>
      <scope>system</scope>
      <systemPath>${project.basedir}/src/main/webapp/WEB-INF/lib/dubbo_interface0.0.1.jar</systemPath>
  </dependency>
```



 