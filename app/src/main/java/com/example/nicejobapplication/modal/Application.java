package com.example.nicejobapplication.modal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Application {

    @NotNull
    private String applicationId;
    @NotNull
    private String employeeId;
    @NotNull
    private String cvId;
    @Nullable
    private String introduction;
    @NotNull
    private String jobId;

    public Application(@NotNull String applicationId, @NotNull String employeeId, @NotNull String cvId, @Nullable String introduction, @NotNull String jobId) {
        this.applicationId = applicationId;
        this.employeeId = employeeId;
        this.cvId = cvId;
        this.introduction = introduction;
        this.jobId = jobId;
    }

    // Getters
    @NotNull
    public String getApplicationId() {
        return applicationId;
    }

    @NotNull
    public String getEmployeeId() {
        return employeeId;
    }

    @NotNull
    public String getCvId() {
        return cvId;
    }

    @Nullable
    public String getIntroduction() {
        return introduction;
    }

    @NotNull
    public String getJobId() {
        return jobId;
    }

    // Setters
    public void setApplicationId(@NotNull String applicationId) {
        this.applicationId = applicationId;
    }

    public void setEmployeeId(@NotNull String employeeId) {
        this.employeeId = employeeId;
    }

    public void setCvId(@NotNull String cvId) {
        this.cvId = cvId;
    }

    public void setIntroduction(@Nullable String introduction) {
        this.introduction = introduction;
    }

    public void setJobId(@NotNull String jobId) {
        this.jobId = jobId;
    }
}
