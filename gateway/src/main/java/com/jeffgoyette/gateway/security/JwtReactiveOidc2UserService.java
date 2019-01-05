package com.jeffgoyette.gateway.security;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtReactiveOidc2UserService extends OidcReactiveOAuth2UserService {

    private final ReactiveJwtDecoder jwtDecoder;

    public JwtReactiveOidc2UserService(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<OidcUser> loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        Mono<OidcUser> user = super.loadUser(userRequest);
        log.info(userRequest.getAccessToken().toString());
        return jwtDecoder.decode(userRequest.getAccessToken()
                .getTokenValue())
                .flatMap(jwt -> user.map(keycloakUser -> new DefaultOidcUser(toGrantedAuthorities(jwt.getClaims()), userRequest.getIdToken(), keycloakUser.getUserInfo(), "preferred_username")));
    }


    private Collection<? extends GrantedAuthority> toGrantedAuthorities(Map<String, Object> claims) {
        log.info(claims.toString());
        JSONObject stringAuthorities = (JSONObject) claims.get("realm_access");
        JSONArray roles = (JSONArray) stringAuthorities.get("roles");
        if (stringAuthorities != null) {
            return roles.stream().map(value -> new SimpleGrantedAuthority(value.toString())).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
