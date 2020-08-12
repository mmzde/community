package sky.starry.community.Exceptiom;

public enum CustomizeErrorCode implements InCustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"您找的问题不在了,要不要换一个试试!!"),
    COMMENT_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYSTEM_ERROE(2004,"服务器冒烟了，亲请稍后再试！！！"),
    TYPE_PARAM_NOT_FOUND(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"您回复的评论不在了,要不要换一个试试!!"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空");
    ;




    @Override
    public String getMessage(){
        return  message;
    }

    @Override
    public Integer getCode() {return code; }

    private Integer code;
    private String message;


    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }


}
