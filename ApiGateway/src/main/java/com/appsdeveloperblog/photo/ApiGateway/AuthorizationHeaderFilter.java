package com.appsdeveloperblog.photo.ApiGateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Custom filter class that can be assigned to a specific gateway route.
 * It will be executed before Spring-Cloud API-Gateway rout the request to the destination microservice.
 * Check the following…
 *
 * This is assigned in the application.properties per route
 * spring.cloud.gateway.routes[0].filters[1]=AuthorizationHeaderFilter
 */
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Logger log = LoggerFactory.getLogger(AuthorizationHeaderFilter.class);

    @Value("${token.secret}")
    private String signingKey;

    // [113] 06:55 : Tells superclass, which Config.class to use when apply() method is called.
    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config{
        // Configuration properties here
    }

    /**
     * 112.  This is the method that gets triggered with this class gets triggered by the Api-Gateway Route
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {

        /* exchange, chain is from GatewayFilter interface
         * exchange - allow us to read Http.request details.
         * chain - let use invoke the next filter
         */
        return (exchange, chain) -> {
            log.info("AuthorizationHeaderFilter: triggered");
            // 1. Get Request Object : allows you to read HEADER, Attributes, Body...etc.
            ServerHttpRequest request = exchange.getRequest();

            // 2. check must contain AUTHORIZATION : if none -> onError()
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No authorization in header");
            }

            // 3. Read Header -> extract token
            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authHeader.replace("Bearer", "");

            // 4. [113] Validating JWT Access Token
            if(!isJwtValid(jwt)){
                return onError(exchange, "Jwt token is not valid");
            }

            // Provide a way to delegate to the next filter: execute the next filter in chain
            return chain.filter(exchange);
        };
    }

    /**
     * [112] 04:25
     * @param exchange - Type ServerWebExchange object, use to read response
     * @param msg - Static message
     * @return - Response object set to complete so it returnes the error message and do not invoke other filters.
     */
    private Mono<Void> onError(ServerWebExchange exchange, String msg) {

        log.info("Error was triggered");

        // 1. Response Object
        ServerHttpResponse res = exchange.getResponse();

        // 2. Set httpStatus: UNAUTHORIZED
        res.setStatusCode(HttpStatus.UNAUTHORIZED);

        // 3. COMPLETE: stop here, so it does not invoke other filter in the chain.
        return res.setComplete();
    }

    /**
     * [113] Validating JWT Access token
     * https://www.notion.so/Customer-Filter-Implementation-ae47ef281e2f41028cb7fc85b606fe15#500e3bafd4bc4216841c9d6bea04ec32
     *
     * Notes: How to work on each cliam separately?  For example parse subject & expiration
     * https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/learn/lecture/13498598#questions/18665464
     *
     * Claims claims = Jwts.parser()
     *  .setSigningKey(tokenSecret)
     *  .parseClaimsJws(jwt).getBody();
     *
     * // Reading Reserved Claims
     * System.out.println("Subject: " + claims.getSubject());
     * System.out.println("Expiration: " + claims.getExpiration());
     *
     * @param jwt
     * @return
     */
    private boolean isJwtValid(String jwt){
        boolean returnValue = true; // default value
        String subject = null;

        log.info("Token.Secrete: {}", signingKey);

        try {
            // Original from class
//            subject = Jwts.parser()
//                    .setSigningKey(signingKey)
//                    .parseClaimsJws(jwt)
//                    .getBody()
//                    .getSubject();


            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(jwt)
                    .getBody();

            subject = claims.getSubject();

        } catch (Exception ex){
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()){
            returnValue = false;
        }

        return returnValue;
    }
}
