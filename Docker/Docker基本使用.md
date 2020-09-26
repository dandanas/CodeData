#### Docker的基本使用

*   查看当前版本

    ```shell
    [root@dandan docker]# docker -version
    ```

*   搜索镜像

    ```shell
    [root@dandan docker]# docker saerch elasticSearch
    仓库名       描述信息                   下载数       是否为官方镜像     是否由自动化构建的镜像
    NAME        DESCRIPTION                STARS       OFFICIAL           AUTOMATED
    nginx       Official build of Nginx.   8187        [OK]    
    ```

*   下载镜像

    下载后的镜像会自动解压到“/var/lib/docker/”数据目录下，镜像文件信息记录到“/var/lib/docker/image/数据目录/repositories.json”文件中，可通过“docker inspect nginx:latest”命令查看

    ```shell
    [root@dandan docker]# docker pull tomcat
    ```

*   查看本地镜像

    ```shell
    [root@localhost ~]# docker images
    仓库名              标签          镜像ID          创建时间            镜像大小
    REPOSITORY         TAG           IMAGE ID        CREATED             SIZE
    tomcat             latest        b175e7467d66    4 days ago          647MB
    ```

*   启动容器

    ```shell
    [root@localhost ~]# docker run -d -p 8080:80 --name local_nginx nginx
    ```

    1.  docker run 运行镜像

    2.  -d 启动docker守护进程
    3.  -p 8080:80 将本地的8080端口绑定到容器的80端口上
    4.  --name local_nginx 分配一个容器名
    5.  nginx 指定运行的镜像名，没有的话指定标签默认是latest

       -i : 表示创建交互式容器

       -t : 表示运行容器的时候建立一个微终端一般与 -i 一起使用

*   关闭容器

    1.docker stop 优雅的关闭容器

    ```shell
    [root@localhost ~]# docker stop -t=30 local_nginx 或 docker stop container_id
    ```

    参数 -t：关闭容器的限时，如果超时未能关闭则用kill强制关闭，默认值10s，这个时间用于容器的自己保存状态

    2.docker kill 容器id或者名称  直接关闭容器

    ```shell
    [root@localhost ~]# docker kill local_nginx 
    [root@localhost ~]# docker kill container_id
    ```

    stop 会给一定的关闭时间交由容器自己保存状态， kill则直接关闭容器	

*   关闭容器

    docker restart 容器id/ 容器名

    ```shell
    [root@localhost ~]# docker restart local_nginx
    ```

    -t：关闭容器的限时，如果超时未能关闭则用kill强制关闭，默认值10s，这个时间用于容器的自己保存状态

*   删除容器

    在删除之前需要关掉容器

    ```shell
    [root@localhost ~]# docker rm container_id
    ```

    