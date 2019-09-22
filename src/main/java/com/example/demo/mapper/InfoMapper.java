package com.example.demo.mapper;

import com.example.demo.model.Info;
import org.apache.ibatis.annotations.*;

@Mapper
public interface InfoMapper {

    // 返回自增id
    @Insert("INSERT INTO info(nickname, birth, sex, head_url) " +
            "VALUES(#{nickname}, #{birth}, #{sex}), #{head_url}")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Info info);

    @Select("SELECT * FROM info WHERE id=#{id}")
    @Results({
            @Result(property = "sex", column = "sex", javaType = Info.Sex.class),
            @Result(property = "headUrl", column = "head_url")
    })
    Info getById(long id);

    @Update("UPDATE user SET nickname=#{nickname}, birth=#{birth}, sex=#{sex}, head_url=#{headUrl} WHERE id=#{id}")
    void updateInfo(Info info);
}
