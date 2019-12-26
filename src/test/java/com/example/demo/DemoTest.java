package com.example.demo;

import cn.hutool.core.lang.Snowflake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTest {

    private Snowflake snowflake = new Snowflake(0, 0);

    @Test
    public void testDemo() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(snowflake.nextId());
        }
    }
}
