package com.appsdeveloperblog.photo.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Section 16: Global Filters
 * A pre-filter will execute every HTTP.request before route takes place.  For Spring framework
 * to notice this class and to place it in applicationContext, you must annotate with `@Component`.
 *
 * 116. Created
 */
@Component
public class MyPreFilter implements GlobalFilter, Ordered {

    private final Logger log = LoggerFactory.getLogger(MyPreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("My 1st pre-filter is invoked....");

        // 117. Accessing Request Path & HTTP Headers
        // 1. get RequestPath
        String requestPath = exchange.getRequest().getPath().toString();
        log.info("Requet path: {}", requestPath);

        // 2,. get Header
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headersNames = headers.keySet();
        log.info("Get Headers Set: {}", headersNames);

        headersNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            log.info(headerName + " = " + headerValue);
        });

        return chain.filter(exchange); // Pass `exchange` object to the next filter.
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
