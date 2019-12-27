package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.utils.ConvertUtils;
import com.example.demo.vo.CategoryVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "分类")
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取分类")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('category:read')")
    public Result get(@PathVariable("id") Long id) {
        Category category = categoryService.get(id);
        return Result.success(ConvertUtils.convert(category, CategoryVo.class));
    }

    @ApiOperation("获取分类列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAuthority('category:read')")
    public Result list(@Valid PageRequest pageRequest) {
        PageInfo<Category> pageInfo = categoryService.list(pageRequest);
        List<Category> categoryList = pageInfo.getList();
        List<CategoryVo> categoryVoList = categoryList.stream()
                .map(category -> ConvertUtils.convert(category, CategoryVo.class))
                .collect(Collectors.toList());
        return PageResult.success(categoryVoList, pageInfo.getTotal());
    }

    @ApiOperation("添加分类")
    @PutMapping("")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result add(@Valid CategoryDto categoryDto) {
        int count = categoryService.add(categoryDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation("修改分类")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('category:update')")
    public Result update(@PathVariable("id") Long id,
                         @Valid CategoryDto categoryDto) {
        int count = categoryService.update(id, categoryDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('category:delete')")
    public Result delete(@PathVariable("id") Long id) {
        int count = categoryService.delete(id);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
