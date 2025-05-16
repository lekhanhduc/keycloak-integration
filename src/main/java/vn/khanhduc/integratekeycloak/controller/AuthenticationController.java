package vn.khanhduc.integratekeycloak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.integratekeycloak.dto.request.SignInRequest;
import vn.khanhduc.integratekeycloak.dto.response.ApiResponse;
import vn.khanhduc.integratekeycloak.dto.response.SignInResponse;
import vn.khanhduc.integratekeycloak.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request) {
        return ApiResponse.<SignInResponse>builder()
                .result(authenticationService.signIn(request))
                .build();
    }

    @PostMapping("/sign-in-keycloak")
    ApiResponse<SignInResponse> signIn(@RequestParam String code) {
        return ApiResponse.<SignInResponse>builder()
                .result(authenticationService.signInKeyCloak(code))
                .build();
    }

}
