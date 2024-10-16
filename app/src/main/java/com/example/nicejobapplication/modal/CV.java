package com.example.nicejobapplication.modal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CV {
    @Nullable
    private String cvId;
    @Nullable
    private final String cvName;
    @Nullable
    private final String avatar;
    @Nullable
    private final String employerName;
    @Nullable
    private final String email;
    @Nullable
    private final String phoneNumber;
    @Nullable
    private final String gender;
    @Nullable
    private final String address;
    @Nullable
    private final String dayOfBirth;
    @Nullable
    private final String careerGoal;
    @Nullable
    private final String workExperience;
    @Nullable
    private final String academicLevel;
    private final long createdAt;

    public CV(@Nullable String cvId, @Nullable String cvName, @Nullable String avatar, @Nullable String employerName,
              @Nullable String email, @Nullable String phoneNumber, @Nullable String gender, @Nullable String address,
              @Nullable String dayOfBirth, @Nullable String careerGoal, @Nullable String workExperience,
              @Nullable String academicLevel, long createdAt) {
        this.cvId = cvId;
        this.cvName = cvName;
        this.avatar = avatar;
        this.employerName = employerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.dayOfBirth = dayOfBirth;
        this.careerGoal = careerGoal;
        this.workExperience = workExperience;
        this.academicLevel = academicLevel;
        this.createdAt = createdAt;
    }

    @Nullable
    public String getCvId() {
        return cvId;
    }

    public void setCvId(@Nullable String cvId) {
        this.cvId = cvId;
    }

    @Nullable
    public String getCvName() {
        return cvName;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    @Nullable
    public String getEmployerName() {
        return employerName;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    public String getGender() {
        return gender;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getDayOfBirth() {
        return dayOfBirth;
    }

    @Nullable
    public String getCareerGoal() {
        return careerGoal;
    }

    @Nullable
    public String getWorkExperience() {
        return workExperience;
    }

    @Nullable
    public String getAcademicLevel() {
        return academicLevel;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @NotNull
    @Override
    public String toString() {
        return "CV{" +
                "cvId='" + cvId + '\'' +
                ", cvName='" + cvName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", employerName='" + employerName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", careerGoal='" + careerGoal + '\'' +
                ", workExperience='" + workExperience + '\'' +
                ", academicLevel='" + academicLevel + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (cvId != null ? cvId.hashCode() : 0);
        result = 31 * result + (cvName != null ? cvName.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (employerName != null ? employerName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (dayOfBirth != null ? dayOfBirth.hashCode() : 0);
        result = 31 * result + (careerGoal != null ? careerGoal.hashCode() : 0);
        result = 31 * result + (workExperience != null ? workExperience.hashCode() : 0);
        result = 31 * result + (academicLevel != null ? academicLevel.hashCode() : 0);
        result = 31 * result + Long.hashCode(createdAt);
        return result;
    }
}
