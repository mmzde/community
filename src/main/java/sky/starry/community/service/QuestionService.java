package sky.starry.community.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.starry.community.Exceptiom.CustomizeErrorCode;
import sky.starry.community.Exceptiom.CustomizeException;
import sky.starry.community.dto.PaginationDTO;
import sky.starry.community.dto.QuestionDTO;
import sky.starry.community.mapper.QuestionExtMapper;
import sky.starry.community.mapper.UserExtMapper;
import sky.starry.community.model.Question;
import sky.starry.community.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    QuestionExtMapper questionExtMapper;

    @Autowired
    UserExtMapper userExtMapper;


    public PaginationDTO list(Integer page, Integer size){

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionExtMapper.count();

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
        List<Question> questions = questionExtMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();



        for (Question question : questions) {
            User user = userExtMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);


        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionExtMapper.countByUserId(userId);

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
        List<Question> questions = questionExtMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();



        for (Question question : questions) {
            User user = userExtMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);


        return paginationDTO;
    }

    public QuestionDTO getById(Long id){

        Question question = questionExtMapper.getById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userExtMapper.findByID(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdata(Question question) {
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionExtMapper.create(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            int i = questionExtMapper.update(question);
            if(i != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(1);
        updateQuestion.setId(id);
        questionExtMapper.incView(updateQuestion);
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
        List<Question> questions= questionExtMapper.selectRelated(question);


        List<QuestionDTO> questioDTOTag = questions.stream().map(q ->{
            QuestionDTO questionRelated = new QuestionDTO();
            BeanUtils.copyProperties(q,questionRelated);
            return questionRelated;}).collect(Collectors.toList());
        return questioDTOTag;
    }
}
