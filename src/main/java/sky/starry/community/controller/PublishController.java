package sky.starry.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sky.starry.community.cache.TagCache;
import sky.starry.community.dto.QuestionDTO;
import sky.starry.community.mapper.QuestionExtMapper;
import sky.starry.community.model.Question;
import sky.starry.community.model.User;
import sky.starry.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PublishController {

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        QuestionDTO question =  questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags",TagCache.get());

        return "publish";

    }
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title",required = false)String title,
            @RequestParam(value = "description",required = false)String description,
            @RequestParam(value = "tag",required = false)String tag,
            @RequestParam(value = "id",required = false) Long id,
            HttpServletRequest request,
            Model model,
            HttpSession session
    ){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        //限制表格内容（后续会改用Ajax）
        if(title == null || title.equals("")){
            model.addAttribute("error","标题不能为空");
            return "/publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","内容不能为空");
            return "/publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error","标签不能为空");
            return "/publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","标签不符合规范");
            return "/publish";
        }

        User user = (User)request.getSession().getAttribute("user");

        if(user == null){
            model.addAttribute("error","用户未登陆");
            return "/publish";
        }

        //输入信息无误，添加文章数据
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setId(id);

        questionService.createOrUpdata(question);
        return "redirect:/";
    }
}
