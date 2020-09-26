package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sky.starry.community.dto.PaginationDTO;
import sky.starry.community.mapper.UserExtMapper;
import sky.starry.community.model.User;
import sky.starry.community.service.NotificationService;
import sky.starry.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    UserExtMapper userExtMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/profile/{action}")
    private String profile(@PathVariable(name="action") String action,
                           Model model,
                           HttpServletRequest request,
                           @RequestParam(name = "page",defaultValue = "1")Integer page,
                           @RequestParam(name = "size",defaultValue = "5")Integer size){


        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error","用户未登陆");
            return "redirect:/";
        }

        if("questions".equals(action)){

            PaginationDTO paginationDTO = questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if("repires".equals(action)){
            model.addAttribute("section","repires");
            model.addAttribute("sectionName","最新回复");

            PaginationDTO paginationDTO = notificationService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }




        return "profile";
    }

}
