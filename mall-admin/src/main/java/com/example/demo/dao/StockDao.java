package com.example.demo.dao;

import org.apache.ibatis.annotations.Update;

public interface StockDao {

    @Update("UPDATE product SET stock=stock+#{count} WHERE id =#{id}")
    int increaseStock(Long id, int count);
}
