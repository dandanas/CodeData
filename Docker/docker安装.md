#### Docker安装

1.Docker 要求 CentOS 系统的内核版本高于 3.10 

```shell
[root@localhost ~]$ uname -r //查看当前内核版本
```

2.使用root，并且yum包更新到最新

```shell
[root@localhost ~]$ su -
[root@localhost ~]$ yum update
```

3.配置yun-docker存储库

```shell
[root@localhost ~]# yum -y install epel-release.noarch yum-utils
[root@localhost ~]# yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

4.安装依赖

```shell
[root@localhost ~]# yum -y install device-mapper-persistent-data  lvm2
```

5.安装Docker

```shell
[root@localhost ~]# yum list docker-ce.x86_64 --showduplicates | sort -r //获取docker版本列表
[root@localhost ~]# yum install docker-ce //默认安装最新版本
```

6.启动Docker服务

```shell
[root@localhost ~]# systemctl start docker
[root@localhost ~]# systemctl enable docker
```

7.配置Docker在线镜像源为国内镜像源

由于Docker安装完成后默认使用的镜像仓库是Docker官方的，属于国外站点，可能需要翻墙，所以我们需要正常使用Docker的话，需要将镜像源修改为国内知名机构克隆的镜像仓库。

```shell
[root@localhost ~]# vim /etc/docker/daemon.json
{
  "registry-mirrors": ["http://hub-mirror.c.163.com"]
}
[root@localhost ~]# systemctl restart docker
```

