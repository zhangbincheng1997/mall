package com.example.demo.dao;

import org.apache.ibatis.annotations.Update;

public interface StockDao {

    @Update("UPDATE product SET stock=stock+#{count} WHERE id =#{id}")
    boolean addStock(Long id, int count);

    @Update("UPDATE product SET stock=stock-#{count} WHERE id =#{id}")
    boolean subStock(Long id, int count);
}
