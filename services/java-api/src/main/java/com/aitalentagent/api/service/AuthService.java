package com.aitalentagent.api.service;

import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.common.Ids;
import com.aitalentagent.api.config.AppProperties;
import com.aitalentagent.api.domain.UserAccount;
import com.aitalentagent.api.auth.TokenService;
import com.aitalentagent.api.repository.AppStore;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final AppStore store;
    private final TokenService tokenService;
    private final AppProperties appProperties;

    public AuthService(AppStore store, TokenService tokenService, AppProperties appProperties) {
        this.store = store;
        this.tokenService = tokenService;
        this.appProperties = appProperties;
    }

    public SessionResponse createSession(SessionRequest request) {
        validateInviteCode(request.inviteCode());
        if (!"anonymous".equals(request.authProvider())) {
            throw new ApiException("BAD_REQUEST", "当前仅支持 anonymous 登录", HttpStatus.BAD_REQUEST);
        }
        if (request.deviceId() == null || request.deviceId().isBlank()) {
            throw new ApiException("BAD_REQUEST", "deviceId 不能为空", HttpStatus.BAD_REQUEST);
        }

        UserAccount user = store.findUserByDeviceId(request.deviceId()).orElseGet(() -> {
            UserAccount created = new UserAccount();
            created.setId(Ids.next("user"));
            created.setAuthProvider("anonymous");
            created.setDeviceId(request.deviceId());
            created.setEntitlements(List.of("candidate.full_flow"));
            store.saveUser(created);
            return created;
        });

        String sessionId = Ids.next("session");
        String accessToken = tokenService.createAccessToken(user, sessionId);
        String refreshToken = tokenService.createRefreshToken(user.getId(), sessionId);
        store.saveRefreshToken(refreshToken, user.getId());

        return new SessionResponse(
                accessToken,
                refreshToken,
                appProperties.getAuth().getAccessTokenTtlSeconds(),
                toUserView(user)
        );
    }

    public SessionResponse refreshSession(String refreshToken) {
        TokenService.RefreshClaims claims = tokenService.parseRefreshToken(refreshToken);
        UserAccount user = store.findUserById(claims.userId())
                .orElseThrow(() -> new ApiException("AUTH_TOKEN_INVALID", "用户不存在", HttpStatus.UNAUTHORIZED));

        String accessToken = tokenService.createAccessToken(user, claims.sessionId());
        return new SessionResponse(
                accessToken,
                refreshToken,
                appProperties.getAuth().getAccessTokenTtlSeconds(),
                toUserView(user)
        );
    }

    public UserView getCurrentUser(String userId) {
        UserAccount user = store.findUserById(userId)
                .orElseThrow(() -> new ApiException("AUTH_TOKEN_INVALID", "用户不存在", HttpStatus.UNAUTHORIZED));
        return toUserView(user);
    }

    private void validateInviteCode(String inviteCode) {
        if (!appProperties.getInvite().isEnabled()) {
            return;
        }
        if (!appProperties.getInvite().getCode().equals(inviteCode)) {
            throw new ApiException("AUTH_ACCESS_DENIED", "邀请码无效", HttpStatus.FORBIDDEN);
        }
    }

    private UserView toUserView(UserAccount user) {
        return new UserView(
                user.getId(),
                user.getRole(),
                user.getAuthProvider(),
                user.getEntitlements(),
                user.getActiveJourneyId()
        );
    }

    public record SessionRequest(String authProvider, String inviteCode, String deviceId, String platform) {
    }

    public record SessionResponse(
            String accessToken,
            String refreshToken,
            long expiresIn,
            UserView user
    ) {
    }

    public record UserView(
            String id,
            String role,
            String authProvider,
            List<String> entitlements,
            String activeJourneyId
    ) {
    }
}
