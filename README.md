## 安装LNMP
https://lnmp.org/

设置MySQL服务允许外网访问
/etc/my.ini
[mysqld]
port        = 3306
bind-address = 0.0.0.0

设置MySQL用户允许外网访问
$ mysql -u root -p
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456';
mysql> FLUSH PRIVILEGES;

## 安装Redis
yum install redis
/etc/redis/redis.conf
```
bind 127.0.0.1
protected-mode yes
# requirepass foobared
```

```
# bind 127.0.0.1
protected-mode no
requirepass 123456
```
开机自启动：chkconfig --add redis

## 安装RabbitMQ
下载：
http://www.rabbitmq.com/releases/erlang/erlang-xxxx.rpm
http://www.rabbitmq.com/releases/rabbitmq-server/xxxx/rabbitmq-server-xxxx.rpm
安装：
rpm -ivh erlang-xxxx.rpm
rpm -ivh rabbitmq-server-xxxx.rpm
依赖：
yum install socat

开启web插件：(localhost:15672)
rabbitmq-plugins enable rabbitmq_management
启动：
service rabbitmq-server start
关闭：
service rabbitmq-server stop

创建用户:
rabbitmqctl add_user root 123456
rabbitmqctl set_user_tags root administrator

Admin-root-Current permissions-Set permission

## 安装Kafka
$ wget https://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.3.0/kafka_2.12-2.3.0.tgz
$ tar -zxvf kafka_2.12-2.3.0.tgz
$ cd kafka_2.12-2.3.0.tgz
$ vim config/server.properties
advertised.listeners=PLAINTEXT://www.littleredhat1997.com:9092
$ vim /etc/hosts
xxx.xx.xx.xx www.littleredhat1997.com

启动：
$ nohup bin/zookeeper-server-start.sh config/zookeeper.properties &
$ nohup bin/kafka-server-start.sh config/server.properties &

关闭：

$ bin/kafka-server-stop.sh

如果内存不足：
$ vim bin/zookeeper-server-start.sh
$ export KAFKA_HEAP_OPTS="-Xmx256M -Xms256M"
$ vim bin/kafka-server-start.sh
$ export KAFKA_HEAP_OPTS="-Xmx256M -Xms256M"

## 端口开放
/etc/sysconfig/iptables
-A INPUT -p tcp -m tcp --dport xxxx -j ACCEPT
service iptables restart