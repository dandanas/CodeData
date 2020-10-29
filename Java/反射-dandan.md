#### 反射

能够分析类能力的程序称为反射（reflective )

JAVA反射机制是在运行状态中，对于任意一个类。都能都知道这个类的所有属性和方法，对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称之为java语言的反射机制

反射可以用来：

* 在运行时分析类的能力
* 在运行时查看对象，例如，编写一个 toString方法供所有类使用
* 实现通用的数组操作代码
* 利用 Method对象，这个对象很像中的函数指针

##### Class类

在程序运行期间，Java运行时系统始终为所有的对象维护一个被称为运行时的类型标识。 这个信息跟踪着每个对象所属的类。虚拟机利用运行时类型信息选择相应的方法执行。 

然而，可以通过专门的 Java类访问这些信息。保存这些信息的类被称为 Class。

一个 Class 对象将表示一个特定类的属性。

获取Class类对象的几种方法

* 如果 T是任意的 Java类型（或void关键字,包括int基本类型以及数组)，T.class将代表匹配的类对象
* t代表类T的实例，t.getClass() 代表匹配的类对象
* Class.forName(className)  调用Class的静态方法 只有在 className是类名或接口名时才能够执行,否则抛出异常

```java
@Test
public void test() {
    Class c1 = int.class;
    System.out.println("int.class : " + c1);
    Class doubleClass = double[].class;
    System.out.println("double[].class : " + doubleClass);
    Class c2 = null;
    try {
        c2 = Class.forName("com.dandan.bean.Animal");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    System.out.println("com.dandan.bean.Animal : " + c2.getName());
    Class c3 = Cat.class;
    System.out.println("Cat.class : " + c3);
    Class c4 = Animal[].class;
    System.out.println("Animal[].class : " + c4);
    Cat cat = new Cat();
    Class c5 = cat.getClass();
    System.out.println("cat.getClass() : " + c5);

	try {
        /**
        * Cat 以及其父类必须要有无参的构造函数
        */
        Cat cat1 = (Cat) (c3.newInstance());
        cat1.setName("dandan");
        System.out.println(cat1.getName());
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (InstantiationException e) {
        e.printStackTrace();
    }
}
```

![](D:\Data\CodeData\Img\Class.PNG)

鉴于历史原 getName 方法在应用于数组类型的时候会返回一个很奇怪的名字

一个 Class对象实际上表示的是一个类型，而这个类型未必一定是一种类。例如， int 不是类，但 int.class 是一个 Class类型的对象

虚拟机为每个类型管理一个 Class 对象。因此，可以利用 == 运算符实现两个类对象比较的操作

```java
if(cat.getClass() == Cat.class)
```

newInstance() 可以用来动态的创建一个实例，调用的默认的构造函数(不带参数)初始化新建的对象，则该类与其父类必须拥有不带参数的构造函数，否则会抛出异常

##### 分析类-检查类的结构

在java.lang.reflect 包中有三个类 Field、Method 和 Constructor分别用于描述类的域、方
法和构造器。 这三个类都有一个叫做getName 的方法，用来返回项目的名称。Field还有一个getType的方法，用来返回描述域所属类型的 Class 对象。

下面的方法只能获取该类的域、方法、构造方法，并不能获取父类的域、方法、构造方法

```java
@Test
public void reflective(){
    Class cat = Cat.class;
    Field[] fields = cat.getDeclaredFields();
    System.out.println("Fields : ");
    for (Field field: fields) {
   		System.out.println(field.getName() + " --> type : " + field.getType() + " --> " + field);
    }

    Method[] methods = cat.getDeclaredMethods();
    System.out.println("Methods : ");
    for (Method method : methods) {
        System.out.println(method.getName() + " --> " + method);
    }

    Constructor[] constructors = cat.getDeclaredConstructors();
    System.out.println("Constructors : ");
    for (Constructor constructor: constructors) {
        System.out.println(constructor.getName() + " --> " + constructor);
    }
}
```

![](D:\Data\CodeData\Img\反射-检查类的结构.png)

Fileld, Method, Constructor 常用方法

* Class getDeclaringClass( )  【 Field,  Method,  Constructor 】
    返回一个用于描述类中定义的构造器、方法或域的Class对象(返回拥有该方法的类)

* Classs[] getExceptionTypes()  【 Method, Constructor】

    返回一个用于描述方法抛出的异常类型的 Class 对象数组。

* int getModifiers( )  【 Field,  Method,  Constructor 】

    返回一个用于描述构造器、方法或域的`修饰符`  (`private`,`public`,`protected`)的整型数值。使用 Modifier 类中的这个 方法可以分析这个返回值

* String getName( ) 

    返冋一个用于描述构造器、 方法或域名的字符串。

* Class[]  getParameterTypes()    【 Method, Constructor】

    返回一个用于描述参数类型的 Class 对象数组 

* Class getReturnType( )  【Method】

    返回一个用于描述返H类型的 Class 对象