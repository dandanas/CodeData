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

![](..\Img\Class.PNG)

鉴于历史原 getName 方法在应用于数组类型的时候会返回一个很奇怪的名字

一个 Class对象实际上表示的是一个类型，而这个类型未必一定是一种类。例如， int 不是类，但 int.class 是一个 Class类型的对象

虚拟机为每个类型管理一个 Class 对象。因此，可以利用 == 运算符实现两个类对象比较的操作

```java
if(cat.getClass() == Cat.class)
```

**newInstance() 可以用来动态的创建一个实例，调用的默认的构造函数(不带参数)初始化新建的对象，则该类与其父类必须拥有不带参数的构造函数，否则会抛出异常** 

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

![](..\Img\反射-检查类的结构.png)

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



##### 运行时使用反射分析对象

在运行时获得对象域的实际内容

在编写程序时，如果知道想要査看的域名和类型，查看指定的域是一件很容易的事情

而利用反射机制可以查看在编译时还不清楚的对象域

1. 获得对应的Class对象
2. 通过Class对象获得对应的域 Field
3. 查看对象域的关键方法是 Field类中的 get 方法。如果 f 是一个 Field类型的对象，obj 是某个包含 f域的类的对象，f.get(obj)将返回一个 对象，其值为 obj 域的当前值

```java
public class Reflective {
	@Test
    public void test3(){
        Animal animal = new Animal("person", "dandan", 12);
        animal.setNumber(10);
        System.out.println("更改前 : " + animal);
        Class animalClass = animal.getClass();
        // type 与name 都是可见的 public
        Field type = null;
        Field number = null;
        // name 是 private
        Field name = null;
        try {
            type = animalClass.getDeclaredField("type");
            number = animalClass.getDeclaredField("number");
            name = animalClass.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Object animalBuild = null;
        Object animalAgeBuild = null;
        Object animalNameBuild = null;
        try {
            //获取 animal 对象中各个 Field 的值
            animalBuild = type.get(animal);
            animalAgeBuild = number.getInt(animal);

            //覆盖访问控制
            name.setAccessible(true);
            animalNameBuild = name.get(animal);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("animal type : " + animalBuild);
        System.out.println("animal age : " + animalAgeBuild);
        System.out.println("animal name : " + animalNameBuild);
        try {
            //更改 animal 对象中 type Field 的值
            type.set(animal, "dog");
            number.setInt(animal, 100);
            name.set(animal, "sahbi");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("更改后 : " + animal);
    }
}
```

![](..\Img\反射-运行时分析对像.png)

name是个私有域，在用get方法是会抛出异常

用get方法只能得到可访问域的值，除非拥有访问权限，否则 Java 安全机制只允许査看任意对象有哪些域，而不允许读取它们的值

name.setAccessible(true); 可以使 name覆盖访问控制，使Java 程序没有受到安全管理器的控制

setAccessible方法是 AccessibleObject类中的一个方法，它是 Field、 Method 和 Constructor类的公共超类。这个特性是为调试、持久存储和相似机制提供的

当域的数据类型是基本数据类型而不是类的时候，可以使用 Field类中的 getInt()方法，也可以调用 get 方法，会自动装箱

Filed 的set方法用一个新值设置 Obj 对象中Field对象表示的域

AccessibleObject  (Field、 Method 和 Constructor类的公共超类) 的方法

* void setAccessible(boolean flag) 为反射对象设置可访问标志。flag 为 true 表明屏蔽 Java语言的访问检查，使得对象的 私有属性也可以被査询和设置。
* boolean  isAccessible( ) 返回反射对象的可访问标志的值。
* static void setAccessible(AccessibleObject[ ] array ,boolean flag) 是一种设置对象数组可访问标志的快捷方法

Field 获取对象的域值，与设置对象的域值

* Object  get (Object obj ) 返回 obj 对象中用Field 对象表示的域值
* void  set(Object obj ,Object newValue) 用一个新值设置 Obj 对象中Field对象表示的域



##### 反射编写泛型数组

java.lang.reflect 包中的 Array类允许动态地创建数组

一个对象数组( Object[]) 不能转换成雇员数组( Employee[ ])。如果这样做，则在运行时 Java将会产生 ClassCastException 异常

![](..\Img\反射-编写泛型数组.png)

Java 数组会记住每个元素的类型，即创建数 组时 new表达式中使用的元素类型。将一个Employee[ ]临时地转换成 Object[ ]数组，然后 再把它转换回来是可以的，但一从开始就是 Object[]  的数组却永远不能转换成 Employe [] 数组

目的就是编写通用数组复制编码

需要 能创建于原数组类型相同的新数组，使用需要java, lang.reflect 包中 Array类的一些方法

```java
Object newArray = Array.newlnstance(componentType, newLength) ;
```

* 需要获取原数组的类对象
* 确认是数组
* 使用 Class类（只能定义表示数组的类对象）的 getComponentType方法确定数组对应
    的类型

```java
public class Reflective {    
	private static Object copyOf(Object a, int newLength){
        //获取数组的类对象
        Class c = a.getClass();
        //确认类对象是数组
        if(!c.isArray()){
            return null;
        }
        //确认数组对应的类型
        Class componentType = c.getComponentType();
        int length = Array.getLength(a);
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
        return newArray;
    }
}
```



##### 反射机制允许调用任意方法

在 Method类中有一个 invoke 方法，它允许调用包装在当前 Method 对象中 的方法

```
Object invoke(Object obj, Object... args)
```

第一个参数是要调用方法的对象， 后面接该方法的参数

```java
public class Reflective {    
	@Test
    public void test6(){
        Animal animal = new Animal("dog", "rock", 12);
        animal.setNumber(2);
        System.out.println("animal begin changed : " + animal);
        Class animalClass = animal.getClass();
        Method test = null;
        Method toString = null;
        Method setType = null;
        try {
            //获取Animal 类对象的方法
            test = animalClass.getDeclaredMethod("Test");
            toString = animalClass.getDeclaredMethod("toString");
            setType = animalClass.getDeclaredMethod("setType", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("通过反射调用 begin");
        try {
            // invoke 调用参数对象的该方法
            test.invoke(animal);
            System.out.println("通过invoke调用 : " + toString.invoke(animal));
            setType.invoke(animal, "cat");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("反射调用 end");
        System.out.println("animal end changed : " + animal);
    }
}
```

通过invoke 可以调用任意方法

invoke 的参数和返回值必须是 Object 类型的

* public  Object  invoke( Object implicitParameter , Object[]  explicitParamenters) 调用这个对象所描述的方法，传递给定参数，并返回方法的返回值。对于静态方法，把 null作为隐式参数传递。在使用包装器传递基本类型的值时，基本类型的返回值必须是未包装的。