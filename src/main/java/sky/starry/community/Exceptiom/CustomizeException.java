package sky.starry.community.Exceptiom;

public class CustomizeException extends RuntimeException{

    private Integer code;
    private String message;

    public CustomizeException(InCustomizeErrorCode recode) {
        this.code = recode.getCode();
        this.message = recode.getMessage();
    }
    @Override
    public String getMessage(){
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
