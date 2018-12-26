package com.jeffgoyette.app1.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @LoadBalanced
    @Bean
    @Profile({"!noServiceDiscovery"})
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile({"noServiceDiscovery"})
    public RestTemplate regularRestTemplate() {
        return new RestTemplate();
    }
}