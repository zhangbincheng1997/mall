package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.entity.User;
import com.example.demo.vo.LoginVo;
import com.example.demo.vo.UpdatePassVo;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    Result register(LoginVo loginVo);

    Result login(HttpServletResponse response,LoginVo loginVo);

    Result logout(HttpServletResponse response, User user);

    Result getUserInfo(User user);

    Result updatePass(User user, UpdatePassVo updatePassVo);
}
