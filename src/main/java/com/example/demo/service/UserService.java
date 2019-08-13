package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.vo.LoginVo;
import com.example.demo.vo.UpdatePassVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    Result register(LoginVo loginVo);

    Result login(HttpServletResponse response, LoginVo loginVo);

    Result logout(HttpServletRequest request, HttpServletResponse response);

    Result getUserByToken(HttpServletRequest request, HttpServletResponse response);

    Result updatePass(HttpServletRequest request, UpdatePassVo updatePassVo);
}
