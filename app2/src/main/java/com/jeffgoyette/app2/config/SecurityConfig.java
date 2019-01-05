package com.jeffgoyette.app2.config;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {

    /**
     * Configures OAuth Login with Spring Security 5.
     * @return
     */
    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurer(
            @Value("${keycloak-client.registration-id}") final String registrationId
    ) {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http    .authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                        .oauth2ResourceServer()
                        .jwt().jwtAuthenticationConverter(grantedAuthoritiesExtractor(registrationId));
            }
        };
    }

    Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor(String clientId) {
        return new JwtAuthenticationConverter() {
            @Override
            protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
                log.info(jwt.getClaims().toString());
                JSONObject stringAuthorities = (JSONObject) jwt.getClaims().get("realm_access");
                JSONArray roles = (JSONArray) stringAuthorities.get("roles");
                if (stringAuthorities != null) {
                    return roles.stream().map(value -> new SimpleGrantedAuthority(value.toString())).collect(Collectors.toList());
                } else {
                    return null;
                }
            }
        };
    }

}
