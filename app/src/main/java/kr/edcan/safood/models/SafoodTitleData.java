package kr.edcan.safood.models;

import java.util.Date;

/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class SafoodTitleData {
    private String title;
    private int contentSize;

    public SafoodTitleData(String title, int contentSize) {
        this.title = title;
        this.contentSize = contentSize;
    }

    public String getTitle() {
        return title;
    }

    public int getContentSize() {
        return contentSize;
    }

}