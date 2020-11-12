## ThreadLocal对象可以提供线程局部变量，每个线程Thread拥有一份自己的副本变量，多个线程互不干扰。

### ThreadLocalMap.set()
1>往ThreadLocalMap中set数据（新增或者更新数据）分为好几种情况.
  
  第一种情况： 通过hash计算后的槽位对应的Entry数据为空：  
  这里直接将数据放到该槽位即可。
  
  第二种情况： 槽位数据不为空，key值与当前ThreadLocal通过hash计算获取的key值一致：  
  这里直接更新该槽位的数据。
  
  第三种情况： 槽位数据不为空，往后遍历过程中，在找到Entry为null的槽位之前，没有遇到key过期的Entry：
  遍历散列数组，线性往后查找，如果找到Entry为null的槽位，则将数据放入该槽位中，或者往后遍历过程中，遇到了key值相等的数据，直接更新即可。  
  
  第四种情况： 槽位数据不为空，往后遍历过程中，在找到Entry为null的槽位之前，遇到key过期的Entry，往后遍历过程中，遇到了（index=7）的槽位数据Entry的key=null：   
  散列数组下标为7位置对应的Entry数据key为null，表明此数据key值已经被垃圾回收掉了，此时就会执行replaceStaleEntry()方法，该方法含义是替换过期数据的逻辑，以index=7位起点开始遍历，进行探测式数据清理工作。   
  初始化探测式清理过期数据扫描的开始位置：slotToExpunge = staleSlot = 7  staleSlot指过期桶的位置  
  以当前staleSlot开始 向前迭代查找，找其他过期的数据，然后更新过期数据起始扫描下标slotToExpunge。for循环迭代，直到碰到Entry为null结束。  
  如果找到了过期的数据，继续向前迭代，直到遇到Entry=null的槽位才停止迭代  
  接着开始以staleSlot位置(index=7)向后迭代，如果找到了相同key值的Entry数据：  
  从当前节点staleSlot向后查找key值相等的Entry元素，找到后更新Entry的值并交换staleSlot元素的位置(staleSlot位置为过期元素)，更新Entry数据，然后开始进行过期Entry的清理工作  
  从当前节点staleSlot向后查找key值相等的Entry元素，直到Entry为null则停止寻找。此时table中没有key值相同的Entry。  
  创建新的Entry，替换table[stableSlot]位置，替换完成后也是进行过期元素清理工作，清理工作主要是有两个方法：expungeStaleEntry()和cleanSomeSlots()  
  
  通过key来计算在散列表中的对应位置，然后以当前key对应的桶的位置向后查找，找到可以使用的桶。  
  
  遍历当前key值对应的桶中Entry数据为空，这说明散列数组这里没有数据冲突，跳出for循环，直接set数据到对应的桶中  
  如果key值对应的桶中Entry数据不为空  
  1 如果k = key，说明当前set操作是一个替换操作，做替换逻辑，直接返回  
  2 如果key = null，说明当前桶位置的Entry是过期数据，执行replaceStaleEntry()方法(核心方法)，然后返回  
  for循环执行完毕，继续往下执行说明向后迭代的过程中遇到了entry为null的情况  
  1 在Entry为null的桶中创建一个新的Entry对象  
  2 执行++size操作  
  调用cleanSomeSlots()做一次启发式清理工作，清理散列数组中Entry的key过期的数据  
  1 如果清理工作完成后，未清理到任何数据，且size超过了阈值(数组长度的2/3)，进行rehash()操作  
  2 rehash()中会先进行一轮探测式清理，清理过期key，清理完成后如果size >= threshold - threshold / 4，就会执行真正的扩容逻辑  
  
### ThreadLocalMap过期key的探测式清理流程

  遍历散列数组，从开始位置向后探测清理过期数据，将过期数据的Entry设置为null，沿途中碰到未过期的数据则将此数据rehash后重新在table数组中定位，如果定位的位置已经有了数据，则会将未过期的数据放到最靠近此位置的Entry=null的桶中，使rehash后的Entry数据距离正确的桶的位置更近一些。  
  
  经过一轮探测式清理后，key过期的数据会被清理掉，没过期的数据经过rehash重定位后所处的桶位置理论上更接近i= key.hashCode & (tab.len - 1)的位置。这种优化会提高整个散列表查询性能。
   
### ThreadLocalMap扩容机制

  在``ThreadLocalMap.set()方法的最后，如果执行完启发式清理工作后，未清理到任何数据，且当前散列数组中Entry的数量已经达到了列表的扩容阈值(len*2/3)，就开始执行rehash()`逻辑：
  接着看下rehash()具体实现：
  ```java
private void rehash() {
    expungeStaleEntries();

    if (size >= threshold - threshold / 4)
        resize();
}

private void expungeStaleEntries() {
    Entry[] tab = table;
    int len = tab.length;
    for (int j = 0; j < len; j++) {
        Entry e = tab[j];
        if (e != null && e.get() == null)
            expungeStaleEntry(j);
    }
}

```
可以看到首先会执行`expungeStaleEntries()`操作，从table的起始位置往后清理，清理完成之后，table中可能有一些key为null的Entry数据被清理掉，所以此时通过判断size >= threshold - threshold / 4 也就是size >= threshold* 3/4 来决定是否扩容。

#### resize()

扩容后的tab的大小为oldLen * 2，然后遍历老的散列表，重新计算hash位置，然后放到新的tab数组中，如果出现hash冲突则往后寻找最近的entry为null的槽位，遍历完成之后，oldTab中所有的entry数据都已经放入到新的tab中了。重新计算tab下次扩容的阈值，

### ThreadLocalMap.get()详解

第一种情况： 通过查找key值计算出散列表中slot位置，然后该slot位置中的Entry.key和查找的key一致，则直接返回

第二种情况： slot位置中的Entry.key和要查找的key不一致,往后查询，直到找到key值相等的槽位，如果途中遇到了key=null的数据，触发一次探测式数据回收操作，执行expungeStaleEntry()方法，执行完后，key=null的数据都会被回收，而key=null的后面的数据都会前移，此时继续往后迭代直到entry=null停止，如果还未查找说明没有此类型的数据
                                   

  
  
