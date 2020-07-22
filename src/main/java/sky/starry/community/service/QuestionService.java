package sky.starry.community.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.starry.community.dto.QuestionDTO;
import sky.starry.community.mapper.QuestionMapper;
import sky.starry.community.mapper.UserMapper;
import sky.starry.community.model.Question;
import sky.starry.community.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper quertionMapper;

    @Autowired
    UserMapper userMapper;


    public List<QuestionDTO> list(){
        List<Question> questions = quertionMapper.list();
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);

            questionDTOS.add(questionDTO);
        }

        return questionDTOS;
    }
}
