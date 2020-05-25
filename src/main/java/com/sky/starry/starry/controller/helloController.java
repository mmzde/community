package com.sky.starry.starry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class helloController {
    @RequestMapping("/")
    public String Hello(Model model){
        return "hellomogf ";
    }
    @RequestMapping("/hello")
    public String Hello(@RequestParam(name = "name") String name, Model model){
        model.addAttribute("name",name);
        return "hello";
    }
}
