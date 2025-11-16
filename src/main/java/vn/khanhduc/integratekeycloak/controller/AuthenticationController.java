package vn.khanhduc.integratekeycloak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.integratekeycloak.dto.response.ApiResponse;
import vn.khanhduc.integratekeycloak.dto.response.SignInResponse;
import vn.khanhduc.integratekeycloak.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in-keycloak")
    ApiResponse<SignInResponse> signIn(@RequestParam String code) {
        return ApiResponse.<SignInResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authenticationService.signInKeyCloak(code))
                .build();
    }

}
