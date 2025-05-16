package vn.khanhduc.integratekeycloak.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
}
