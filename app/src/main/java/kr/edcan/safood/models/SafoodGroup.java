package kr.edcan.safood.models;

import java.util.ArrayList;

/**
 * Created by Junseok on 2016-10-22.
 */
public class SafoodGroup {
    private String id, name, admin;
    private int color;
    private ArrayList<Food> foodList;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAdmin() {
        return admin;
    }

    public int getColor() {
        return color;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public SafoodGroup(String id, String name, String admin, int color, ArrayList<Food> foodList) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.color = color;
        this.foodList = foodList;

    }
}
