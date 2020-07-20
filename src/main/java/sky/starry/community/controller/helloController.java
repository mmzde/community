package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sky.starry.community.mapper.UserMapper;
import sky.starry.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class helloController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String Hello(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);

                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        return "index";
    }
    @RequestMapping("/hello")
    public String Hello(@RequestParam(name = "name",defaultValue = "姓名七个字") String name,
                        Model model){
        model.addAttribute("name",name);
        return "hello";
    }
}
