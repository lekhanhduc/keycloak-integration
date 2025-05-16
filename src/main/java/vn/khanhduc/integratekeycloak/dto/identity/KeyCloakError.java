package vn.khanhduc.integratekeycloak.dto.identity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyCloakError {
    private String errorMessage;
}
