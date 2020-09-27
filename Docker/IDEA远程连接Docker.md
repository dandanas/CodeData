#### IDEA远程连接Docker 

##### 1.修改docker.service

```shell
vi /usr/lib/systemd/system/docker.service

## ExecStart 后面改成 -H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock
```

##### 2.重启docker ,重新读取配置文件

```shell
# 重新加载配置文件
[root@dandan usr]# systemctl daemon-reload 
# 重启Docker服务
[root@dandan usr]# systemctl restart docke
```

##### 3.查看端口是否生效

```shell
[root@dandan usr]# curl http://127.0.0.1:2375/info
```

#####  4.查看端口是否开启

```shell
[root@dandan usr]# netstat -nlpt
```

##### 5.开放防火墙端口

```shell
[root@dandan usr]# firewall-cmd --add-port=2375/tcp
```

##### 6.IDEA 配置

下载Docker插件

![](E:\CodeData\Img\idea_docker_配置.png)

直到 Connection successful 才算成功

