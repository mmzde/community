package sky.starry.community.dto;

import lombok.Data;
import sky.starry.community.model.User;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Integer commenttator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Long commentCount;
    private User user;
}
