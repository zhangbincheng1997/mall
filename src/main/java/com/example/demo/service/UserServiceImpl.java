package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.model.Info;
import com.example.demo.model.User;
import com.example.demo.mapper.InfoMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.Constants;
import com.example.demo.utils.CookieUtils;
import com.example.demo.utils.MD5Utils;
import com.example.demo.utils.UUIDUtils;
import com.example.demo.vo.InfoVo;
import com.example.demo.vo.UserVo;
import com.example.demo.vo.UpdatePassVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    InfoMapper infoMapper;

    @Autowired
    RedisService redisService;

    @Override
    public Result register(UserVo loginVo) {
        User user = userMapper.getByEmail(loginVo.getEmail());
        // EMAIL_EXIST
        if (user != null) {
            return Result.error(Status.EMAIL_EXIST);
        }
        Info info = new Info();
        infoMapper.insert(info);
        user = new User();
        user.setEmail(loginVo.getEmail());
        String salt = UUIDUtils.UUID();
        user.setPassword(MD5Utils.MD5Salt(loginVo.getPassword(), salt));
        user.setSalt(salt);
        user.setInfo(info);
        userMapper.insert(user);
        return Result.success("");
    }

    @Override
    public Result login(HttpServletResponse response, UserVo loginVo) {
        User user = userMapper.getByEmail(loginVo.getEmail());
        // EMAIL_NOT_EXIST
        if (user == null) {
            return Result.error(Status.EMAIL_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String fmPass = MD5Utils.MD5Salt(loginVo.getPassword(), user.getSalt());
        // PASSWORD_ERROR
        if (!dbPass.equals(fmPass)) {
            return Result.error(Status.PASSWORD_ERROR);
        }
        String token = UUIDUtils.UUID();
        // 设置token
        redisService.set(token, user, Constants.COOKIE_EXPIRY); // token -> user
        // 设置cookie
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    @Override
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookie(request, Constants.COOKIE_TOKEN);
        // 删除token
        redisService.del(token);
        // 删除cookie
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, null, 0);
        return Result.success("");
    }

    @Override
    public Result getUserInfo(User user) {
        return Result.success(user.getInfo());
    }

    @Override
    public Result updatePass(HttpServletRequest request, HttpServletResponse response, UpdatePassVo updatePassVo) {
        String token = CookieUtils.getCookie(request, Constants.COOKIE_TOKEN);
        User user = (User) redisService.get(token);
        String dbPass = user.getPassword();
        String fmPass = MD5Utils.MD5Salt(updatePassVo.getOldPass(), user.getSalt());
        // PASSWORD_ERROR
        if (!dbPass.equals(fmPass)) {
            return Result.error(Status.PASSWORD_ERROR);
        }
        String newPass = MD5Utils.MD5Salt(updatePassVo.getNewPass(), user.getSalt());
        user.setPassword(newPass);
        userMapper.updatePass(user);
        // 重新设置token、cookie时间
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    @Override
    public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, InfoVo infoVo) {
        String token = CookieUtils.getCookie(request, Constants.COOKIE_TOKEN);
        User user = (User) redisService.get(token);

        // infoVo => user
        // BeanUtils.copyProperties(infoVo, user);

        if (infoVo.getNickname() != null) {
            user.getInfo().setNickname(infoVo.getNickname());
        }
        if (infoVo.getBirth() != null) {
            user.getInfo().setBirth(infoVo.getBirth());
        }
        if (infoVo.getSex() != null) {
            user.getInfo().setSex(Info.Sex.parseCode(infoVo.getSex()));
        }
        if (infoVo.getHeadUrl() != null) {
            user.getInfo().setHeadUrl(infoVo.getHeadUrl());
        }
        infoMapper.updateInfo(user.getInfo());
        // 重新设置token、cookie时间
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }
}
