package com.example.demo.mapper;

import com.example.demo.model.OrderTimeline;
import com.example.demo.model.OrderTimelineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderTimelineMapper {
    long countByExample(OrderTimelineExample example);

    int deleteByExample(OrderTimelineExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderTimeline record);

    int insertSelective(OrderTimeline record);

    List<OrderTimeline> selectByExample(OrderTimelineExample example);

    OrderTimeline selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderTimeline record, @Param("example") OrderTimelineExample example);

    int updateByExample(@Param("record") OrderTimeline record, @Param("example") OrderTimelineExample example);

    int updateByPrimaryKeySelective(OrderTimeline record);

    int updateByPrimaryKey(OrderTimeline record);
}