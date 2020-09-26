package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sky.starry.community.dto.NotificationDTO;
import sky.starry.community.enums.NotificationEnum;
import sky.starry.community.model.User;
import sky.starry.community.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/notification/{id}")
    private String profile(@PathVariable(name = "id") Long id,
                           Model model,
                           HttpServletRequest request) {


        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登陆");
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if (NotificationEnum.REPLAR_QUESTION.getType() == notificationDTO.getType() || NotificationEnum.REPLAR_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }


    }
}
