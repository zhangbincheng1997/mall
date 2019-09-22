package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.model.User;
import com.example.demo.vo.UserInfoVo;
import com.example.demo.vo.UserVo;
import com.example.demo.vo.UpdatePassVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    Result register(UserVo loginVo);

    Result login(HttpServletResponse response, UserVo loginVo);

    Result logout(HttpServletRequest request, HttpServletResponse response);

    Result getUserInfo(User user);

    Result updatePass(HttpServletRequest request, HttpServletResponse response, UpdatePassVo updatePassVo);

    Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, UserInfoVo userInfoVo);
}
