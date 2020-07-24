package sky.starry.community.Exceptiom;

public class CustomizeException extends RuntimeException{

    private String message;

    public CustomizeException(InCustomizeErrorCode recode) {
        this.message = recode.getMessage();

    }
    @Override
    public String getMessage(){
        return message;
    }
}
