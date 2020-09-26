package sky.starry.community.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.starry.community.Exceptiom.CustomizeErrorCode;
import sky.starry.community.Exceptiom.CustomizeException;
import sky.starry.community.dto.NotificationDTO;
import sky.starry.community.dto.PaginationDTO;
import sky.starry.community.enums.NotificationEnum;
import sky.starry.community.enums.NotificationStatusEnum;
import sky.starry.community.mapper.NotificationMapper;
import sky.starry.community.mapper.UserMapper;
import sky.starry.community.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    UserMapper userMapper;

    /*获取未读信息*/
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

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


        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");

        //获取分页数据
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));


        if(notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfValue(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOList);
        return paginationDTO;
    }

    /*获取未读信息数量*/
    public Long unreadCount(Long id) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    /*读取通知信息*/
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfValue(notification.getType()));

        return notificationDTO;
    }
}
