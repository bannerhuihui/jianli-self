package com.aitalentagent.api.domain;

import java.util.ArrayList;
import java.util.List;

public class InterviewSessionEntity {

    private String id;
    private String journeyId;
    private String stage = "experience_exploration";
    private String status = "active";
    private List<InterviewTurnEntity> turns = new ArrayList<>();
    private List<String> missingEvidence = new ArrayList<>();
    private int questionIndex;
    private boolean canGenerateProfile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<InterviewTurnEntity> getTurns() {
        return turns;
    }

    public void setTurns(List<InterviewTurnEntity> turns) {
        this.turns = turns;
    }

    public List<String> getMissingEvidence() {
        return missingEvidence;
    }

    public void setMissingEvidence(List<String> missingEvidence) {
        this.missingEvidence = missingEvidence;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public boolean isCanGenerateProfile() {
        return canGenerateProfile;
    }

    public void setCanGenerateProfile(boolean canGenerateProfile) {
        this.canGenerateProfile = canGenerateProfile;
    }
}
