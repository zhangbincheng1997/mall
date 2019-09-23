## MySQL
1. 设置MySQL服务允许外网访问
```
$ vim /etc/my.cnf

[mysqld]
port        = 3306
bind-address = 0.0.0.0
```

2. 设置MySQL用户允许外网访问
```
$ mysql -u root -p

mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456'; 
mysql> FLUSH PRIVILEGES;
```

3. Navicat Premium 12 破解
参考 https://www.52pojie.cn/thread-952490-1-1.html

Druid
http://localhost:8080/druid/index.html

MyBatis Generator
先数据库建好表，然后Run Generator.java

## Tomcat
1. 安装
```
$ wget http://us.mirrors.quenda.co/apache/tomcat/tomcat-9/v9.0.24/bin/apache-tomcat-9.0.24.tar.gz
$ tar -zxvf apache-tomcat-9.0.24.tar.gz
$ mv apache-tomcat-9.0.24.tar.gz tomcat
$ mv tomcat /usr/local
```

## Redis
1. 安装
```
$ yum install redis
```

2. 配置
```
$ vim /etc/redis.conf

bind 127.0.0.1         ----> # bind 127.0.0.1
protected-mode yes     ----> protected-mode no
# requirepass foobared ----> requirepass 123456
daemonize no           ----> daemonize yes
```

3. 启动
```
$ redis-server /etc/redis.conf
```

## RabbitMQ
1. 安装
```
$ wget https://www.rabbitmq.com/releases/erlang/erlang-19.0.4-1.el7.centos.x86_64.rpm
$ wget https://www.rabbitmq.com/releases/rabbitmq-server/v3.6.15/rabbitmq-server-3.6.15-1.el7.noarch.rpm
$ rpm -ivh erlang-19.0.4-1.el7.centos.x86_64.rpm
$ rpm -ivh rabbitmq-server-3.6.15-1.el7.noarch.rpm
$ yum install socat
```

2. 启动/关闭
```
$ service rabbitmq-server start
$ service rabbitmq-server stop
```

3. 开启web插件
```
$ rabbitmq-plugins enable rabbitmq_management

localhost:15672

# 创建用户
Admin ----> Add a user ----> Username , Password and Tags(Admin)
# 远程限制
Admin ----> All users ----> Set permission
```

## 端口开放
1. 修改
```
$ vim /etc/sysconfig/iptables
```

2. 重启
```
$ service iptables restart
```

3. 查看端口
```
$ iptables -L -n
```


## 其他
1. lombook 插件

IDEA 多个主函数忽略报错运行
Build, not error check

对应Date类型，前端和数据库不一致：
@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")