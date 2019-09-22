package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // 返回自增id
    @Insert("INSERT INTO user(email, password, salt) VALUES(#{email}, #{password}, #{salt})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(User user);

    @Select("SELECT * FROM user WHERE email=#{email}")
    @Results({
            @Result(column = "info_id", property = "info", one = @One(select = "com.example.demo.mapper.InfoMapper.getById"))
    })
    User getByEmail(String email);

    @Update("UPDATE user SET password=#{password} WHERE id=#{id}")
    void updatePass(User user);
}
