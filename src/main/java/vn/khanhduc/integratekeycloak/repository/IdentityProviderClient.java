package vn.khanhduc.integratekeycloak.repository;

import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vn.khanhduc.integratekeycloak.dto.identity.AuthorizationCodeExchange;
import vn.khanhduc.integratekeycloak.dto.identity.TokenExchangeResponse;
import vn.khanhduc.integratekeycloak.dto.identity.UserCreationParam;

@FeignClient(name = "identity-provider", url = "${identity-provider.url}")
public interface IdentityProviderClient {

    @PostMapping(value = "/realms/identity-service/protocol/openid-connect/token",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenExchangeResponse exchangeToken(@RequestBody MultiValueMap<String, String> params);

    @PostMapping(value = "/admin/realms/identity-service/users",
                consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createUser(@RequestHeader("Authorization") String authorization,  @RequestBody UserCreationParam param);


    @PostMapping(value = "/realms/identity-service/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap AuthorizationCodeExchange params);

}
