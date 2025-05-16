package vn.khanhduc.integratekeycloak.dto.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String username;
    private String password;
}
