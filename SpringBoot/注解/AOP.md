### AOP【动态代理】  
程序运行期间，动态的将某段代码切入到指定方法指定位置进行运行的编程方式  

1.导入aop模块  
```xml
 <!-- https://mvnrepository.com/artifact/org.springframework/spring-aspects -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>4.3.12.RELEASE</version>
        </dependency>
```
2.定义一个业务逻辑类；在业务逻辑运行时将日志进行打印（方法运行时，运行结束，方法出现异常）

```java
package com.dandan.aop;


/**
 * @date：2020/10/27
 * @author：suchao
 */
public class MathCalculator {

    public int div(int i ,int j){
        return i/j;
    }
}

```  
3.定义一个日志切面类，切面类里面的方法需要动态感知MathCalculate.div运行到了哪里然后执行  
        通知方法：  
            前置通知(@Before)：logStart,目标方法执行前    
            后置通知(@After): logEnd，目标方法执行后  
            返回通知(@AfterReturning): logReturn，目标方法正常执行  
            异常通知(@AfterThrowing): logException，牧鞭方法出现异常  
            环绕通知(Around): 动态代理，手动推进目标方法运行（joinPoint.proceed()) 
```java
package com.dandan.aop;

import org.aspectj.lang.annotation.*;

/**
 * @date：2020/10/27
 * @author：suchao
 * 日志打印切面类
 */
@Aspect
public class LogAspect {

    /**
     *抽取公共的切入点表达式
     *
     */
    @Pointcut("execution(public int com.dandan.aop.MathCalculator.*(..))")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void logStart(){
        System.out.println("除法运行。。参数列表{} ");
    }

    @After("pointCut()")
    public void logEnd(){
        System.out.println("除法结束");
    }

    @AfterReturning("pointCut())")
    public void logReturn(){
        System.out.println("除法正常返回。。。运行结果：{} ");
    }

    @AfterThrowing("pointCut()")
    public void logException(){
        System.out.println("除法发生异常。。。运行异常：{}  ");
    }

}

```
4.给切面类的方法标注何时运行
5.将切面类和业务逻辑类都加入到容器中
```java
package com.dandan.config;

import com.dandan.aop.LogAspect;
import com.dandan.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @date：2020/10/27
 * @author：suchao
 */
@Configuration
@EnableAspectJAutoProxy
public class MainConfigOfAOP {

    //业务逻辑类
    @Bean
    public MathCalculator mathCalculator(){
        return new MathCalculator();
    }

    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }

}

``` 
6.告诉Spring那个是切面类哪个是业务逻辑类，在切面类上加@Aspect注解
7.给配置类中加@EnableAspectJAutoProxy，开启基于注解的AOP模式    
测试类：
```java
package com.dandan.demo;

import com.dandan.aop.MathCalculator;
import com.dandan.config.MainConfigOfAOP;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @date：2020/10/27
 * @author：suchao
 */
public class IOCTest {

    @Test
    public void test() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
        MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
        mathCalculator.div(1,1);
    }
}

```
```md
除法运行。。参数列表{} 
除法正常返回。。。运行结果：{} 
除法结束```