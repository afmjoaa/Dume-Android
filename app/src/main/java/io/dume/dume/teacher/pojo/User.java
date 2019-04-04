package io.dume.dume.teacher.pojo;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int age;
    private String mailAddress;
    private String phoneNumber;
    private String avatarUrl;
    private String maritalStatus;
    private String religion;
    private String gender;
    private static User newUser = null;

    private User() {
    }

    public static User getInstance() {
        if (newUser == null) {
            newUser = new User();
        }
        return newUser;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;

    }

    public String getMailAddress() {
        return mailAddress;
    }

    public User setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public User setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public User setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }


/*  public static class Builder {
        private String name;
        private int age;
        private String mailAddress;
        private String phoneNumber;
        private String avatarUrl;
        private String maritalStatus;
        private String religion;
        private String gender;


        public User build() {
            User user = new User();
            return user;
        }
    }*/
}
