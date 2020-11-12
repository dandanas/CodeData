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

![](..\Img\idea_docker_配置.png)

直到 Connection successful 才算成功

##### 7. docker-maven-plugin插件

```xml
			<plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>1.2.0</version>
                <!--将插件绑定在某个phase执行-->
                <executions>
                    <execution>
                        <id>build-image</id>
                        <!--用户只需执行mvn package ，就会自动执行mvn docker:build-->
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!--指定生成的镜像名-->
                    <imageName>dandan/${project.artifactId}</imageName>
                    <!--指定标签-->
                    <imageTags>
                        <imageTag>latest</imageTag>
                    </imageTags>
                    <!-- 指定 Dockerfile 路径-->
                    <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>
                    <!--指定远程 docker api地址-->
                    <dockerHost>http://192.168.6.1:2375</dockerHost>
                    <!-- 这里是复制 jar 包到 docker 容器指定目录配置 -->
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <!--jar 包所在的路径  此处配置的 即对应 target 目录-->
                            <directory>${project.build.directory}</directory>
                            <!-- 需要包含的 jar包 ，这里对应的是 Dockerfile中添加的文件名　-->
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                </configuration>
            </plugin>
```

##### 8.在pom文件中指定的路径下新建 Dockerfile文件

```dockerfile
#基础镜像
FROM java:8
# 声明容器中的 /tmp 为匿名卷
VOLUME /tmp
#COPY本地的文件拷贝到容器镜像中
COPY demo-docker8-0.0.1-SNAPSHOT.jar demo.jar
#RUN命令是创建Docker镜像（image）的步骤，RUN命令对Docker容器（ container）造成的改变是会被反映到创建的Docker镜像上的
RUN bash -c "touch /demo.jar"
#暴露镜像的端口供主机做映射，启动镜像时，使用-P参数来讲镜像端口与宿主机的随机端口做映射
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
```

##### 9.直接打包项目就能在docker上创建镜像了

##### 10.docker image

![](..\Img\dockerimage.PNG)

image ID or name 直接指定镜像名

下面加上端口映射

运行就可以启动容器了