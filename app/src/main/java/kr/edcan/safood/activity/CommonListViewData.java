package kr.edcan.safood.activity;

/**
 * Created by Junseok on 2016-10-16.
 */
public class CommonListViewData {
    private String title, content;

    public CommonListViewData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
