package cn.jnu.server.mapper;

import cn.jnu.server.dto.user.UserPageQuery;
import cn.jnu.server.vo.user.RoleVO;
import cn.jnu.server.vo.user.UserInfoVO;
import cn.jnu.server.vo.user.UserRecordVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Service
public interface UserPageMapper {

    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roles", column = "id", many = @Many(select = "getRole"))
    })
    @Select("<script>" +
            "SELECT u.* FROM user AS u" +
            "<if test=\"query.roleName != null and query.roleName != ''\">" +
            " LEFT JOIN user_role AS ur ON u.id = ur.user_id" +
            " LEFT JOIN role AS r ON ur.role_id = r.id" +
            "</if>" +
            " WHERE 1=1" +
            "<if test=\"query.roleName != null and query.roleName != ''\">" +
            " AND r.name LIKE concat('%', #{query.roleName}, '%')" +
            "</if>" +
            "<if test=\"query.name != null and query.name != ''\">" +
            " AND u.name LIKE concat('%', #{query.name}, '%')" +
            "</if>" +
            "<if test=\"query.company != null and query.company != ''\">" +
            " AND u.company LIKE concat('%', #{query.company}, '%')" +
            "</if>" +
            "<if test=\"query.department != null and query.department != ''\">" +
            " AND u.department LIKE concat('%', #{query.department}, '%')" +
            "</if>" +
            "<if test=\"query.status != null\">" +
            " AND u.status = #{query.status}" +
            "</if>" +
            "<if test=\"query.startTime != null\">" +
            " AND <![CDATA[u.create_time >= #{query.startTime}]]>" +
            "</if>" +
            "<if test=\"query.endTime != null\">" +
            " AND <![CDATA[u.create_time <= #{query.endTime}]]>" +
            "</if>" +
            "<if test=\"query.keyword != null and query.keyword != ''\">" +
            " AND (u.name LIKE concat('%', #{query.keyword}, '%')" +
            " OR u.phone LIKE concat('%', #{query.keyword}, '%')" +
            " OR u.email LIKE concat('%', #{query.keyword}, '%')" +
            " OR u.company LIKE concat('%', #{query.keyword}, '%')" +
            " OR u.department LIKE concat('%', #{query.keyword}, '%'))" +
            "</if>" +
            "<if test=\"query.roleName != null and query.roleName != ''\">" +
            " GROUP BY u.id" +
            "</if>" +
            "<if test=\"query.sort\">" +
            " ORDER BY u.create_time DESC" +
            "</if>" +
            "</script>")
    Page<UserRecordVO> page(Page<UserInfoVO> page, @Param("query") UserPageQuery query);

    @Select("SELECT r.* FROM role r" +
            " LEFT JOIN user_role ur ON r.id = ur.role_id" +
            " WHERE ur.user_id = #{userId}")
    RoleVO getRole(@Param("userId") String userId);

    // 注：
    // <![CDATA[...]]> 是为了转义 <、<=、>、>=符号
    // #预编译
    // $填充
}
