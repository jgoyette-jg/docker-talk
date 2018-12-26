package com.jeffgoyette.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.io.IOException;
import java.security.InvalidKeyException;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                @Value("${keycloak-client.registration-id}") final String registrationId,
            @Value("${spring.security.oauth2.client.registration.my-keycloak.client-id}") final String clientId)
            throws Exception {
        // @formatter:off
        http
                .authorizeExchange().pathMatchers("/test**").authenticated()
                .and()
                    .oauth2Login()
                .and().formLogin().loginPage("/oauth2/authorization/"+registrationId)
                .and()
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        // @formatter:on
        return http.build();
    }

    @Bean
    OidcReactiveOAuth2UserService userService(ReactiveJwtDecoder jwtDecoder){
        return new JwtReactiveOidc2UserService(jwtDecoder);
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder(OAuth2ClientProperties oauth2ClientProperties) throws IOException, InvalidKeyException {
        return new NimbusReactiveJwtDecoder(oauth2ClientProperties.getProvider().get("keycloak").getJwkSetUri());
    }

}
