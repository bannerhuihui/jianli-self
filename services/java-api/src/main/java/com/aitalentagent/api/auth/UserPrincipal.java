package com.aitalentagent.api.auth;

import java.util.List;

public record UserPrincipal(
        String userId,
        String sessionId,
        String role,
        String authProvider,
        List<String> entitlements
) {
}
