package kr.edcan.safood.models;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 10. 20..
 */
public class GroupMemo {
    private String title, content;
    private int color;
    private ArrayList<String> foods;

    public GroupMemo(String title, String content, int color, ArrayList<String> foods) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.foods = foods;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getColor() {
        return color;
    }

    public ArrayList<String> getFoods() {
        return foods;
    }
}
