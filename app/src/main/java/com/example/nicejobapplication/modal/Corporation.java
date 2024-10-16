package com.example.nicejobapplication.modal;

import org.jetbrains.annotations.NotNull;

public final class Corporation {

    @NotNull
    private String corpID;
    @NotNull
    private String corpName;
    @NotNull
    private String corpDescription;
    @NotNull
    private String corpLogo;

    // Constructor with all parameters
    public Corporation(@NotNull String corpID, @NotNull String corpName, @NotNull String corpDescription, @NotNull String corpLogo) {
        this.corpID = corpID;
        this.corpName = corpName;
        this.corpDescription = corpDescription;
        this.corpLogo = corpLogo;
    }

    // Getters
    @NotNull
    public String getCorpID() {
        return corpID;
    }

    @NotNull
    public String getCorpName() {
        return corpName;
    }

    @NotNull
    public String getCorpDescription() {
        return corpDescription;
    }

    @NotNull
    public String getCorpLogo() {
        return corpLogo;
    }

    // Setters
    public void setCorpID(@NotNull String corpID) {
        this.corpID = corpID;
    }

    public void setCorpName(@NotNull String corpName) {
        this.corpName = corpName;
    }

    public void setCorpDescription(@NotNull String corpDescription) {
        this.corpDescription = corpDescription;
    }

    public void setCorpLogo(@NotNull String corpLogo) {
        this.corpLogo = corpLogo;
    }
}
