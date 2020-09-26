package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sky.starry.community.dto.PaginationDTO;
import sky.starry.community.mapper.UserExtMapper;
import sky.starry.community.service.QuestionService;

@Controller
public class IndexController {
    @Autowired
    private UserExtMapper userExtMapper;

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
