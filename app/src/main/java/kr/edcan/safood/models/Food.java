package kr.edcan.safood.models;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 10. 20..
 */
public class Food {
    private String name, thumbnail, barcode, foodAllergic, foodIngredient, foodid, foodName;
    private ArrayList<String> allergy;

    public Food(String name, String thumbnail, String barcode, String foodAllergic, String foodIngredient, String foodid, String foodName, ArrayList<String> allergy) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.barcode = barcode;
        this.foodAllergic = foodAllergic;
        this.foodIngredient = foodIngredient;
        this.foodid = foodid;
        this.foodName = foodName;
        this.allergy = allergy;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getFoodAllergic() {
        return foodAllergic;
    }

    public String getFoodIngredient() {
        return foodIngredient;
    }

    public String getFoodid() {
        return foodid;
    }

    public String getFoodName() {
        return foodName;
    }

    public ArrayList<String> getAllergy() {
        return allergy;
    }
}
