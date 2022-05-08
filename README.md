# mall 烟草选址系统

## 后端技术
| 技术 | 说明 |
| ---- | ---- |
| [Spring Framework](https://github.com/spring-projects/spring-framework) | IoC(控制反转)、AOP(面向切面) |
| [Spring Boot](https://github.com/spring-projects/spring-boot) | MVC框架 |
| [Spring Security](https://github.com/spring-projects/spring-security) | 安全框架 |
| [JWT](https://github.com/jwtk/jjwt) | 单点登录 |
| [MyBatis](https://github.com/mybatis/mybatis-3) | 数据库框架  |
| [MyBatis Plus](https://github.com/baomidou/mybatis-plus) | 数据库增强框架 |
| [MySQL](https://github.com/mysql/mysql-server) | 关系型数据库 |
| [Redis](https://github.com/antirez/redis) | 缓存型数据库 |
| [QiNiu](https://github.com/qiniu/java-sdk) | 对象存储 |
| [Swagger](https://github.com/swagger-api/swagger-ui) | 文档接口 |
| [LogStash](https://github.com/elastic/logstash) | 日志收集 |
| [Hutool](https://github.com/looly/hutool) | Java工具类库 |
| [Lombok](https://github.com/rzwitserloot/lombok) | 简化对象封装工具（需要安装IDEA插件） |

## 结构
mall:  
----mall-mbg: 数据库生成  
----mall-common: 基础模块  
----mall-security: 用户认证  
----mall: 业务代码

## 启动
Run ServerApplication.java

* Web: http://localhost:8090/

* Swagger: http://localhost:8090/swagger-ui.html

## MySQL
1. 安装（略，或直接安装[LNMP](https://lnmp.org/)）

2. 配置外网访问
```
$ vim /etc/my.cnf
+ [mysqld]
+ port = 3306
+ bind-address = 0.0.0.0

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
+ bind 127.0.0.1         ----> # bind 127.0.0.1
+ protected-mode yes     ----> protected-mode no
+ # requirepass foobared ----> requirepass 123456
+ daemonize no           ----> daemonize yes
```

3. 启动/关闭
```
$ redis-server 或者 ($ redis-server /etc/redis.conf)
$ redis-cli shutdown
```

## Spring Security
```
Filter -> Interceptor -> Aspect -> Controller
OncePerRequestFilter 重复执行的filter只需要一次执行

1. AbstractUserDetailsAuthenticationProvider.java
     private class DefaultPostAuthenticationChecks implements UserDetailsChecker
         -> isAccountNonLocked() -> isEnabled() -> isAccountNonExpired()
2. DaoAuthenticationProvider.java
     protected void additionalAuthenticationChecks
         -> check username and password
3. AbstractUserDetailsAuthenticationProvider.java
     private class DefaultPreAuthenticationChecks implements UserDetailsChecker
         -> isCredentialsNonExpired()

USERNAME_NOT_FOUND(1004, "认证失败：用户名不存在"),
BAD_CREDENTIALS(1005, "认证失败：密码错误"),
ACCOUNT_DISABLED(1006, "认证失败：用户不可用"),
ACCOUNT_LOCKED(1007, "认证失败：用户锁定"),
ACCOUNT_EXPIRED(1008, "认证失败：用户过期"),
CREDENTIALS_EXPIRED(1009, "认证失败：证书过期"),
```
