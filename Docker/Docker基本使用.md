#### Docker 常用命令

Linux下docker命令在root权限下才有用

##### docker images

所有本地镜像

![image-20200115111742290](D:\Data\CodeData\Img\image-docker-images.png)

##### docker ps

查看容器的运行信息

![image-20200116113432734](D:\Data\CodeData\Img\image-docker-ps.png)

```shell
CONTAINER ID  IMAGE  COMMAND      CREATED  STATUS  PORTS           NAMES  SIZE
容器ID 		 镜像   启动容器的命令  创建时间  状态    端口信息和连接类型  名字    大小
```



不带选项显示当前正在运行的容器

* 选项
    * -a 显示所有进程 容器的状态共有 7 种：created|restarting|running|removing|paused|exited(暂停)|dead
    * --no-trunc 显示完整信息，不会截断信息
    * -s 显示容器大小 一个是容器真实增加的大小，一个是整个容器的虚拟大小 size(vietual 123MB)

##### docker run

命令格式：`docker run [OPTIONS] IMAGE [COMMAND] [ARG...]`

```shell
docker run -p 7524:6379 -d --name redis001 redis
```

* 常用选项

    * `-d, --detach = false` 守护进程，指定容器运行与前台还是后台，默认为false

    * `-u, --user =  ""` 指定容器的用户 

    * `-p, --publish = []` 指定暴露端口  

        ```shell
        docker run -p ip:hostPort:containerPort  redis
        docker run -p 9314:6379 redis  
        ```

        -p 分配宿主机的端口映射到虚拟机 

        ip:hostPort 表示宿主机的ip与端口

        containerPort 表示容器的端口

    * `--name =""` 指定容器的名字，后续可以通过名字对容器进行管理

##### docker pull 

拉取镜像

下载后的镜像会自动解压到“/var/lib/docker/”数据目录下，镜像文件信息记录到“/var/lib/docker/image/数据目录/repositories.json”文件中，可通过“docker inspect nginx:latest”命令查看

#####  systemctl restart docker

重启docker

```shell
systemctl restart docker
```



