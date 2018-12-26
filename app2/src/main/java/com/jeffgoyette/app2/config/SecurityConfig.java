package com.jeffgoyette.app2.config;

import com.jeffgoyette.app2.security.KeycloakOauth2UserService;
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
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Configures OAuth Login with Spring Security 5.
     * @return
     */
    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurer(
            @Value("${keycloak-client.registration-id}") final String registrationId
            , KeycloakOauth2UserService keycloakOidcUserService
    ) {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http    .authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                        .oauth2ResourceServer()
                        .jwt().jwtAuthenticationConverter(grantedAuthoritiesExtractor(registrationId));
//                        // Configure session management to your needs.
//                        // I need this as a basis for a classic, server side rendered application
//                        .sessionManagement()
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .and()
//                        // Depends on your taste. You can configure single paths here
//                        // or allow everything a I did and then use method based security
//                        // like in the controller below
//                        .authorizeRequests()
//                        .anyRequest().permitAll()
//                        .and()
//                        // This is the point where OAuth2 login of Spring 5 gets enabled
//                        .oauth2Login().userInfoEndpoint().oidcUserService(keycloakOidcUserService)
//                        .and()
//                        // I don't want a page with different clients as login options
//                        // So i use the constant from OAuth2AuthorizationRequestRedirectFilter
//                        // plus the configured realm as immediate redirect to Keycloak
//                        .loginPage(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + registrationId);
            }
        };
    }

    Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor(String clientId) {
        return new JwtAuthenticationConverter() {
            @Override
            protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

                @SuppressWarnings("unchecked")
                Map<String, Object> resourceMap = (Map<String, Object>) jwt.getClaims().get("resource_access");

                @SuppressWarnings("unchecked")
                Map<String, Map<String, Object>> clientResource = (Map<String, Map<String, Object>>) resourceMap.get(clientId);
                if (CollectionUtils.isEmpty(clientResource)) {
                    return Collections.emptyList();
                }

                @SuppressWarnings("unchecked")
                List<String> clientRoles = (List<String>) clientResource.get("roles");
                if (CollectionUtils.isEmpty(clientRoles)) {
                    return Collections.emptyList();
                }

                Collection<GrantedAuthority> authorities = AuthorityUtils
                        .createAuthorityList(clientRoles.toArray(new String[0]));

                SimpleAuthorityMapper authoritiesMapper = new SimpleAuthorityMapper();
                authoritiesMapper.setConvertToUpperCase(true);

                if (authoritiesMapper == null) {
                    return authorities;
                }

                return authoritiesMapper.mapAuthorities(authorities);
            }
        };
    }

//    @Bean
//    KeycloakOauth2UserService keycloakOidcUserService(OAuth2ClientProperties oauth2ClientProperties) {
//
//        // TODO use default JwtDecoder - where to grab?
//        NimbusJwtDecoderJwkSupport jwtDecoder = new NimbusJwtDecoderJwkSupport(
//                oauth2ClientProperties.getProvider().get("keycloak").getJwkSetUri());
//
//        SimpleAuthorityMapper authoritiesMapper = new SimpleAuthorityMapper();
//        authoritiesMapper.setConvertToUpperCase(true);
//
//        return new KeycloakOauth2UserService(jwtDecoder, authoritiesMapper);
//    }
//
//    @Bean
//    KeycloakLogoutHandler keycloakLogoutHandler() {
//        return new KeycloakLogoutHandler(new RestTemplate());
//    }

}
