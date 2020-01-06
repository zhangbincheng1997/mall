package com.example.demo.mapper;

import org.apache.ibatis.annotations.Update;

public interface ProductCustomMapper {

    @Update("UPDATE product SET stock=stock-#{count} WHERE id =#{id}")
    int decreaseStock(Long id, int count);

    @Update("UPDATE product SET stock=stock+#{count} WHERE id =#{id}")
    int increaseStock(Long id, int count);
}
