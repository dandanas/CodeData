#### MongoDB 基本操作

##### 数据库操作

```sql
use db_name;  //存在该数据库则切换到该数据库，否则创建数据库
```

*   创建数据库后需要对数据库做一次操作，数据库才真正创建保留

```sql
db  //显示当前数据库

show dbs;  //查看所有数据库
```

```sql
db.dropDatabase();  //删除数据库
```

*   在DataGrip上删除数据库会报错，但是在命令行上操作不会

##### 集合操作

*   查看已有集合

    ```sql
    show collections;
    show tables;
    ```

*   创建集合

    ```
    db.createCollection(name, options);
    ```

    options: 可选参数, 指定有关内存大小及索引的选项

    |  字段  | 类型 | 描述                                                         |
    | :----: | ---- | ------------------------------------------------------------ |
    | capped | bool | (可选)如果为 true，则创建固定集合。固定集合是指有着固定大小的集合，当达到最大值时，它会自动覆盖最早的文档。<br/>**当该值为 true 时，必须指定 size 参数。** |
    |  size  | int  | (可选)为固定集合指定一个最大值，即字节数。<br/>**如果 capped 为 true，也需要指定该字段。** |
    |  max   | int  | (可选)指定固定集合中包含文档的最大数量。                     |

    MongoDB 首先检查固定集合的 size 字段，然后检查 max 字段。

    参数以json的形式写入命令

    ```sql
    db.createCollection("site", {capped : true, size : 100, max: 10});
    ```
    
* 删除集合

    ```sql
    db.collection.drop()
    ```

	```sql
  db.collection.drop()
	```


##### 文档操作
