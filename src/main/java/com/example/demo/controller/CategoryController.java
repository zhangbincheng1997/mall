package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.CategoryDto;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "分类")
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取分类列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAuthority('category:read')")
    public Result list(@RequestParam(name = "id", defaultValue = "0") Long id) {
        List<CategoryVo> categoryVoList = categoryService.list(id);
        return PageResult.success(categoryVoList);
    }

    @ApiOperation("添加分类")
    @PostMapping("")
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
    @PutMapping("/{id}")
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
