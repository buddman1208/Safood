package kr.edcan.safood.models;

import java.util.Date;

/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class SafoodTitleData {
    private String title;
    private int contentSize;
    private Date lastModifiedDate;

    public SafoodTitleData(String title, int contentSize, Date lastModifiedDate) {
        this.title = title;
        this.contentSize = contentSize;
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public int getContentSize() {
        return contentSize;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
}
