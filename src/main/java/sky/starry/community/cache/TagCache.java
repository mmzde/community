package sky.starry.community.cache;

import org.apache.commons.lang3.StringUtils;
import sky.starry.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","css","php","html","java","html5","node.js","python","c","c++","golang","c#"));
        tagDTOS.add(program);

        TagDTO frame = new TagDTO();
        frame.setCategoryName("平台框架");
        frame.setTags(Arrays.asList("spring","laravel","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDTOS.add(frame);

        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags,",");
        List<TagDTO> list = get();
        List<String> tagList = list.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return invalid;

    }
}
