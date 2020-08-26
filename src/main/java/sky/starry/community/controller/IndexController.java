package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sky.starry.community.dto.PaginationDTO;
import sky.starry.community.dto.QuestionDTO;
import sky.starry.community.mapper.UserMapper;
import sky.starry.community.model.Question;
import sky.starry.community.model.User;
import sky.starry.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String Hello(Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "10")Integer size){


        PaginationDTO paginationDTO = questionService.list(page,size);
        model.addAttribute("pagination",paginationDTO);

        return "index";
    }


    @RequestMapping("/hello")
    public String Hello(@RequestParam(name = "name",defaultValue = "姓名七个字") String name,
                        Model model){
        model.addAttribute("name",name);
        return "hello";
    }
}
