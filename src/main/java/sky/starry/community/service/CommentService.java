package sky.starry.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.starry.community.Exceptiom.CustomizeErrorCode;
import sky.starry.community.Exceptiom.CustomizeException;
import sky.starry.community.dto.CommentDTO;
import sky.starry.community.enums.CommentTypeEnum;
import sky.starry.community.enums.NotificationEnum;
import sky.starry.community.enums.NotificationStatusEnum;
import sky.starry.community.mapper.*;
import sky.starry.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    QuestionExtMapper questionExtMapper;

    @Autowired
    CommentExtMapper commentExtMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    NotificationMapper notificationMapper;

    @Transactional
    public void insertComment(Comment comment,User commentCreator) {

        if(comment.getParentId() == null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || comment.getType() == 0){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_NOT_FOUND);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentExtMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            //获取父评论的问题
            Question question = questionExtMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentExtMapper.insertComment(comment);

            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1L);
            commentExtMapper.incCommentCount(parentComment);

            //创建通知
            createNotify(comment,dbComment.getCommenttator(), NotificationEnum.REPLAR_COMMENT,commentCreator.getName(),question.getTitle(),dbComment.getParentId());
        }else {
            //回复问题
            Question question = questionExtMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentExtMapper.insertComment(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            //创建通知
            createNotify(comment,question.getCreator(), NotificationEnum.REPLAR_QUESTION,commentCreator.getName(),question.getTitle(),question.getId());
        }
    }

    //创建回复消息
    private void createNotify(Comment comment, Long receiver, NotificationEnum notificationEnum, String notificationName, String outerTitle, Long outerid) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationEnum.getType());
        notification.setOuterid(outerid);
        notification.setNotifier(comment.getCommenttator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotificationName(notificationName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size() == 0){
            return new ArrayList<>();
        }

//        获取去重评论人
//        JAVA8新特性快捷获取comments中的commenttator,
//        快捷原语句为commenttators = comments.stream().map(comments -> getCommenttator()).collect(Collestors.toSet),然后转到userIds
        List<Long> userIds = comments.stream().map(Comment::getCommenttator).distinct().collect(Collectors.toList());

//        获取评论人并转化为map
        UserExample userexample = new UserExample();
        userexample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userexample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
//      获取comment转化为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommenttator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
