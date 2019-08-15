package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.entity.Info;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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
    UserRepository userRepository;

    @Autowired
    RedisService redisService;

    @Override
    public Result register(UserVo loginVo) {
        User user = userRepository.findByMobile(loginVo.getMobile());
        // MOBILE_EXIST
        if (user != null) {
            return Result.error(Status.MOBILE_EXIST);
        }
        user = new User();
        user.setMobile(loginVo.getMobile());
        String salt = UUIDUtils.UUID();
        user.setMobile(loginVo.getMobile());
        user.setPassword(MD5Utils.MD5Salt(loginVo.getPassword(), salt));
        user.setSalt(salt);
        user.setInfo(new Info());
        userRepository.save(user);
        return Result.success("");
    }

    @Override
    public Result login(HttpServletResponse response, UserVo loginVo) {
        User user = userRepository.findByMobile(loginVo.getMobile());
        // MOBILE_NOT_EXIST
        if (user == null) {
            return Result.error(Status.MOBILE_NOT_EXIST);
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
        userRepository.save(user);
        // 重新设置token、cookie
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    @Override
    public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, InfoVo infoVo) {
        String token = CookieUtils.getCookie(request, Constants.COOKIE_TOKEN);
        User user = (User) redisService.get(token);
        System.out.println(infoVo);
        if (infoVo.getNickname() != null) {
            user.getInfo().setNickname(infoVo.getNickname());
        }
        if (infoVo.getBirth() != null) {
            user.getInfo().setBirth(infoVo.getBirth());
        }
        if (infoVo.getSex() != null) {
            user.getInfo().setSex(infoVo.getSex());
        }
        if (infoVo.getHead() != null) {
            user.getInfo().setHead(infoVo.getHead());
        }
        userRepository.save(user);
        // 重新设置token、cookie
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }
}
