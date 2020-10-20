`@SpringBootApplication` 不仅表明这个类是SpringBoot的住配置类，Spring应该运行这个类的main方法来启动SpringBoot应用；
    而且组合了三个其他的注解；

## 1.@SpringBootConfiguration

@SpringBootConfiguration继承自@Configuration，二者功能也一致，标注当前类是配置类（配置类也是容器中的一个组件）
并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到spring容器中，并且实例名就是方法名。

## 2.@EnableAutoConfiguration

将主配置类所在包以及包下面所有子包里面的组件扫描到Spring容器中

启用SpringBoot的自动配置。这个注解会告诉SpringBoot自动配置它认为我们会用到的组件。是借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IoC容器。
@Import(AutoConfigurationImportSelector.class)，借助AutoConfigurationImportSelector，@EnableAutoConfiguration可以帮助SpringBoot应用将所有符合条件的@Configuration配置都加载到当前SpringBoot创建并使用的IoC容器。

selectImports()方法内容：

SpringFactoriesLoader属于Spring框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置

把扫描到的这些文件包装成Properties对象，从Properties中获取到EnableAutoConfiguration类（类名）对应的值，然后把他们添加到容器中

配合@EnableAutoConfiguration使用的话，它更多是提供一种配置查找的功能支持，即根据@EnableAutoConfiguration的完整类名org.springframework.boot.autoconfigure.EnableAutoConfiguration作为查找的Key,获取对应的一组@Configuration类
  
  如果我们不需要某些自动配置，可以通过@EnableAutoConfiguration注解的exclude或者excludeName属性来指定不需要的自动配置，需要注意的是，当不需要的进行自动配置的类不在classpath下时，此时只能通过excludeName属性指定类的全路径名来排除不需要的自动配置。


@EnableScheduling是通过@Import将Spring调度框架相关的bean定义都加载到IoC容器。

@EnableMBeanExport是通过@Import将JMX相关的bean定义加载到IoC容器。

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @AutoConfigurationPackage
    @Import(AutoConfigurationImportSelector.class)
    public @interface EnableAutoConfiguration {
	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

	Class<?>[] exclude() default {};
    String[] excludeName() default {};
    }
## 3.@ComponentScan

启用组件自动扫描并加载符合条件的组件（比如@Component和@Repository等）或者bean定义，最终将这些bean定义加载到IoC容器中。

可以通过basePackages等属性来细粒度的定制@ComponentScan自动扫描的范围，如果不指定，则默认Spring框架实现会从声明@ComponentScan所在类的package进行扫描。

1，ComponentScan注解扫描包。

    @ComponentScan({"com.company.user","com.company.service"})

2，ComponentScan注解扫描类。

    @ComponentScan(basePackageClasses={XxxService.class})
    
参数作用  
 
    basePackageClasses：对basepackages()指定扫描注释组件包类型安全的替代。
     
    excludeFilters：指定不适合组件扫描的类型。
     
    includeFilters：指定哪些类型有资格用于组件扫描。
     
    lazyInit：指定是否应注册扫描的beans为lazy初始化。
     
    nameGenerator：用于在Spring容器中的检测到的组件命名。
     
    resourcePattern：控制可用于组件检测的类文件。
     
    scopedProxy：指出代理是否应该对检测元件产生，在使用过程中会在代理风格时尚的范围是必要的。
     
    scopeResolver：用于解决检测到的组件的范围。
     
    useDefaultFilters：指示是否自动检测类的注释 
    
 #SpringApplication执行流程
 
 main()方法会调用SpringApplication中静态的run()方法，后者会真正执行应用的引导过程，也就是创建Spring应用的上下文。
 在传递给run的两个参数中，一个是配置类，另一个是命令行参数。
 
 1.如果使用的是SpringApplication的静态run方法，那么，这个方法里面首先要创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法
 
 在SpringApplication实例初始化的时候，它会提前做几件事情：
 
    public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    		this.resourceLoader = resourceLoader;
    		Assert.notNull(primarySources, "PrimarySources must not be null");
    		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    		this.webApplicationType = WebApplicationType.deduceFromClasspath();
    		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
    		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    		this.mainApplicationClass = deduceMainApplicationClass();
    	}
--------------------------------------------------------------------------------------------------------------------------
    
    ·根据classpath里面是否存在某个特征类（org.springframework.web.context.ConfigurableWebApplicationContext）来决定是否应该创建一个为Web应用使用的ApplicationContext类型。
 
    ·使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer。
 
    ·使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。
 
    ·推断并设置main方法的定义类。
    
  2.SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener。调用它们的started()方法，告诉这些SpringApplicationRunListener，“嘿，SpringBoot应用要开始执行咯！”。
  
  3.创建并配置当前Spring Boot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。
  
  4.遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，告诉他们：“当前SpringBoot应用使用的Environment准备好了咯！”。
  
  5.如果SpringApplication的showBanner属性被设置为true，则打印banner。【banner：英文广告横幅，在这里面指的是运行时输出的SpringBoot，还可以进行修改】
  
  6.根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，然后根据条件决定是否添加`ShutdownHook`，决定是否使用自定义的`BeanNameGenerator`，决定是否使用自定义的`ResourceLoader`，当然，最重要的，将之前准备好的Environment设置给创建好的ApplicationContext使用。  
  
  `ShutdownHook`
   ###### 参考：https://www.cnblogs.com/goodAndyxublog/p/11658187.html
  
  `BeanNameGenerator`
  spring中每个bean都要有一个id或者name标示每个唯一的bean，在xml中定义一个bean可以指定其id和name值，但那些没有指定的，或者注解的spring的bean name则是BeanNameGenerator接口实现的特性。
  
  7.ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，查找并加载classpath中所有可用的ApplicationContext-Initializer，然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法来对已经创建好的ApplicationContext进行进一步的处理。
  
  8.遍历调用所有SpringApplicationRunListener的contextPrepared()方法。
  
  9.最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的IoC容器配置加载到已经准备完毕的ApplicationContext。
  
  10.遍历调用所有SpringApplicationRunListener的contextLoaded()方法。
  
  11.调用ApplicationContext的refresh()方法，完成IoC容器可用的最后一道工序。
  
  12.查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。
  
  13.正常情况下，遍历执行SpringApplicationRunListener的finished()方法、（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，只不过这种情况下会将异常信息一并传入处理）

