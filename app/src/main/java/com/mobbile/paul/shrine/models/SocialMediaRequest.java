package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class SocialMediaRequest implements Serializable {

    private String brandInfo;

    private String socialMedia;

    private String achievement;

    private String acheivementGoal;

    public SocialMediaRequest() {
    }

    public String getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(String brandInfo) {
        this.brandInfo = brandInfo;
    }

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getAcheivementGoal() {
        return acheivementGoal;
    }

    public void setAcheivementGoal(String acheivementGoal) {
        this.acheivementGoal = acheivementGoal;
    }
}
