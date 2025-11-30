package com.saga.order.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String method = exchange.getRequest().getMethodValue();
        String path = exchange.getRequest().getURI().getPath();
        log.info("REQ START: {} {} from {}", method, path, exchange.getRequest().getRemoteAddress());

        return chain.filter(exchange)
                .doOnSuccessOrError((aVoid, ex) -> {
                    if (ex != null) {
                        log.error("REQ ERROR: {} {} -> {}", method, path, ex.toString());
                    } else {
                        Integer status = exchange.getResponse().getStatusCode() != null
                                ? exchange.getResponse().getStatusCode().value()
                                : null;
                        log.info("REQ COMPLETE: {} {} -> status={}", method, path, status);
                    }
                });
    }
}

