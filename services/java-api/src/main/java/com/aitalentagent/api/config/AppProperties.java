package com.aitalentagent.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Agents agents = new Agents();
    private final Auth auth = new Auth();
    private final Invite invite = new Invite();
    private final Cors cors = new Cors();

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
}
