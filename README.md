# jcstress的使用

## 项目构建遇到的问题
https://wiki.openjdk.java.net/display/CodeTools/jcstress

官网的maven命令执行后，老是报找不到archetype，应该是没有指定archetype版本的原因，用下面的命令代替：

```shell script
mvn archetype:generate \
  -DinteractiveMode=false \
  -DarchetypeGroupId=org.openjdk.jcstress \
  -DarchetypeArtifactId=jcstress-java-test-archetype \
  -DarchetypeVersion=0.5 \
  -DgroupId=com.jcst \
  -DartifactId=jcst \
  -Dversion=1.0
```

添加jcstress samples依赖
```shell script
<dependency>
    <groupId>org.openjdk.jcstress</groupId>
    <artifactId>jcstress-samples</artifactId>
    <version>${jcstress.version}</version>
</dependency>
```

samples中的源码在这里能找到 

http://hg.openjdk.java.net/code-tools/jcstress/file/tip/jcstress-samples/src/main/java/org/openjdk/jcstress/samples

## 使用

在项目目录下执行`mvn clean package`，在target目录下会生成两个jar包

- jcst-1.0.jar
- jcstress.jar

jcst-1.0.jar不用管，jcstress.jar是关注的重点，自己编写的测试类最终也会在这个jar包中

### 打印帮助信息

```shell script
java -jar target/jcstress.jar -h
```

### 通过正则表达式匹配来列出想要找的测试类

比如执行下面命令:

```shell script
java -jar target/jcstress.jar -t APISample -l
```
会输出以下内容,这些都是samples中的测试类:

```shell script

Java Concurrency Stress Tests
---------------------------------------------------------------------------------
Rev: null, built by Administrator with 1.8.0_131 at null

org.openjdk.jcstress.samples.APISample_01_Simple
org.openjdk.jcstress.samples.APISample_02_Arbiters
org.openjdk.jcstress.samples.APISample_03_Termination
org.openjdk.jcstress.samples.APISample_04_Nesting.PlainTest
org.openjdk.jcstress.samples.APISample_04_Nesting.VolatileTest
org.openjdk.jcstress.samples.APISample_05_SharedMetadata.PlainTest
org.openjdk.jcstress.samples.APISample_05_SharedMetadata.VolatileTest
org.openjdk.jcstress.samples.APISample_06_Descriptions
```

自己写的测试类也可以找到，在`com.jcst`包下有一个我自己写的测试类`ZftConcurrencyTest`，执行下面命令：
```shell script
java -jar target/jcstress.jar -t Zft -l
```

得到以下输出:
```shell script
Java Concurrency Stress Tests
---------------------------------------------------------------------------------
Rev: null, built by Administrator with 1.8.0_131 at null

com.jcst.ZftConcurrencyTest
```

其中`-t`还有`-l`的含义在帮助信息中可以找到，不再赘述

## 如何编写一个测试类

几个核心注解的含义：

- @JCStressTest: 代表这是一个jcstress测试类

- @Description:  测试类的一个描述

- @Outcome: 测试结果的一个描述
    一个测试类通常会有多个结果,因此可以有多个@Outcome，不过Duplicated Annotation从jdk8以后才支持，需要注意
    
- @State 加在某个类上，代表这个类保存着测试需要更改/读取的数据

- @Actor 加在方法上，该方法会被一个线程所执行，如果多个方法都有@Actor，那么会有多个线程去分别执行它们其中的代码

- @Arbiter: All memory effects from {@link Actor}s are visible in Arbiter. This makes arbiter useful for reading the final state into
             results. 也就是说这个注解可以将最终的state(所有的Actors都执行完毕)读取到results中.被@Arbiter注解的方法入参是有限制的，参考javadoc
- @Result: 这个注解用户代码几乎用不到，因为jcstress已经预置了许多的结果类 
             
上述注解的含义其实在其javadoc中描述的很清楚，不懂的地方读javadoc即可

## 执行测试

先用samples中的一个类做演示:

执行下面命令：
```shell script
java -jar target/jcstress.jar -t APISample_01_Simple
```
等待一段时间的执行，然后观察结果,觉得命令行的结果不太直观的话，可以看results目录下生成的html文件

比如要执行我自己写的一个ZftConcurrencyTest测试，则执行下面命令：
```shell script
java -jar target/jcstress.jar -t ZftConcurrencyTest
```



## Intellij idea以及Maven的一些配置

```text

首先idea需要一些地方进行配置:
- Project Structure -> Project 中的Project SDK, Project language level
                    -> Modules 中的 language level

- Settings -> Java compiler 中的 bytecode version

这些都调整成1.8相对应的配置

其次，由于需要使用maven进行package，从而得到一个jar包，所以对maven使用的jdk版本也需要进行配置
在${maven_home}/conf/settings.xml中配置下面内容：

<profile>
      <id>jdk-1.8</id>
      <activation>
          <activeByDefault>true</activeByDefault>
          <jdk>1.8</jdk>
      </activation>
      <properties>
          <maven.compiler.source>1.8</maven.compiler.source>
          <maven.compiler.target>1.8</maven.compiler.target>
          <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
      </properties>
</profile>
```


## 补充

Result注解类中有一个拼写错误,算是一个bug吧。。。
```shell script
All fields in {@link Result} classes **shoudl** be either primitive, or String.
```
