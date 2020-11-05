#### lambda表达式

##### lambda 的用法

> () -> {}

括号里面的是参数， 花括号里面的是实现的内容

Lambda表达式的一些约定

- 一个 Lambda 表达式可以有零个或多个参数；
- 参数的类型既可以明确声明，也可以根据上下文来推断。例如：`(int a)`与`(a)`效果相同
- 所有参数需包含在圆括号内，参数之间用逗号相隔。例如：`(a, b)` 或 `(int a, int b)` 或 `(String a, int b, float c)`
- 空圆括号代表参数集为空。例如：`() -> 42`
- 当只有一个参数，且其类型可推导时，圆括号（）可省略。例如：`a -> return a*a`
- Lambda 表达式的主体可包含零条或多条语句；
- 如果 Lambda 表达式的主体只有一条语句，花括号{}可省略。匿名函数的返回类型与该主体表达式一致
- 如果 Lambda 表达式的主体包含一条以上语句，则表达式必须包含在花括号{}中（形成代码块）。匿名函数的返回类型与代码块的返回类型一致，若没有返回则为空

使用Lambda： `Lambda` 返回的是函数式接口的实例对象

##### 函数式编程接口

**lambda表达式本质是  函数式接口的实例**

**Lambda表达式，实际就是创建出该接口的实例对象**

`@FunctionalInterface`  可以解决编译层面的错误

函数式接口只能有一个抽象方法，多了会抛出编译错误( 接口默认的方法是 `public abstract`), 但是可以添加其他的 `default` 或 `static` 方法

###### 常用的函数式接口

> 一元接口

​	一元接口只有一个入参

* **Function**  函数型接口

    ```java
    public interface Function<T, R> {
        R apply(T t);
    }
    ```

    一个入参一个返回值

* **Consumer **  消费型接口

    ```java
    public interface Consumer<T> {
        void accept(T t);
    }
    ```

    一个入参，没有返回值

* **Supplier** 供给型接口

    ```java
    public interface Supplier<T> {
        T get();
    }
    ```

    无参数，有返回值

* **Predicate** 断定型接口

    ```java
    public interface Predicate<T> {
       boolean test(T t);
    }
    ```

    一个参数，返回Boolean



##### 方法引用

如果**函数式接口的实现恰好可以通过调用一个方法来实现**，那么我们可以使用方法引用

```java
public class Cat extends Animal{

    private Integer legs;

    //不带参数的构造方法
    public Cat(){
    }

    public Cat(String type, String name, Integer age, Integer legs){
        super(type, name, age);
        this.legs = legs;
    }
	
    //实例方法
    public void getCatName(String name){
        String newName = name + " getName";
        System.out.println(newName);
    }
	
    //静态方法
    public static void staticName(String name){
        System.out.println(name + " static dandan");
    }
}
```

* 静态方法的方法引用

    ```java
    //静态方法引用
    Consumer<String> consumerStatic = Cat::staticName;
    consumerStatic.accept("dandan");
    //输出: dandan static dandan
    ```

    

* 非静态方法的方法引用

    ```java
    //构造方法引用
    Supplier<Cat> catNewSupplier = Cat::new;
    System.out.println(catNewSupplier.get());
    //输出: Cat(super=Animal(type=null, name=null, age=null, number=0), legs=null)
    ```

    

* 构造函数的方法引用

    ```java
    Cat cat = new Cat("cat", "miao", 12, 12);
    //实例方法引用
    Consumer<String> consumer = cat::getCatName;
    consumer.accept("dandna");
    //输出: dandna getName
    ```

使用方法引用，如果**函数式接口的实现恰好可以通过调用一个方法来实现**，那么我们可以使用方法引用来替代Lambda表达式

```java
// Supplier是一个无入参带返回的值的函数式编程接口

// () -> new Cat()这整句Lambda表达式，返回的是Supplier接口的实例。从Lambda表达式可以看出无参数，带返回值
Supplier<Cat> cat = () -> new Cat(); 

// 由于这个“() -> new Cat()”Lambda表达式可以通过调用一个方法就实现了，那么我们可以优化成方法引用
Supplier<Cat> cat2 = Cat::new;
```

