package cn.jnu.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"cn.jnu.*.mapper", "cn.jnu.*.dao"}) // 扫描Mapper
@SpringBootApplication(scanBasePackages = "cn.jnu")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
