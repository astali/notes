###1.pom.xml依赖包引入
```
<!--版本号控制-->
<properties>
        <spring.version>4.3.12.RELEASE</spring.version>
        <activemq.version>5.11.2</activemq.version>
        <junit.version>4.12</junit.version>
    </properties>
   <!--依赖包--> 
         <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>
          <dependency>
                <groupId>org.apache.activemq</groupId>
                <artifactId>activemq-all</artifactId>
                <version>${activemq.version}</version>
            </dependency>
            <!-- Spring -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-beans</artifactId>
                <version>${spring.version}</version>
            </dependency>
             <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-jms</artifactId>
                <version>${spring.version}</version>
            </dependency>
```