package kr.edcan.safood.models;

/**
 * Created by Junseok on 2016-10-21.
 */

public class History {
    private String foodname, thumbnail, date;

    public History(String foodname, String thumbnail, String date) {
        this.foodname = foodname;
        this.thumbnail = thumbnail;
        this.date = date;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDate() {
        return date;
    }
}
