package cn.jnu.security.jwt;

import cn.jnu.mbg.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class JwtUtils {

    /**
     * 获得当前认证信息
     */
    public static JwtUserDetails getDetail() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof JwtUserDetails ? (JwtUserDetails) authentication.getPrincipal() : null;
    }

    /**
     * 角色列表
     */
    public static List<String> getRoles() {
        JwtUserDetails detail = getDetail();
        return detail != null ? detail.getRoleList() : null;
    }

    /**
     * 用户
     */
    public static User getUser() {
        JwtUserDetails detail = getDetail();
        return detail != null ? detail.getUser() : null;
    }

    /**
     * 用户ID
     */
    public static Long getUserId() {
        User user = getUser();
        return user != null ? user.getId() : 0L;
    }

    /**
     * 用户名
     */
    public static String getUsername() {
        User user = getUser();
        return user != null ? user.getUsername() : "*";
    }
}
