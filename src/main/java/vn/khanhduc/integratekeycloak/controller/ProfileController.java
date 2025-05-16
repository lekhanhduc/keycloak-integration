package vn.khanhduc.integratekeycloak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.integratekeycloak.dto.request.RegistrationRequest;
import vn.khanhduc.integratekeycloak.dto.response.ApiResponse;
import vn.khanhduc.integratekeycloak.dto.response.ProfileResponse;
import vn.khanhduc.integratekeycloak.service.ProfileService;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    ApiResponse<ProfileResponse> register(@RequestBody RegistrationRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.register(request))
                .build();
    }

    @GetMapping("/profiles")
    ApiResponse<List<ProfileResponse>> profiles() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.findAll())
                .build();
    }

    @GetMapping("/info")
    ApiResponse<ProfileResponse> profile() {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.info())
                .build();
    }


}
