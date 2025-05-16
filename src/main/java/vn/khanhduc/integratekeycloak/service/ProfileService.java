package vn.khanhduc.integratekeycloak.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import vn.khanhduc.integratekeycloak.dto.identity.Credential;
import vn.khanhduc.integratekeycloak.dto.identity.TokenExchangeParam;
import vn.khanhduc.integratekeycloak.dto.identity.UserCreationParam;
import vn.khanhduc.integratekeycloak.dto.request.RegistrationRequest;
import vn.khanhduc.integratekeycloak.dto.response.ProfileResponse;
import vn.khanhduc.integratekeycloak.entity.Profile;
import vn.khanhduc.integratekeycloak.exception.AppException;
import vn.khanhduc.integratekeycloak.exception.ErrorCode;
import vn.khanhduc.integratekeycloak.exception.HandlerErrorFeignClient;
import vn.khanhduc.integratekeycloak.repository.IdentityProviderClient;
import vn.khanhduc.integratekeycloak.repository.ProfileRepository;
import java.util.List;
import java.util.Objects;
import static vn.khanhduc.integratekeycloak.constant.IdentityConstant.LOCATION;
import static vn.khanhduc.integratekeycloak.constant.IdentityConstant.PASSWORD;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    @Value( "${identity-provider.grant_type}")
    private String grantType;

    @Value( "${identity-provider.client_id}")
    private String clientId;

    @Value( "${identity-provider.client_secret}")
    private String clientSecret;

    @Value( "${identity-provider.scope}")
    private String scope;

    private final ProfileRepository profileRepository;
    private final IdentityProviderClient providerClient;
    private final HandlerErrorFeignClient errorFeignClient;

    public ProfileResponse register(RegistrationRequest request) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", scope);

        try {
            var exchangeToken = providerClient.exchangeToken(params);

            var creationResponse = providerClient.createUser(
                    "Bearer " + exchangeToken.getAccessToken(),
                    UserCreationParam.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .enabled(true)
                            .emailVerified(true)
                            .credentials(List.of(Credential.builder()
                                    .type(PASSWORD)
                                    .value(request.getPassword())
                                    .temporary(false)
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);

            Profile profile = Profile.builder()
                    .userId(userId)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dob(request.getDob())
                    .build();

            profileRepository.save(profile);
            log.info("Profile saved {}", profile.getProfileId());

            return ProfileResponse.builder()
                    .profileId(profile.getProfileId())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dob(request.getDob())
                    .build();
        }catch (FeignException exception) {
            throw errorFeignClient.handleKeycloakException(exception);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ProfileResponse> findAll() {
        return profileRepository.findAll().stream()
                .map(profile -> ProfileResponse.builder()
                        .profileId(profile.getProfileId())
                        .username(profile.getUsername())
                        .email(profile.getEmail())
                        .firstName(profile.getFirstName())
                        .lastName(profile.getLastName())
                        .dob(profile.getDob())
                        .build())
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public ProfileResponse info() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userId == null) throw new AppException(ErrorCode.UNAUTHENTICATED);

        return profileRepository.findByUserId(userId)
                .map(profile -> ProfileResponse.builder()
                        .profileId(profile.getProfileId())
                        .firstName(profile.getFirstName())
                        .lastName(profile.getLastName())
                        .username(profile.getUsername())
                        .email(profile.getEmail())
                        .dob(profile.getDob())
                        .build())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = Objects.requireNonNull(response.getHeaders().get(LOCATION)).getFirst();
        String[] split = location.split("/");
        return split.length > 0 ? split[split.length - 1] : "";
    }

}
