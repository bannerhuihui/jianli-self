package com.aitalentagent.api.web;

import com.aitalentagent.api.auth.AuthContext;
import com.aitalentagent.api.common.ApiResponse;
import com.aitalentagent.api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/session")
    public ResponseEntity<ApiResponse<AuthService.SessionResponse>> createSession(
            @Valid @RequestBody CreateSessionRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthService.SessionResponse response = authService.createSession(new AuthService.SessionRequest(
                request.authProvider(),
                request.inviteCode(),
                request.deviceId(),
                request.platform()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(response, requestId(httpRequest)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthService.SessionResponse>> refresh(
            @Valid @RequestBody RefreshRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthService.SessionResponse response = authService.refreshSession(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.of(response, requestId(httpRequest)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthService.UserView>> me(HttpServletRequest httpRequest) {
        AuthService.UserView user = authService.getCurrentUser(AuthContext.requireUser().userId());
        return ResponseEntity.ok(ApiResponse.of(user, requestId(httpRequest)));
    }

    private String requestId(HttpServletRequest request) {
        return request.getAttribute("requestId").toString();
    }

    public record CreateSessionRequest(
            @NotBlank String authProvider,
            String inviteCode,
            @NotBlank String deviceId,
            String platform
    ) {
    }

    public record RefreshRequest(@NotBlank String refreshToken) {
    }
}
