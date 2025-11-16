package vn.khanhduc.integratekeycloak.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyCloakError {
    private String errorMessage;
}
