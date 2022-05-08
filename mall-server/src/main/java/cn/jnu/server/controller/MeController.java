package cn.jnu.server.controller;

import cn.hutool.core.convert.Convert;
import cn.jnu.common.base.GlobalException;
import cn.jnu.common.base.Result;
import cn.jnu.common.base.ResultCode;
import cn.jnu.mbg.entity.User;
import cn.jnu.security.jwt.JwtUtils;
import cn.jnu.server.dto.user.UpdatePwdDTO;
import cn.jnu.server.dto.user.UserInfoDTO;
import cn.jnu.server.facade.UserFacade;
import cn.jnu.server.mapper.PermMapper;
import cn.jnu.server.vo.user.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "个人")
@RestController
@RequestMapping("/me")
@AllArgsConstructor
public class MeController {

    private final PasswordEncoder passwordEncoder;
    private final PermMapper permMapper;
    private final UserFacade userFacade;

    @ApiOperation("获取个人信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        // 当前登录角色基本信息
        Long userId = JwtUtils.getUserId();
        UserInfoVO userInfoVo = Convert.convert(UserInfoVO.class, userFacade.getById(userId));

        // 当前登录角色拥有的角色
        List<String> roles = JwtUtils.getRoles();
        userInfoVo.setRoles(roles);

        // 当前登录角色拥有的按钮权限（接口权限不对外提供）
        List<String> perms = permMapper.listBtnPerms(JwtUtils.getRoles());
        userInfoVo.setPerms(perms);
        return Result.success(userInfoVo);
    }

    @ApiOperation("修改个人信息")
    @PutMapping("/info")
    public Result<String> updateInfo(@RequestBody @Valid UserInfoDTO DTO) {
        Long userId = JwtUtils.getUserId();
        userFacade.updateInfo(userId, DTO);
        return Result.success();
    }

    @ApiOperation("修改个人密码")
    @PutMapping("/password")
    public Result<String> updatePassword(@RequestBody @Valid UpdatePwdDTO DTO) {
        Long userId = JwtUtils.getUserId();
        User user = userFacade.getById(userId);
        if (!passwordEncoder.matches(DTO.getOldPwd(), user.getPassword())) {
            throw new GlobalException(ResultCode.BAD_CREDENTIALS);
        }
        userFacade.updatePassword(userId, DTO.getNewPwd());
        return Result.success();
    }
}
