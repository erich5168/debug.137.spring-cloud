package com.appsdeveloperblog.photo.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilterConfiguration {

    final Logger log = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter(){

        return ((exchange, chain) -> {
            log.info("2nd pre-filter is....invoked...");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("2st post-filter is....invoked....");
            }));
        });
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter(){

        return ((exchange, chain) -> {
            log.info("3rd pre-filter is....invoked...");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("3rd post-filter is....invoked....");
            }));
        });
    }
}
