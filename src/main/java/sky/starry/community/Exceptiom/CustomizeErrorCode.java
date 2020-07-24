package sky.starry.community.Exceptiom;

public enum CustomizeErrorCode implements InCustomizeErrorCode {

    QUESTION_NOT_FOUND("您找的问题不在了,要不要换一个试试!!");
    public String getMessage(){
        return  message;
    }

    private String message;
    CustomizeErrorCode(String message) {
        this.message = message;
    }


}
