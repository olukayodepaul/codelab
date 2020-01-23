package com.mobbile.paul.shrine.models;

/**
 * Created by Ehigiator David on 05/05/2019.
 */
public class UserProfile {

    private String userName;

    private String userId;

    private String userCompany;

    private String userEmail;

    public static String USER_DB_REFERENCE = "Clients";

    public UserProfile() {
    }

    public UserProfile(String userName, String userId, String userCompany, String userEmail) {
        this.userName = userName;
        this.userId = userId;
        this.userCompany = userCompany;
        this.userEmail = userEmail;
    }
}
