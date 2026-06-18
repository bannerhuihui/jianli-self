package com.aitalentagent.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Agents agents = new Agents();
    private final Auth auth = new Auth();
    private final Invite invite = new Invite();
    private final Cors cors = new Cors();
    private final Llm llm = new Llm();
    private final Storage storage = new Storage();

    public Agents getAgents() {
        return agents;
    }

    public Auth getAuth() {
        return auth;
    }

    public Invite getInvite() {
        return invite;
    }

    public Cors getCors() {
        return cors;
    }

    public Llm getLlm() {
        return llm;
    }

    public Storage getStorage() {
        return storage;
    }

    public static class Storage {
        private String type = "local";
        private final ProxyOss proxyOss = new ProxyOss();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ProxyOss getProxyOss() {
            return proxyOss;
        }
    }

    public static class ProxyOss {
        private String uploadUrl = "";
        private String prefix = "jianli/resume";

        public String getUploadUrl() {
            return uploadUrl;
        }

        public void setUploadUrl(String uploadUrl) {
            this.uploadUrl = uploadUrl;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    public static class Agents {
        private String mode = "mock";
        private long mockDelayMs = 3000;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public long getMockDelayMs() {
            return mockDelayMs;
        }

        public void setMockDelayMs(long mockDelayMs) {
            this.mockDelayMs = mockDelayMs;
        }
    }

    public static class Auth {
        private String jwtSecret;
        private long accessTokenTtlSeconds = 604800;
        private long refreshTokenTtlSeconds = 2592000;

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public long getAccessTokenTtlSeconds() {
            return accessTokenTtlSeconds;
        }

        public void setAccessTokenTtlSeconds(long accessTokenTtlSeconds) {
            this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        }

        public long getRefreshTokenTtlSeconds() {
            return refreshTokenTtlSeconds;
        }

        public void setRefreshTokenTtlSeconds(long refreshTokenTtlSeconds) {
            this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
        }
    }

    public static class Invite {
        private boolean enabled;
        private String code = "beta-2026";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class Cors {
        private List<String> allowedOrigins = List.of();

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class Llm {
        private final Volcengine volcengine = new Volcengine();

        public Volcengine getVolcengine() {
            return volcengine;
        }
    }

    public static class Volcengine {
        private String apiKey = "";
        private String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";
        private final Endpoints endpoints = new Endpoints();
        private final AgentOptions agentOptions = new AgentOptions();

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Endpoints getEndpoints() {
            return endpoints;
        }

        public AgentOptions getAgentOptions() {
            return agentOptions;
        }
    }

    public static class Endpoints {
        private String resume = "";
        private String interview = "";
        private String profile = "";
        private String resumeBuilder = "";

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public String getInterview() {
            return interview;
        }

        public void setInterview(String interview) {
            this.interview = interview;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getResumeBuilder() {
            return resumeBuilder;
        }

        public void setResumeBuilder(String resumeBuilder) {
            this.resumeBuilder = resumeBuilder;
        }
    }

    public static class AgentOptions {
        private double resumeTemperature = 0.2;
        private double interviewTemperature = 0.5;
        private double profileTemperature = 0.3;
        private double resumeBuilderTemperature = 0.5;

        public double getResumeTemperature() {
            return resumeTemperature;
        }

        public void setResumeTemperature(double resumeTemperature) {
            this.resumeTemperature = resumeTemperature;
        }

        public double getInterviewTemperature() {
            return interviewTemperature;
        }

        public void setInterviewTemperature(double interviewTemperature) {
            this.interviewTemperature = interviewTemperature;
        }

        public double getProfileTemperature() {
            return profileTemperature;
        }

        public void setProfileTemperature(double profileTemperature) {
            this.profileTemperature = profileTemperature;
        }

        public double getResumeBuilderTemperature() {
            return resumeBuilderTemperature;
        }

        public void setResumeBuilderTemperature(double resumeBuilderTemperature) {
            this.resumeBuilderTemperature = resumeBuilderTemperature;
        }
    }
}
