## Optional

> Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。  
>  Optional 类的引入很好的解决空指针异常。
>
```java
//of（）：为非null的值创建一个Optional
Optional<String> optional = Optional.of("bam");
// isPresent（）： 如果值存在返回true，否则返回false
optional.isPresent();           // true
//get()：如果Optional有值则将其返回，否则抛出NoSuchElementException
optional.get();                 // "bam"
//orElse（）：如果有值则将其返回，否则返回指定的其它值
optional.orElse("fallback");    // "bam"
//ifPresent（）：如果Optional实例有值则为其调用consumer，否则不做处理
optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"

```
```java

@Slf4j
public class OptionalTest {

    @Test
    public void whenCreateOfEmptyOptional_thenNullPointerException() {
        User user = new User();
        //Optional<User> opt = Optional.of(user);
        //Optional<User> opt1 = Optional.ofNullable(null);

    }

    @Test
    public void whenCreateOfNullableOptional_thenOk() {
        String name = "John";
        //name为null时会报空指针
        Optional<String> opt = Optional.ofNullable(name);

        assertEquals("cc", opt.get());
    }

    @Test
    public void whenCheckIfPresent_thenOk() {
        Integer[] integers1 = {1, 2};
        List<Integer> test = Arrays.asList(integers1);
        User user = new User("dad", 100,test);
        Optional<User> opt = Optional.ofNullable(user);

        //是否为空
        //assertTrue(opt.isPresent());
        //assertEquals(user.getName(), opt.get().getName());

        /**
         * 检查是否有值的另一个选择是 ifPresent() 方法。该方法除了执行检查，还接受一个Consumer(消费者) 参数
         * 如果对象不是空的，就对执行传入的 Lambda 表达式：
         */

        opt.ifPresent( u -> assertEquals(user.getName(), u.getName()));
    }

    @Test
    public void whenEmptyValue_thenReturnDefault() {
        User user = null;
        Integer[] integers1 = {1, 2};
        List<Integer> test = Arrays.asList(integers1);
        User user2 = new User("dad", 100,test);
        User user3 = new User("father", 100,test);
        //orElse()，如果有值则返回该值，否则返回传递给它的参数值
        //User result = Optional.ofNullable(user).orElse(user2);
        //如果对象的初始值不是 null，那么默认值会被忽略
       // User result = Optional.ofNullable(user3).orElse(user2);
        /**
         * 同类型的 API 是 orElseGet() —— 其行为略有不同。
         * 这个方法会在有值的时候返回值，如果没有值，它会执行作为参数传入的 Supplier(供应者) 函数式接口，并将返回其执行结果
         * 以下写法等同于：
         * User result = Optional.ofNullable(user).orElseGet( () ->{
         *             return user2;
         *         });
         */
        User result = Optional.ofNullable(user).orElseGet( () -> user2);
        assertEquals(user2.getName(), result.getName());
    }

    @Test
    public void givenEmptyValue_whenCompare_thenOk() {
        User user = null;
        User result = Optional.ofNullable(user).orElse(createNewUser());
        log.info("Using orElse{}",result);
        User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
        log.info("Using orElseGet{}",result2);
    }
    private User createNewUser() {
        log.info("Creating New User");
        Integer[] integers1 = {1, 2};
        List<Integer> test = Arrays.asList(integers1);
        User user = new User("dad", 100,test);
        return  user;
    }

    @Test
    public void givenPresentValue_whenCompare_thenOk() {
        User user = new User("baby",100,null);
        //该方法在传入非空值的时候仍然会创建对象，执行createNewUser
        User result = Optional.ofNullable(user).orElse(createNewUser());
        log.info("Using orElse{}",result);
        //该方法在传入非空值的时候不会创建对象
        //在执行较密集的调用时，比如调用 Web 服务或数据查询，这个差异会对性能产生重大影响。
       // User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
       // log.info("Using orElseGet{}",result2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void whenThrowException_thenOk() {
        User result = Optional.ofNullable(new User())
                .orElseThrow( () -> new IllegalArgumentException());
    }

    @Test
    public void whenMap_thenOk() {
        User user = new User(null, 1,null);
        //User user = null;
        //map() 对值应用(调用)作为参数的函数，然后将返回的值包装在 Optional 中。这就使对返回值进行链试调用的操作成为可能 —— 这里的下一环就是 orElse()。
        String name = Optional.ofNullable(user)
                .map(u -> u.getName()).orElse("diedi");

        assertEquals(name, user.getName());
    }

    @Test
    public void whenFlatMap_thenOk() {
        Integer[] integers1 = {1, 2};
        Integer[] integers2 = {1, 2,3};
        List<Integer> test = Arrays.asList(integers1);
        List<Integer> test2 = Arrays.asList(integers2);
        User user = new User("haha", 123,null);
        //既然 getter 方法返回 Integer 值的 Optional，可以在对 User 的 Optional 对象调用 flatMap() 时，用它作为参数。
        //TODO map方法其实就可以，flatmap why?
        //其返回的值是解除包装的 Integer
        List<Integer> test3 = Optional.ofNullable(user)
                .flatMap(u -> u.getTest()).orElse(test2);

        log.info("{}",test3);
        //assertEquals(age, user.getAge().get());
    }

    @Test
    public void whenFilter_thenOk() {
        User user = new User("boss", 1,null);
        //filter() 接受一个 Predicate 参数，返回测试结果为 true 的值。如果测试结果为 false，会返回一个空的 Optional。
        //来看一个根据名字验证来决定接受或拒绝 User(用户) 的示例
        Optional<User> result = Optional.ofNullable(user)
                .filter(u -> u.getName() != null && u.getName().contains("bo"));

        //Predicate表示定义一组条件并确定指定对象是否符合这些条件的方法
        Predicate<User> user1 = u -> u.getName() != null && u.getName().contains("bo");

        assertTrue(result.isPresent());
    }



}
```
 >java 内置的四大函数式接口  
 > 消费型接口 Consumer<T> void accept (T t)  
 > 供给型接口 Supplier<T> T get()  
 > 函数型接口 Function<T,R> R apply(T t)  
 > 断定型接口 Predicate<T> boolean test(T t)  
```java
public class lambdaTest {

    @Test
    public void test1(){
        happy(100, new Consumer<Double>() {
            @Override
            public void accept(Double aDouble) {
                System.out.println("买买买"+aDouble);
            }
        });
    }

    public void happy(double money, Consumer<Double> consumer){
        consumer.accept(money);
    }

    @Test
    public void test2(){
        happy(100,(aDouble)->System.out.println("买买买"+aDouble));
    }

    @Test
    public void test3(){
        List<String> list = Arrays.asList("aaa1","bbb1","ccc");
        List<String> filterString = filterString(list, new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("1");
            }
        });

        System.out.println(filterString);

    }

    //根据指定规则过滤集合中的字符串，此规则由Predicate中的方法决定
    public ArrayList<String> filterString(List<String> list, Predicate<String> predicate){
        ArrayList<String> filterStrings = new ArrayList<>();
        for(String s :list) {
            if (predicate.test(s)) {
                filterStrings.add(s);
            }
        }

        return filterStrings;

    }

    @Test
    public void test4(){
        List<String> list = Arrays.asList("aaa1","bbb1","ccc");
        //filterString(list, s -> {s.contains("1")});
        List<String> filterString = filterString(list, s -> s.contains("1"));

        System.out.println(filterString);

    }

    //构造器引用
    @Test
    public void test5(){

        //lambda表达式实现
        Supplier<User> supplier = () -> new User();
        User user = supplier.get();

        //构造器引用
        Supplier<User> supplier1 = User::new;
    }

    //数组引用
    @Test
    public void test6(){

        //lambda表达式实现
        Function<Integer,String[]> function = length -> new String[length];

        String[] apply = function.apply(10);

        System.out.println(Arrays.toString(apply));

        //数组引用
        Function<Integer,String[]> function1 =String[] :: new;
        String[] apply1 = function.apply(5);

        System.out.println(Arrays.toString(apply1));

    }

}

```