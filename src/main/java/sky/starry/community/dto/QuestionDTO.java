package sky.starry.community.dto;

import lombok.Data;
import sky.starry.community.model.User;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String tag;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private Integer unlikeCount;
    private User user;
}
