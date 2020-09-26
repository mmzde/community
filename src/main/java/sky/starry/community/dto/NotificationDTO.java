package sky.starry.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
/*
* @pram id
* @pram outerid 用作外键，如问题id
* @pram outerTitle
* @pram type 类型 文章或评论
* @pram typeName 枚举String 不想写字
* @pram status 已读/未读标志量
* @pram notifier notificationId的缩减（不想再改表hhhh） 发送人id
* @pram notificationName 发送人姓名
* */
    private Long id;
    private Integer type;
    private String typeName;
    private Long gmtCreate;
    private Integer status;
    private Long outerid;
    private String outerTitle;
    private Long notifier;
    private String notificationName;

}
