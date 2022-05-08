package cn.jnu.server.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermMapper {

    @Select("<script>" +
            " SELECT DISTINCT(btn_perm)" +
            " FROM permission p, role_permission rp, role r" +
            " WHERE p.btn_perm IS NOT NULL" +
            " AND p.id = rp.permission_id" +
            " AND rp.role_id = r.id" +
            " AND r.value IN" +
            " <foreach collection=\"roles\" item=\"role\" open=\"(\" close=\")\" separator=\",\" >" +
            " #{role}" +
            " </foreach> " +
            "</script>")
    List<String> listBtnPerms(@Param("roles") List<String> roles);
}
