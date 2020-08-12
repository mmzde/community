package sky.starry.community.dto;

import lombok.Data;
import sky.starry.community.Exceptiom.CustomizeErrorCode;
import sky.starry.community.Exceptiom.CustomizeException;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;
    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setCode(code);
        resultDto.setMessage(message);

        return resultDto;
    }

    public static ResultDTO errorOf(CustomizeErrorCode recode) {
        return errorOf(recode.getCode(),recode.getMessage());
    }

    public static ResultDTO successOf(){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");

        return resultDto;
    }
    public static <T> ResultDTO successOf(T t){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);
        return resultDto;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }
}
