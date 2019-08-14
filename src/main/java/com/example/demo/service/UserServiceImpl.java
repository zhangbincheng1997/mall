//package com.example.demo.service;
//
//import com.example.demo.base.Result;
//import com.example.demo.base.Status;
//import com.example.demo.component.RedisService;
//import com.example.demo.entity.User;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.utils.Constants;
//import com.example.demo.utils.MD5Utils;
//import com.example.demo.utils.UUIDUtils;
//import com.example.demo.vo.LoginVo;
//import com.example.demo.vo.UpdatePassVo;
//import org.aspectj.apache.bcel.classfile.Constant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Date;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    public Result register(LoginVo loginVo) {
//        User user = userRepository.findByMobile(loginVo.getMobile());
//        // MOBILE_EXIST
//        if (user != null) {
//            return Result.error(Status.MOBILE_EXIST);
//        }
//        user = new User();
//        user.setMobile(loginVo.getMobile());
//        String salt = UUIDUtils.UUID();
//        user.setMobile(loginVo.getMobile());
//        user.setPassword(MD5Utils.MD5Salt(loginVo.getPassword(), salt));
//        user.setSalt(salt);
//        user.setRegisterDate(new Date());
//        user.setLastLoginDate(new Date());
//        userRepository.save(user);
//        return Result.success("");
//    }
//
//    @Override
//    public Result login(HttpServletResponse response, LoginVo loginVo) {
//        User user = userRepository.findByMobile(loginVo.getMobile());
//        // MOBILE_NOT_EXIST
//        if (user == null) {
//            return Result.error(Status.MOBILE_NOT_EXIST);
//        }
//        String dbPass = user.getPassword();
//        String fmPass = MD5Utils.MD5Salt(loginVo.getPassword(), user.getSalt());
//        // PASSWORD_ERROR
//        if (!dbPass.equals(fmPass)) {
//            return Result.error(Status.PASSWORD_ERROR);
//        }
//        String token = UUIDUtils.UUID();
//        addCookieToken(response, token, user);
//        return Result.success("");
//    }
//
//    @Override
//    public Result logout(HttpServletRequest request, HttpServletResponse response) {
//        String token = getCookieToken(request);
//        // COOKIE_ERROR
//        if (token == null) {
//            return Result.error(Status.COOKIE_ERROR);
//        }
//        delCookieToken(response, token);
//        return Result.success("");
//    }
//
//    @Override
//    public Result getUserByToken(HttpServletRequest request, HttpServletResponse response) {
//        String token = getCookieToken(request);
//        // COOKIE_ERROR
//        if (token == null) {
//            return Result.error(Status.COOKIE_ERROR);
//        }
//        // ACCOUNT_NOT_LOGIN
//        User user = (User) redisService.get(token);
//        if (user == null) {
//            return Result.error(Status.NOT_LOGIN);
//        }
//        // 延长token有效期
//        addCookieToken(response, token, user);
//        return Result.success(user);
//    }
//
//    @Override
//    public Result updatePass(HttpServletRequest request, UpdatePassVo updatePassVo) {
//        String token = getCookieToken(request);
//        // COOKIE_ERROR
//        if (token == null) {
//            return Result.error(Status.COOKIE_ERROR);
//        }
//        // ACCOUNT_NOT_LOGIN
//        User user = (User) redisService.get(token);
//        if (user == null) {
//            return Result.error(Status.NOT_LOGIN);
//        }
//        String dbPass = user.getPassword();
//        String fmPass = MD5Utils.MD5Salt(updatePassVo.getOldPass(), user.getSalt());
//        if (!dbPass.equals(fmPass)) {
//            return Result.error(Status.PASSWORD_ERROR);
//        }
//        String newPass = MD5Utils.MD5Salt(updatePassVo.getNewPass(), user.getSalt());
//        user.setPassword(newPass);
//        return Result.success("");
//    }
//
//
//}

package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Constants;
import com.example.demo.utils.MD5Utils;
import com.example.demo.utils.UUIDUtils;
import com.example.demo.vo.LoginVo;
import com.example.demo.vo.UpdatePassVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisService redisService;

    @Override
    public Result register(LoginVo loginVo) {
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
        user.setRegisterDate(new Date());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        return Result.success("");
    }

    @Override
    public Result login(HttpServletResponse response, LoginVo loginVo) {
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
        // 添加token
        redisService.set(token, user, Constants.COOKIE_EXPIRY); // token -> user
        redisService.set(user.getId().toString(), token, Constants.COOKIE_EXPIRY); // id -> token
        // 添加cookie
        addCookie(response, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    @Override
    public Result logout(HttpServletResponse response, User user) {
        // 删除token
        String token = (String) redisService.get(user.getId().toString());
        redisService.del(token);
        redisService.del(user.getId().toString());
        // 删除cookie
        addCookie(response, token, 0);
        return Result.success("");
    }

    @Override
    public Result getUserInfo(User user) {
        return Result.success(user);
    }

    @Override
    public Result updatePass(User user, UpdatePassVo updatePassVo) {
        String dbPass = user.getPassword();
        String fmPass = MD5Utils.MD5Salt(updatePassVo.getOldPass(), user.getSalt());
        // PASSWORD_ERROR
        if (!dbPass.equals(fmPass)) {
            return Result.error(Status.PASSWORD_ERROR);
        }
        String newPass = MD5Utils.MD5Salt(updatePassVo.getNewPass(), user.getSalt());
        user.setPassword(newPass);
        userRepository.save(user);
        String token = (String) redisService.get(user.getId().toString());
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    private void addCookie(HttpServletResponse response, String token, int expiry) {
        Cookie cookie = new Cookie(Constants.COOKIE_TOKEN, token);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
