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

    