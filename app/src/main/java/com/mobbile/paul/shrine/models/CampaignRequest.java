package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class CampaignRequest implements Serializable {

    private String background;

    private String objectives;

    private String consumerInsight;

    private String features;

    private String competitors;

    private String benefit;

    private String budget;

    private String timings;

    private String deliverable;

    public CampaignRequest() {
    }


    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getConsumerInsight() {
        return consumerInsight;
    }

    public void setConsumerInsight(String consumerInsight) {
        this.consumerInsight = consumerInsight;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getCompetitors() {
        return competitors;
    }

    public void setCompetitors(String competitors) {
        this.competitors = competitors;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getDeliverable() {
        return deliverable;
    }

    public void setDeliverable(String deliverable) {
        this.deliverable = deliverable;
    }
}
