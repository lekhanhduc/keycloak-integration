package vn.khanhduc.integratekeycloak.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_KEY = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Map<String, Object> realAccessMap = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        if(realAccessMap == null || !realAccessMap.containsKey(ROLES_KEY)) {
            return Collections.emptyList();
        }

        Object roles = realAccessMap.get(ROLES_KEY);
        if(!(roles instanceof List<?>)) {
            return Collections.emptyList();
        }

        return ((List<?>) roles)
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toList());
    }

}