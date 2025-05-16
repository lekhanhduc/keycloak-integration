package vn.khanhduc.integratekeycloak.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "KEYCLOAK-AUTH-ERROR-HANDLER")
public class KeycloakAuthErrorHandler {

    public AppException handleKeycloakAuthException(FeignException exception) {
        log.error("KeycloakAuthErrorHandler Exception", exception);
        if(exception.status() == 401) {
            return new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

}
