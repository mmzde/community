package sky.starry.community.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.starry.community.Exceptiom.CustomizeErrorCode;
import sky.starry.community.Exceptiom.CustomizeException;
import sky.starry.community.dto.PaginationDTO;
import sky.starry.community.dto.QuestionDTO;
import sky.starry.community.mapper.QuestionMapper;
import sky.starry.community.mapper.UserMapper;
import sky.starry.community.model.Question;
import sky.starry.community.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size){

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();

        Integer pageCount;
        //确认页数
        if (totalCount%size == 0){
            pageCount = totalCount/size;
        }else {
            pageCount = totalCount/size+1;
        }

        if(page>pageCount){
            page = pageCount;
        }
        if(page <1){
            page=1;
        }

        paginationDTO.setPagination(pageCount,page);



        //size*(page-1)
        Integer offset = size*(page-1);

        //获取分页数据
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();



        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);


        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);

        Integer pageCount;
        //确认页数
        if (totalCount%size == 0){
            pageCount = totalCount/size;
        }else {
            pageCount = totalCount/size+1;
        }

        if(page>pageCount){
            page = pageCount;
        }
        if(page <1){
            page=1;
        }

        paginationDTO.setPagination(pageCount,page);
        //size*(page-1)
        Integer offset = size*(page-1);

        //获取分页数据
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();



        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);


        return paginationDTO;
    }

    public QuestionDTO getById(Long id){

        Question question = questionMapper.getById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findByID(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdata(Question question) {
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            int i = questionMapper.update(question);
            if(i != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(1);
        updateQuestion.setId(id);
        questionMapper.incView(updateQuestion);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }

        Question question = new Question();
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexpTag = String.join("|", tags);
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions= questionMapper.selectRelated(question);


        List<QuestionDTO> questioDTOTag = questions.stream().map(q ->{
            QuestionDTO questionRelated = new QuestionDTO();
            BeanUtils.copyProperties(q,questionRelated);
            return questionRelated;}).collect(Collectors.toList());
        return questioDTOTag;
    }
}
