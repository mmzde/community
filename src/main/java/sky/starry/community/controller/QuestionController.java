package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sky.starry.community.dto.CommentDTO;
import sky.starry.community.dto.QuestionDTO;
import sky.starry.community.enums.CommentTypeEnum;
import sky.starry.community.model.Question;
import sky.starry.community.service.CommentService;
import sky.starry.community.service.QuestionService;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    private String Qusertion(Model model,
                             @PathVariable("id") Long id){

        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestion = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOS);
        model.addAttribute("relatedQuestions",relatedQuestion);

        return "question";
    }
}
