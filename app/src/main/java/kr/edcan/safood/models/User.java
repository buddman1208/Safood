package kr.edcan.safood.models;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 9. 24..
 */
public class User {
    private String userid, username, profileImage, apikey, groupid;
    private ArrayList<History> history;
    private Exception exception;

    public User(String userid, String username, String profileImage, String apikey, String groupid, ArrayList<History> history, Exception exception) {
        this.userid = userid;
        this.username = username;
        this.profileImage = profileImage;
        this.apikey = apikey;
        this.groupid = groupid;
        this.history = history;
        this.exception = exception;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getApikey() {
        return apikey;
    }

    public String getGroupid() {
        return groupid;
    }

    public ArrayList<History> getHistory() {
        return history;
    }

    public Exception getException() {
        return exception;
    }

    public class Exception{
        private ArrayList<String> religion, allergy, custom;

        public Exception(ArrayList<String> religion, ArrayList<String> allergy, ArrayList<String> custom) {
            this.religion = religion;
            this.allergy = allergy;
            this.custom = custom;
        }

        public ArrayList<String> getReligion() {
            return religion;
        }

        public ArrayList<String> getAllergy() {
            return allergy;
        }

        public ArrayList<String> getCustom() {
            return custom;
        }
    }
}
