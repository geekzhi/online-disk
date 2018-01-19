package com.geekzhang.demo.controller;

import com.geekzhang.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {

    @Autowired
    UserServiceImpl userService;
}
