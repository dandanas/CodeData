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

