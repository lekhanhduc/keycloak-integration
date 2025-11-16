package vn.khanhduc.integratekeycloak.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j(topic = "HANDLER-FEIGN-CLIENT")
public class HandlerErrorFeignClient {

    private final ObjectMapper objectMapper;
    private final Map<String, ErrorCode> errorMap;

    public HandlerErrorFeignClient() {
        this.objectMapper = new ObjectMapper();
        this.errorMap = new HashMap<>();
        errorMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        errorMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
    }

    public AppException handleKeycloakException(FeignException exception) {
        log.error("FeignClient Exception", exception);
        try {
            log.warn(exception.contentUTF8());
            KeyCloakError keyCloakError = objectMapper.readValue(exception.contentUTF8(), KeyCloakError.class);
            if(Objects.nonNull(keyCloakError.getErrorMessage()) && errorMap.containsKey(keyCloakError.getErrorMessage())) {
                return new AppException(errorMap.get(keyCloakError.getErrorMessage()));
            }
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize body to KeyCloakError", e);
        }
        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
