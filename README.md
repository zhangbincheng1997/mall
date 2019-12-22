# SpringBoot Template

## 后端技术
| 技术 | 说明 |
| ---- | ---- |
| Spring Boot | MVC框架 |
| Spring Security | 安全框架 |
| JWT | 单点登录 |
| MyBatis | 数据库框架  |
| MyBatis Generator | MyBatis生成插件 |
| MyBatis PageHelper | MyBatis分页插件 |
| Druid | 数据库连接池 |
| Redis | 缓存数据库 |
| RabbitMQ | 消息队列 |
| QiNiu | 对象存储 |
| Swagger | 文档接口 |
| LogStash | 日志收集 |
| Lombok | 简化对象封装工具（需要安装IDEA插件） |

MyBatis Generator:
数据库建表（见demo.sql） -> Run Generator.java

Druid:
http://localhost:8080/druid/index.html

Swagger:
http://localhost:8080/swagger-ui.html

## 表
MySQL5.7才支持双timestamp
```
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` int(11) NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `login_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` double(8, 2) NOT NULL,
  `stock` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '商品状态：0正常、1下架，默认0',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

......
```

## MySQL
1. 安装（略，或直接安装[LNMP](https://lnmp.org/)）

2. 配置外网访问
```
$ vim /etc/my.cnf

[mysqld]
port = 3306
bind-address = 0.0.0.0

$ mysql -u root -p

mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456'; 
mysql> FLUSH PRIVILEGES;
```

3. 启动/关闭
```
$ service mysql start
$ service mysql stop
```

## Redis
1. 安装
```
$ yum install redis
```

2. 配置外网访问
```
$ vim /etc/redis.conf

bind 127.0.0.1         ----> # bind 127.0.0.1
protected-mode yes     ----> protected-mode no
# requirepass foobared ----> requirepass 123456
daemonize no           ----> daemonize yes
```

3. 启动/关闭
```
$ redis-server 或者 ($ redis-server /etc/redis.conf)
$ redis-cli shutdown
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
# 外网访问
Admin ----> All users ----> Set permission
```

## Tomcat
1. 安装
```
$ wget http://mirrors.tuna.tsinghua.edu.cn/apache/tomcat/tomcat-9/v9.0.30/bin/apache-tomcat-9.0.30.tar.gz
$ tar -zxvf apache-tomcat-9.0.30.tar.gz
$ mv apache-tomcat-9.0.30.tar.gz tomcat
$ mv tomcat /usr/local
```

2. 启动/关闭
```
$ cd /usr/local/tomcat
$ ./bin/start.sh
$ ./bin/stop.sh
```

## 端口开放
```
# 修改端口
$ vim /etc/sysconfig/iptables
# 重启端口
$ service iptables restart
# 查看端口
$ iptables -L -n
```

## 参考链接
>* Spring Boot博客：https://github.com/ityouknow/spring-boot-examples
>* Spring Boot项目：https://github.com/macrozheng/mall
>* Xshell 6 免费：https://www.netsarang.com/zh/free-for-home-school
>* Navicat Premium 12 破解：https://www.52pojie.cn/thread-952490-1-1.html