package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.SkuDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonTest {

    @Test
    public void testDemo() {
        String json = "[{\"ids\":[{\"1\":\"1\"},{\"2\":\"4\"},{\"4\":\"8\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"1\"},{\"2\":\"4\"},{\"4\":\"9\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"1\"},{\"2\":\"5\"},{\"4\":\"8\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"1\"},{\"2\":\"5\"},{\"4\":\"9\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"2\"},{\"2\":\"4\"},{\"4\":\"8\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"2\"},{\"2\":\"4\"},{\"4\":\"9\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"2\"},{\"2\":\"5\"},{\"4\":\"8\"}],\"price\":0,\"stock\":0,\"sku\":0},{\"ids\":[{\"1\":\"2\"},{\"2\":\"5\"},{\"4\":\"9\"}],\"price\":0,\"stock\":0,\"sku\":0}]";
        List<SkuDto> skuDtoList = JSONObject.parseArray(json, SkuDto.class);
        skuDtoList.forEach(System.out::println);
    }
}
