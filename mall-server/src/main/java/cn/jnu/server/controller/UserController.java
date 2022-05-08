package cn.jnu.server.controller;

import cn.hutool.core.convert.Convert;
import cn.jnu.common.base.Result;
import cn.jnu.common.dto.IdsRequest;
import cn.jnu.mbg.entity.User;
import cn.jnu.server.dto.user.UserAddDTO;
import cn.jnu.server.dto.user.UserEnabledDTO;
import cn.jnu.server.dto.user.UserInfoDTO;
import cn.jnu.server.dto.user.UserPageQuery;
import cn.jnu.server.facade.UserFacade;
import cn.jnu.server.vo.user.UserRecordVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户")
@RestController
@RequestMapping("")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public Result<List<UserRecordVO>> list(@Valid UserPageQuery query) {
        Page<UserRecordVO> page = userFacade.list(query);
        return Result.success(page.getRecords(), page.getTotal());
    }

    @ApiOperation("添加用户")
    @PostMapping("/add")
    public Result<String> save(@RequestBody @Valid UserAddDTO DTO) {
        User user = Convert.convert(User.class, DTO);
        userFacade.save(DTO.getRoleIds(), user);
        return Result.success();
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/updateInfo/{id}")
    public Result<String> updateInfo(@PathVariable("id") Long id,
                                     @RequestBody @Valid UserInfoDTO DTO) {
        userFacade.updateInfo(id, DTO);
        return Result.success();
    }

    @ApiOperation("重置用户密码")
    @PutMapping("/updatePassword/{id}")
    public Result<String> updatePassword(@PathVariable("id") Long id) {
        String initialPwd = "12345678";
        userFacade.updatePassword(id, initialPwd);
        return Result.success();
    }

    @ApiOperation("修改用户状态")
    @PostMapping("/status")
    public Result<String> updateStatus(@RequestBody @Valid UserEnabledDTO DTO) {
        userFacade.updateStatus(DTO);
        return Result.success();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    public Result<String> delete(@RequestBody @Valid IdsRequest request) {
        userFacade.delete(request.getIds());
        return Result.success();
    }
}
