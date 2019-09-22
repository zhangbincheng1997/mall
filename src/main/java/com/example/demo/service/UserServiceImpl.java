package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.model.UserExample;
import com.example.demo.utils.Constants;
import com.example.demo.utils.CookieUtils;
import com.example.demo.utils.MD5Utils;
import com.example.demo.utils.UUIDUtils;
import com.example.demo.vo.UserInfoVo;
import com.example.demo.vo.UserVo;
import com.example.demo.vo.UpdatePassVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    @Override
    public Result register(UserVo loginVo) {
        UserExample ex = new UserExample();
        ex.createCriteria().andUsernameEqualTo(loginVo.getUsername());
        List<User> userList = userMapper.selectByExample(ex);
        // EMAIL_EXIST
        if (userList.size() != 0) {
            return Result.error(Status.EMAIL_EXIST);
        }
        User user = new User();
        user.setUsername(loginVo.getUsername());
        String salt = UUIDUtils.UUID();
        user.setPassword(MD5Utils.MD5Salt(loginVo.getPassword(), salt));
        user.setSalt(salt);
//
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateStr = format.format(date);

        userMapper.insert(user);
        return Result.success("");
    }

    /**
     * redis
     * username -> token
     * token -> user
     * <p>
     * 异地登陆 https://www.cnblogs.com/zuoxh/p/10238594.html
     * 将token作为value，账户的id作为key
     * <p>
     * 每次登录都去redis中查询该账户的登录是否过期，没有过期则删掉原来的id，token，将新生成token作为value存入redis中。
     * 过期则没有该账户信息，则重新存入redis中
     * <p>
     * 用户每次请求接口都需要验证是否在登录状态。（这里需要一个filter或则intercepter）获取token。解析token。
     * 将id从token中解析出来去。然后将用户的id作为key去redis中查询token。
     * <p>
     * 查询为空则表示登录过期。不为空则将解析出来的token和redis中的token作对比，如果相同，则用户状态正常则继续请求接口。
     * 如果不相同，则账号在其他设备登录.
     *
     * @param response
     * @param loginVo
     * @return
     */
    @Override
    public Result login(HttpServletResponse response, UserVo loginVo) {
        UserExample ex = new UserExample();
        ex.createCriteria().andUsernameEqualTo(loginVo.getUsername());
        List<User> userList = userMapper.selectByExample(ex);
        // EMAIL_NOT_EXIST
        if (userList.size() == 0) {
            return Result.error(Status.EMAIL_EXIST);
        }
        User user = userList.get(0);
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
        return Result.success(user);
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
        userMapper.updateByPrimaryKeySelective(user);
        // 重新设置token、cookie时间
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    @Override
    public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, UserInfoVo userInfoVo) {
        String token = CookieUtils.getCookie(request, Constants.COOKIE_TOKEN);
        User user = (User) redisService.get(token);

        // userInfoVo => user
//        BeanUtils.copyProperties(userInfoVo, user);

        if (userInfoVo.getNickname() != null) {
            user.setNickname(userInfoVo.getNickname());
        }
        if (userInfoVo.getBirthday() != null) {
            user.setBirthday(userInfoVo.getBirthday());
        }
        if (userInfoVo.getGender() != null) {
            user.setGender(userInfoVo.getGender());
        }
        if (userInfoVo.getIcon() != null) {
            user.setIcon(userInfoVo.getIcon());
        }

        userMapper.updateByPrimaryKeySelective(user);
        // 重新设置token、cookie时间
        redisService.set(token, user, Constants.COOKIE_EXPIRY);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
        return Result.success("");
    }

    // updateByPrimaryKeySelective 部分更新
    // updateByPrimaryKey 全部更新
}
