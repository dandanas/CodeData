SpringApplication 构造函数 ：
1.配置SpringBean 源
2.推断javaWebApplication类型 是否是web类型，如果是则启动tomcat

SpringBoot自动装配：
1.手动装配 
    Spring模式注解 :
        模式注解上用于声明在应用中扮演组件角色的注解
       见 SpringBoot/注解.md

   > Spring自定义注解 : 表示该自定义注解作为哪种类型的声明
   
   > `@Target`({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    
     public enum ElementType {
         /** 类，接口（包括注解类型）或枚举的声明 */
         TYPE,
     
         /** 属性的声明 */
         FIELD,
     
         /** 方法的声明 */
         METHOD,
     
         /** 方法形式参数声明 */
         PARAMETER,
     
         /** 构造方法的声明 */
         CONSTRUCTOR,
     
         /** 局部变量声明 */
         LOCAL_VARIABLE,
     
         /** 注解类型声明 */
         ANNOTATION_TYPE,
     
         /** 包的声明 */
         PACKAGE
     }
   >
   > `@Retention`(RetentionPolicy.RUNTIME)
      注解@Retention可以用来修饰注解，是注解的注解，称为元注解。

   >  Retention注解有一个属性value，是RetentionPolicy类型的，Enum RetentionPolicy是一个枚举类型
      这个枚举决定了Retention注解应该如何去保持，也可理解为Rentention 搭配 RententionPolicy使用。RetentionPolicy有3个值：CLASS  RUNTIME   SOURCE
      按生命周期来划分可分为3类：
            1、RetentionPolicy.SOURCE：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
            2、RetentionPolicy.CLASS：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
            3、RetentionPolicy.RUNTIME：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
      这3个生命周期分别对应于：Java源文件(.java文件) ---> .class文件 ---> 内存中的字节码。
      那怎么来选择合适的注解生命周期呢？
      首先要明确生命周期长度 SOURCE < CLASS < RUNTIME ，所以前者能作用的地方后者一定也能作用。
      一般如果需要在运行时去动态获取注解信息，那只能用 RUNTIME 注解，比如@Deprecated使用RUNTIME注解
      如果要在编译时进行一些预处理操作，比如生成一些辅助代码（如 ButterKnife），就用 CLASS注解；
      如果只是做一些检查性的操作，比如 @Override 和 @SuppressWarnings，使用SOURCE 注解。
   >
   > `@Documented` 
      @Documented 注解表明这个注解应该被 javadoc工具记录. 默认情况下,javadoc是不包括注解的. 
      但如果声明注解时指定了 @Documented,则它会被 javadoc 之类的工具处理, 所以注解类型信息也会被包括在生成的文档中，是一个标记注解，没有成员。
   > `@Inherited`
      Inherited作用是，使用此注解声明出来的自定义注解，在使用此自定义注解时，如果注解在类上面时，子类会自动继承此注解，否则的话，子类不会继承此注解。
      使用Inherited声明出来的注解，只有在类上使用时才会有效，对方法，属性等其他无效
      
     
###### 参考：https://blog.csdn.net/xsp_happyboy/article/details/80987484
    
   
2.自动装配
自动配置幕后英雄：SpringFactoriesLoader详解

  SpringFactoriesLoader属于Spring框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置
  
       public final class SpringFactoriesLoader {
       public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
       private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);
       private static final Map<ClassLoader, MultiValueMap<String, String>> cache = new ConcurrentReferenceHashMap();

   配合@EnableAutoConfiguration使用的话，它更多是提供一种配置查找的功能支持，即根据@EnableAutoConfiguration的完整类名org.springframework.boot.autoconfigure.EnableAutoConfiguration作为查找的Key,获取对应的一组@Configuration类
  
  如果我们不需要某些自动配置，可以通过@EnableAutoConfiguration注解的exclude或者excludeName属性来指定不需要的自动配置，需要注意的是，当不需要的进行自动配置的类不在classpath下时，此时只能通过excludeName属性指定类的全路径名来排除不需要的自动配置。