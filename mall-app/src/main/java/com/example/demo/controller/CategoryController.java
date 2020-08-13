package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.entity.Category;
import com.example.demo.facade.CategoryFacade;
import com.example.demo.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "分类")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryFacade categoryFacade;

    @ApiOperation("获取分类列表")
    @GetMapping("")
    public Result<List<CategoryVo>> list(@RequestParam(name = "id", defaultValue = "0") Long id) {
        // 全部读出 加速搜索
        List<Category> categoryList = categoryFacade.list();
        List<CategoryVo> categoryVoList = get(categoryList, id);
        return Result.success(categoryVoList);
    }

    // 递归调用
    public List<CategoryVo> get(List<Category> categoryList, Long id) {
        List<CategoryVo> categoryVoList = getByPid(categoryList, id);
        categoryVoList.forEach(categoryVo -> categoryVo.setChildren(get(categoryList, categoryVo.getId())));
        return categoryVoList;
    }

    public List<CategoryVo> getByPid(List<Category> categoryList, Long id) {
        return categoryList.stream()
                .filter(category -> category.getPid().equals(id))
                .map(category -> Convert.convert(CategoryVo.class, category))
                .collect(Collectors.toList());
    }
}
