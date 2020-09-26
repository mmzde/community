package sky.starry.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer pageCount;

    public void setPagination(Integer pageCount, Integer page) {
        this.page = page;
        this.pageCount = pageCount;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {

            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=pageCount){
                pages.add(page+i);
            }
        }


        if(page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }
        if(page.equals(pageCount)){
            showNext = false;
        }else{
            showNext = true;
        }

        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
        if(pages.contains(pageCount)){
            showEndPage = false;
        }else{
            showEndPage  = true;
        }
    }
}
