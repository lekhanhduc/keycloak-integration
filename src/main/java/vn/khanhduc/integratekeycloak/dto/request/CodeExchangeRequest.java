package vn.khanhduc.integratekeycloak.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeExchangeRequest {
    private String code;
}
