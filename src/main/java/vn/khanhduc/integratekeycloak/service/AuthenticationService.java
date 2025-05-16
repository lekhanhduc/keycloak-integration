package vn.khanhduc.integratekeycloak.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import vn.khanhduc.integratekeycloak.dto.identity.AuthorizationCodeExchange;
import vn.khanhduc.integratekeycloak.dto.request.CodeExchangeRequest;
import vn.khanhduc.integratekeycloak.dto.request.SignInRequest;
import vn.khanhduc.integratekeycloak.dto.response.SignInResponse;
import vn.khanhduc.integratekeycloak.exception.KeycloakAuthErrorHandler;
import vn.khanhduc.integratekeycloak.repository.IdentityProviderClient;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationService {

    @Value( "${identity-provider.client_id}")
    private String clientId;

    @Value( "${identity-provider.client_secret}")
    private String clientSecret;

    @Value( "${identity-provider.scope}")
    private String scope;

    @Value( "${identity-provider.redirect_uri}")
    private String redirectUri;

    private static final String grantType = "authorization_code";

    private final IdentityProviderClient providerClient;
    private final KeycloakAuthErrorHandler authErrorHandler;

    public SignInResponse signIn(SignInRequest request) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("grant_type", grantType);
        multiValueMap.add("client_id", clientId);
        multiValueMap.add("client_secret", clientSecret);
        multiValueMap.add("scope", scope);
        try {
            var tokenResponse = providerClient.exchangeToken(multiValueMap);

            return SignInResponse.builder()
                    .accessToken(tokenResponse.getAccessToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .build();
        }catch(FeignException exception) {
            throw authErrorHandler.handleKeycloakAuthException(exception);
        }
    }

    public SignInResponse signInKeyCloak(String code) {
        var exchangeToken = providerClient.exchangeToken(AuthorizationCodeExchange.builder()
                        .client_id(clientId)
                        .client_secret(clientSecret)
                        .code(code)
                        .grant_type(grantType)
                        .redirect_uri(redirectUri)
                        .scope(scope)
                .build());

        log.info("Token {}", exchangeToken.getAccessToken());

        return SignInResponse.builder()
                .accessToken(exchangeToken.getAccessToken())
                .refreshToken(exchangeToken.getRefreshToken())
                .build();
    }
}
