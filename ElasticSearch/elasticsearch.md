### ElasticSearch

##### ES关于索引的操作REST命令

| method |                    url                     |       描述       |
| :----: | :----------------------------------------: | :--------------: |
|  PUT   |     localhost:9200/\_index/_type/\_id      | 创建文档(指定id) |
|  POST  |        localhost:9200/\_index/_type        | 创建文档(随机id) |
|  POST  | localhost:9200/\_index/\_type/_id/\_update |     修改文档     |
| DELETE |     localhost:9200/\_index/\_type/\_id     |     删除文档     |
|  GET   |     localhost:9200/\_index/\_type/\_id     |  通过id查询文档  |
|  POST  |   localhost:9200/\_index/\_type/\_search   |   查询所有数据   |

##### ES关于文档的基本操作

> 基本操作

