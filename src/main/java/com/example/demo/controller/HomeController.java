package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String IndexPage() {
        return "index";
    }

    @GetMapping("/home")
    @ResponseBody
    public Result home(User user) {
        return Result.success(user);
    }
}
