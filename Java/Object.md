#### Object

Object是所有类的超类

除了基本类型外，其他的所有类包括数组都扩展了Object类

##### equals()

Object中的equals用来判断一个对象是否等于另一个对象，实际上通过判断两个对象是否有相同的引用。

```java
public boolean equals(Object obj) {
        return (this == obj);
}
```

这样的话对比两个对象状态是否相等就毫无意义，只需要两个对象的状态(字段)相等，才认为两个对象是相等的

所以需要重写类的equals方法

子类在定义equals时，首先调用父类的equals。如果检测失败，则对象不可能相等。如果父类中的域都相等，则再比较子类中的域。（只有当父类重写过equals才调用super.equals(obj)，不然调用的是Object的equals对相同状态的对象可能返回false）

```java
@Override
public boolean equals(Object otherObject){
    //判断是否引用同一个对象
    if(this == otherObject){
        return true;
    }
    //比较对象不能为空
    if(otherObject == null){
        return false;
    }
    //只有当两个类属于同一个类时，才有可能相等
    if(getClass() != otherObject.getClass()){
        return false;
    }
    Animal other = (Animal) otherObject;
    //比较类下面的域是否都相等，基础类型用 == 对象用 equals
    return this.age.equals(other.getAge()) && this.name == other.getName() && this.type == other.getType();
}
```

```java
@Override
public boolean equals(Object otherObject){
    //父类重写过equals, 所以先比较用父类的比较
    if(!(super.equals(otherObject))){
        return false;
    }
    //引用同一个对象
    if(this == otherObject){
        return true;
    }
    if(otherObject == null){
        return false;
    }
    if(getClass() != otherObject.getClass()){
        return false;
    }
    Cat other = (Cat) otherObject;
    return this.legs.equals(other.getLegs());
}
```

Java 规范要求equals实现下面特性

* 自反性: 对于任何非空引用x，x.equals(x) 应当返回true
* 对称性 ：对于任何引用 x 和 y, 当且仅当 y.equals(x) 返回 true, x.equals(y) 也应该返回 true
* 传递性 ：对于任何引用x、 y 和 z, 如果 x.equals(y) 返N true， y.equals(z)返回 true，x.equals(z) 也应该返回true
* 一致性 ： 如果 x 和 y引用的对象没有发生变化，反复调用 x.eqimIS(y) 应该返回同样的结果
* 对于任何非空 x，x.equals(null) 应当返回false

##### hascode()

散列码（ hash code ) 是由对象导出的一个整型值。散列码是没有规律的。如果 x 和 y 是 两个不同的对象，x.hashCode( ) 与 y.hashCode( ) 基本上不会相同

##### toString()

用于返回表示对象值的字符串

```java
//Object 实现
public String toString() {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
}
```

数组继承了Object的toString()方法，所以会输出 字符串 `[I@4361bd48` 

输出数组一般用

```
System.out.println(Arrays.toString(arr));
```

