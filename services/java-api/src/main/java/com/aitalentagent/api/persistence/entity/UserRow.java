package com.aitalentagent.api.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserRow {

    @Id
    private String id;
    private String role;
    @Column(name = "auth_provider")
    private String authProvider;
    @Column(name = "device_id")
    private String deviceId;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> entitlements = new ArrayList<>();
    @Column(name = "wechat_mp_open_id")
    private String wechatMpOpenId;
    @Column(name = "wechat_oa_open_id")
    private String wechatOaOpenId;
    @Column(name = "union_id")
    private String unionId;
    @Column(name = "active_journey_id")
    private String activeJourneyId;
    @Column(name = "created_at")
    private Instant createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public List<String> getEntitlements() { return entitlements; }
    public void setEntitlements(List<String> entitlements) { this.entitlements = entitlements; }
    public String getWechatMpOpenId() { return wechatMpOpenId; }
    public void setWechatMpOpenId(String wechatMpOpenId) { this.wechatMpOpenId = wechatMpOpenId; }
    public String getWechatOaOpenId() { return wechatOaOpenId; }
    public void setWechatOaOpenId(String wechatOaOpenId) { this.wechatOaOpenId = wechatOaOpenId; }
    public String getUnionId() { return unionId; }
    public void setUnionId(String unionId) { this.unionId = unionId; }
    public String getActiveJourneyId() { return activeJourneyId; }
    public void setActiveJourneyId(String activeJourneyId) { this.activeJourneyId = activeJourneyId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
