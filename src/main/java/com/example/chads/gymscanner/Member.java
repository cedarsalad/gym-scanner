package com.example.chads.gymscanner;

/**
 * Created by Nathaniel Reid on 2018-02-28.
 */

public class Member {

    private int id;
    private String avatar;
    private String firstName;
    private String lastName;
    private String dob;
    private int age;
    private String address;
    private String city;
    private String province;
    private String barcode;
    private int visits;
    private boolean status;


    public Member(int id, String avatar, String firstName, String lastName, String dob, int age, String address, String city, String province, String barcode, int visits, boolean status){
        this.id = id;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.age = age;
        this.address = address;
        this.city = city;
        this.province = province;
        this.barcode = barcode;
        this.visits = visits;
        this.status = status;
    }

    public Member (){}

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + dob + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", barcode='" + barcode + '\'' +
                ", visits=" + visits +
                ", status=" + status +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}