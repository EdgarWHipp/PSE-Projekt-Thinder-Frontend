package com.pse.app.thinder.data.model;

import com.google.gson.annotations.SerializedName;

public class Supervisor {
    String firstName;

    String lastName;
    @SerializedName("id")
    int supervisorId;

    String password;

    String eMail;

    String university;
    @SerializedName("degree")
    String academicDegree;

    String location;

    String institute;

    String phoneNumber;

}