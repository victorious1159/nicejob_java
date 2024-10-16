package com.example.nicejobapplication.modal;

import com.google.firebase.Timestamp;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Jobs {
    private String jobID;
    private String jobName;
    private String corpID;
    private int numOfRecruit;
    private int genderJob;
    private String[] workAddress;
    private int careerId;
    private int expId;
    private int wayToWorkId;
    private int salaryId;
    private int levelId;
    private int state;
    private Timestamp createdAt;
    private Timestamp expertDay;
    private String[] jobDescription;
    private String[] recruitRequire;
    private String[] benefit;

    public Jobs(String jobID, String jobName, String corpID, int numOfRecruit, int genderJob, String[] workAddress,
                int careerId, int expId, int wayToWorkId, int salaryId, int levelId, int state, Timestamp createdAt,
                Timestamp expertDay, String[] jobDescription, String[] recruitRequire, String[] benefit) {
        this.jobID = jobID;
        this.jobName = jobName;
        this.corpID = corpID;
        this.numOfRecruit = numOfRecruit;
        this.genderJob = genderJob;
        this.workAddress = workAddress;
        this.careerId = careerId;
        this.expId = expId;
        this.wayToWorkId = wayToWorkId;
        this.salaryId = salaryId;
        this.levelId = levelId;
        this.state = state;
        this.createdAt = createdAt;
        this.expertDay = expertDay;
        this.jobDescription = jobDescription;
        this.recruitRequire = recruitRequire;
        this.benefit = benefit;
    }

    public Jobs(String jobID, String jobName, String corpID, int expId, int salaryId, String[] workAddress, Timestamp deadline) {
        this(jobID, jobName, corpID, 0, 0, workAddress, 0, expId, 0, salaryId, 0, 0, null, deadline, null, null, null);
    }

    public Jobs(String jobID, String jobName, String corpID) {
        this(jobID, jobName, corpID, 0, 0, null, 0, 0, 0, 0, 0, 0, null, null, null, null, null);
    }

    public String displayJobDescription(String[] jobDescription) {
        if (jobDescription != null) {
            return String.join("\n", jobDescription).replace("[", "").replace("]", "");
        } else {
            return "Chưa cập nhật";
        }
    }

    public String displayRecruitRequire(String[] recruitRequire) {
        if (recruitRequire != null) {
            return String.join("\n", recruitRequire).replace("[", "").replace("]", "");
        } else {
            return "Chưa cập nhật";
        }
    }

    public String displayBenefit(String[] benefit) {
        if (benefit != null) {
            return String.join("\n", benefit).replace("[", "").replace("]", "");
        } else {
            return "Chưa cập nhật";
        }
    }

    public String displayWorkAddress(String[] workAddress) {
        return String.join("\n", workAddress);
    }

    public String getLevel(int levelId) {
        switch (levelId) {
            case 1: return "Nhân viên";
            case 2: return "Trưởng nhóm";
            case 3: return "Trưởng/Phó phòng";
            case 4: return "Quản lý / Giám sát";
            case 5: return "Trưởng chi nhánh";
            case 6: return "Phó giám đốc";
            case 7: return "Giám đốc";
            case 8: return "Thực tập sinh";
            default: return "Nhân viên";
        }
    }

    public String getGenderRequire(int genderId) {
        switch (genderId) {
            case 0: return "Nam";
            case 1: return "Nữ";
            default: return "Không yêu cầu";
        }
    }

    public String getWayToWork(int wtfId) {
        switch (wtfId) {
            case 1: return "Toàn thời gian";
            case 2: return "Bán thời gian";
            case 3: return "Thực tập";
            default: return "Remote - Làm việc từ xa";
        }
    }

    public String getAddress(String address) {
        String[] splinted = address.split(":");
        return splinted[0].replace("- ", "");
    }

    public String getExp(int exp) {
        switch (exp) {
            case 1: return "Mới ra trường";
            case 2: return "1-2 năm";
            case 3: return "3-4 năm";
            case 4: return "5 năm trở lên";
            default: return "Không yêu cầu";
        }
    }

    public String getSalary(int exp) {
        switch (exp) {
            case 1: return "Dưới 3 triệu";
            case 2: return "3-5 triệu";
            case 3: return "5-7 triệu";
            case 4: return "7-10 triệu";
            case 5: return "10-12 triệu";
            case 6: return "12-15 triệu";
            case 7: return "15-20 triệu";
            case 8: return "20-25 triệu";
            case 9: return "25-30 triệu";
            case 10: return "Trên 30 triệu";
            default: return "Thỏa thuận";
        }
    }

    public long getDeadline(Timestamp deadline) {
        java.time.LocalDate currentTime = Timestamp.now().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate futureTime = deadline.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(currentTime, futureTime);
    }

    // Getters and setters
    // ...

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCorpID() {
        return corpID;
    }

    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }

    public int getNumOfRecruit() {
        return numOfRecruit;
    }

    public void setNumOfRecruit(int numOfRecruit) {
        this.numOfRecruit = numOfRecruit;
    }

    public int getGenderJob() {
        return genderJob;
    }

    public void setGenderJob(int genderJob) {
        this.genderJob = genderJob;
    }

    public String[] getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String[] workAddress) {
        this.workAddress = workAddress;
    }

    public int getCareerId() {
        return careerId;
    }

    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

    public int getExpId() {
        return expId;
    }

    public void setExpId(int expId) {
        this.expId = expId;
    }

    public int getWayToWorkId() {
        return wayToWorkId;
    }

    public void setWayToWorkId(int wayToWorkId) {
        this.wayToWorkId = wayToWorkId;
    }

    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpertDay() {
        return expertDay;
    }

    public void setExpertDay(Timestamp expertDay) {
        this.expertDay = expertDay;
    }

    public String[] getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String[] jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String[] getRecruitRequire() {
        return recruitRequire;
    }

    public void setRecruitRequire(String[] recruitRequire) {
        this.recruitRequire = recruitRequire;
    }

    public String[] getBenefit() {
        return benefit;
    }

    public void setBenefit(String[] benefit) {
        this.benefit = benefit;
    }

    @Override
    public String toString() {
        return "Jobs{" +
                "jobID='" + jobID + '\'' +
                ", jobName='" + jobName + '\'' +
                ", corpID='" + corpID + '\'' +
                ", numOfRecruit=" + numOfRecruit +
                ", genderJob=" + genderJob +
                ", workAddress=" + Arrays.toString(workAddress) +
                ", careerId=" + careerId +
                ", expId=" + expId +
                ", wayToWorkId=" + wayToWorkId +
                ", salaryId=" + salaryId +
                ", levelId=" + levelId +
                ", state=" + state +
                ", createdAt=" + createdAt +
                ", expertDay=" + expertDay +
                ", jobDescription=" + Arrays.toString(jobDescription) +
                ", recruitRequire=" + Arrays.toString(recruitRequire) +
                ", benefit=" + Arrays.toString(benefit) +
                '}';
    }
}

