package world.grendel.cringeit.config;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import world.grendel.cringeit.aspects.AuthenticatedRouteHandler;

/**
 * AuthenticatedRouteConfiguration
 */
@Configuration
public class AuthenticatedRouteConfiguration {
    @Bean
    public AuthenticatedRouteHandler getAutowireCapableAuthenticatedRouteHandler() {
        return Aspects.aspectOf(AuthenticatedRouteHandler.class);
    }
}
