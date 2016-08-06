package kr.edcan.safood.models;

import java.util.Date;

/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class SafoodContentData {
    private String title;
    private Date addedDate;

    public SafoodContentData(String title, Date addedDate) {
        this.title = title;
        this.addedDate = addedDate;
    }

    public String getTitle() {
        return title;
    }

    public Date getAddedDate() {
        return addedDate;
    }
}
