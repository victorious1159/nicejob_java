package com.example.nicejobapplication.modal;

import org.jetbrains.annotations.NotNull;

public final class Employee {
    @NotNull
    private final String employeeID;
    @NotNull
    private final String fullName;
    private final int gender;
    @NotNull
    private final String phoneNumber;
    @NotNull
    private final String email;
    @NotNull
    private final String avatar;
    @NotNull
    private final String userId;

    public Employee(@NotNull String employeeID, @NotNull String fullName, int gender, @NotNull String phoneNumber,
                    @NotNull String email, @NotNull String avatar, @NotNull String userId) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatar = avatar;
        this.userId = userId;
    }

    @NotNull
    public String getEmployeeID() {
        return employeeID;
    }

    @NotNull
    public String getFullName() {
        return fullName;
    }

    public int getGender() {
        return gender;
    }

    @NotNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public String getAvatar() {
        return avatar;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID='" + employeeID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
