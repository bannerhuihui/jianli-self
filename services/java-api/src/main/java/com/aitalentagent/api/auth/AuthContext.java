package com.aitalentagent.api.auth;

import com.aitalentagent.api.common.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthContext {

    private AuthContext() {
    }

    public static UserPrincipal requireUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new ApiException("AUTH_TOKEN_INVALID", "未登录或 Token 无效", HttpStatus.UNAUTHORIZED);
        }
        return principal;
    }
}
