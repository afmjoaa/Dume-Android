package io.dume.dume.auth;

import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;

public class DataStore implements Serializable {
    public static String TEACHER = "teacher";
    public static String STUDENT = "student";
    public static int STATION = 1;
    private String firstName = null;
    private String lastName = null;
    private String accountManjor = "student";
    private String phoneNumber = null;
    public static transient PhoneAuthProvider.ForceResendingToken resendingToken = null;
    private static DataStore dataStoreObj;
    private String verificationId = null;
    private String email = null;
    private String photoUrl = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhotoUri() {
        return photoUrl == null ? "" : photoUrl;
    }

    public void setPhotoUri(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getVerificationId() {
        return verificationId;
    }

    private DataStore() {
        // this constructor is projected by private modifier
    }

    public static DataStore getInstance() {
        if (dataStoreObj == null) {
            dataStoreObj = new DataStore();
        }
        return dataStoreObj;
    }

    public static int getSTATION() {
        return STATION;
    }

    public static void setSTATION(int STATION) {
        DataStore.STATION = STATION;
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

    public String getAccountManjor() {
        return accountManjor;
    }

    public void setAccountManjor(String accountManjor) {
        this.accountManjor = accountManjor;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
