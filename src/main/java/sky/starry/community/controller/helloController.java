package sky.starry.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class helloController {
    @RequestMapping("/")
    public String Hello(Model model){
        return "index";
    }
    @RequestMapping("/hello")
    public String Hello(@RequestParam(name = "name",defaultValue = "姓名七个字") String name, Model model){
        model.addAttribute("name",name);
        return "hello";
    }
}
