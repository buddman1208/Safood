package kr.edcan.safood.models;

import java.util.ArrayList;

/**
 * Created by JunseokOh on 2016. 9. 24..
 */
public class User {
    private String email, name, phone, auth_token, reservation, _id;
    private ArrayList<String> reservation_waiting;

    public User( String email, String name, String phone, String auth_token) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.auth_token = auth_token;
    }

    public User(String email, String name, String phone, String auth_token, String reservation) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.auth_token = auth_token;
        this.reservation = reservation;
    }

    public User(String email, String name, String phone, String auth_token, String reservation, ArrayList<String> reservation_waiting) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.auth_token = auth_token;
        this.reservation = reservation;
        this.reservation_waiting = reservation_waiting;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public String getReservation() {
        return reservation;
    }


    public ArrayList<String> getReservation_waiting() {
        return reservation_waiting;
    }

    public String get_id() {
        return _id;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setReservation_waiting(ArrayList<String> reservation_waiting) {
        this.reservation_waiting = reservation_waiting;
    }
}
