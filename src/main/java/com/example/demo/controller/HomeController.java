package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.entity.User;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String IndexPage() {
        return "index";
    }

    @RequestMapping("home")
    @ResponseBody
    public Result home(User user) {
        return Result.success(user);
    }
}
