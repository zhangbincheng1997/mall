package com.example.demo;

import com.example.demo.component.redis.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisService redisService;

    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
    public static final String LUA_SCRIPT =
            ""
                    + "for goodsIndex = 1, #KEYS do\n"
                    + "  local goodStock = redis.call('get', KEYS[goodsIndex])\n"
                    + "  if tonumber(goodStock) < tonumber(ARGV[goodsIndex]) then\n"
                    + "    return 0;\n"
                    + "  end\n"
                    + "end\n"
                    + "for goodsIndex = 1, #KEYS do\n"
                    + "  redis.call('decrby', KEYS[goodsIndex], ARGV[goodsIndex])\n"
                    + "end\n"
                    + "return 1;\n";

    @Test
    public void buy() {
        // 设置库存
        redisService.set("1", 100);
        redisService.set("2", 100);
        redisService.set("3", 100);

        // 预减库存
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        keys.add("1");
        keys.add("2");
        keys.add("3");
        values.add(10);
        values.add(20);
        values.add(30);
        Long result = redisService.execute(redisScript, keys, values.toArray());

        // 查看库存
        System.out.println(result);
        System.out.println(redisService.get("1"));
        System.out.println(redisService.get("2"));
        System.out.println(redisService.get("3"));
    }
}
