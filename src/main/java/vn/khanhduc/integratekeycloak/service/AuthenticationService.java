package vn.khanhduc.integratekeycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.khanhduc.integratekeycloak.dto.identity.AuthorizationCodeExchange;
import vn.khanhduc.integratekeycloak.dto.response.SignInResponse;
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

    public SignInResponse signInKeyCloak(String code) {
        var exchangeToken = providerClient.exchangeToken(
                AuthorizationCodeExchange.builder()
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
