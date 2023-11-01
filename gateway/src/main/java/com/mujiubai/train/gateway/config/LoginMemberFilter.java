package com.mujiubai.train.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoginMemberFilter implements GlobalFilter, Ordered {

    private final static Logger LOG = LoggerFactory.getLogger(LoginMemberFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.contains("/admin")
                || path.contains("/hello")
                || path.contains("/member/member/login")
                || path.contains("/member/member/send-code")) {
            LOG.info("LoginMemberFilter无需拦截当前url：{}", path);
            return chain.filter(exchange);// 继续链式过滤
        }
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (token == null || token.isEmpty()) {
            LOG.info("token为空，拦截该请求");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        boolean valid = com.mujiubai.train.gateway.util.JwtUtil.validate(token);
        if (valid) {
            LOG.info("token合法，放行：{}", token);
            return chain.filter(exchange);// 继续链式过滤
        } else {
            LOG.info("token非法，拦截该请求：{}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // return exchange.getResponse().setComplete(); //终结过滤链
    }

    // 多个过滤类时，用于指定过滤顺序，前提是实现Ordered接口
    @Override
    public int getOrder() {
        return 0;
    }

}
