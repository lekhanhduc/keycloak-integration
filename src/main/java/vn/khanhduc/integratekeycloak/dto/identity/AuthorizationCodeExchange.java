package vn.khanhduc.integratekeycloak.dto.identity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationCodeExchange {
    private String scope;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
}
