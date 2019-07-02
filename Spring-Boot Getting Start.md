# Spring-Boot Getting Started



## 关于 Spring Boot

官方文档是这样来描述 Spring Boot 的。

> Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
>
> Most Spring Boot applications need very little Spring configuration.

Spring Boot 其实就是由 Pivotal 团队提供的一个框架，其设计目的是用来简化 Spring 应用的初始搭建以及开发过程。

为什么这样说呢？

![img](https://pic2.zhimg.com/50/v2-b2c92856b17a107a7fdd53e65b7b2eff_hd.gif)

先前搭建一个 Spring Web 需要做什么呢？

- 配置 web.xml，加载 Spring 和 Spring mvc
- 配置数据库连接、配置 Spring 事务
- 配置加载配置文件的读取，开启注解
- 配置日志文件

- ...
- 配置完成之后部署 Tomcat 调试
- ...

而 Spring Boot ？

![Spring Initializr](https://s2.ax1x.com/2019/07/01/ZGeKNF.png)

然后 Next 到底。

然后 就交给 IDEA 自动导入依赖包了。

- maven 依赖下载默认使用的是外网，极卡无比，需要在 pom.xml 中添加阿里云镜像仓库。

```xml
  <repositories>
        <repository>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
```

至此，就可以一键 RUN 了！

单单从搭建 Spring  项目的角度来看就已经足够彰显其简单、快速、方便的特性了。

## 使用 Spring Boot 的 Java 注释和 XML Bean 配置

https://www.springboottutorial.com/spring-boot-java-xml-context-configuration

#### Loading Beans from Java Configuration

>  src/main/java/test/configuration/JavaConfiguration.java

在` JavaConfiguration.java `中定义 Beans ;

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfiguration {
	
	@Bean
	public String someDummyBean1() {
		return "someDummyBean1";
	}
	
	@Bean
	public String someDummyBean2() {
		return "someDummyBean2";
	}

}
```



#### Loading Beans from XML Configuration

> src/main/resources/applicationContext.xml

在` applicationContext.xml `中定义 Beans ;

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

	<!-- <context:component-scan base-package="com.in28minutes.spring.basics"/> -->

	<bean id="xmlStringBean1" class="java.lang.String">
		<constructor-arg value="stringBean1" />
	</bean>

	<bean id="xmlStringBean2" class="java.lang.String">
		<constructor-arg value="stringBean2" />
	</bean>

</beans>
```

>  src/main/java/test/configuration/XmlConfiguration.java

在` XmlConfiguration.java `中引入`applicationContext.xml` ;

``` java
@Configuration
@ImportResource({"classpath*:applicationContext.xml"})
public class XmlConfiguration {
}
```



## 关于 Docker



https://blogs.msdn.microsoft.com/jcorioland/2016/10/13/getting-started-with-windows-containers/



https://hub.docker.com/_/mysql



- install
- bugger
- commands

ORM(Object Relationship Mapping)

通过 MyBatis 实现 ORM