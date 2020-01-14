package com.example.demo.mapper;

import com.example.demo.model.AttributeValue;
import com.example.demo.model.AttributeValueExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AttributeValueMapper {
    long countByExample(AttributeValueExample example);

    int deleteByExample(AttributeValueExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AttributeValue record);

    int insertSelective(AttributeValue record);

    List<AttributeValue> selectByExample(AttributeValueExample example);

    AttributeValue selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AttributeValue record, @Param("example") AttributeValueExample example);

    int updateByExample(@Param("record") AttributeValue record, @Param("example") AttributeValueExample example);

    int updateByPrimaryKeySelective(AttributeValue record);

    int updateByPrimaryKey(AttributeValue record);
}