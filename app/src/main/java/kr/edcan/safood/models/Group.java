package kr.edcan.safood.models;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 10. 19..
 */
public class Group {
    private String groupname, groupid, admin;
    private ArrayList<String> members;

    public Group(String groupname, String groupid, String admin, ArrayList<String> members) {
        this.groupname = groupname;
        this.groupid = groupid;
        this.admin = admin;
        this.members = members;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getAdmin() {
        return admin;
    }

    public ArrayList<String> getMembers() {
        return members;
    }
}
