package com.aitalentagent.api.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserAccount {

    private String id;
    private String role = "CANDIDATE";
    private String authProvider = "anonymous";
    private String deviceId;
    private List<String> entitlements = new ArrayList<>(List.of("candidate.full_flow"));
    private String wechatMpOpenId;
    private String wechatOaOpenId;
    private String unionId;
    private String activeJourneyId;
    private Instant createdAt = Instant.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<String> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(List<String> entitlements) {
        this.entitlements = entitlements;
    }

    public String getWechatMpOpenId() {
        return wechatMpOpenId;
    }

    public void setWechatMpOpenId(String wechatMpOpenId) {
        this.wechatMpOpenId = wechatMpOpenId;
    }

    public String getWechatOaOpenId() {
        return wechatOaOpenId;
    }

    public void setWechatOaOpenId(String wechatOaOpenId) {
        this.wechatOaOpenId = wechatOaOpenId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getActiveJourneyId() {
        return activeJourneyId;
    }

    public void setActiveJourneyId(String activeJourneyId) {
        this.activeJourneyId = activeJourneyId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
