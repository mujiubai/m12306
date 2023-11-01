// package com.mujiubai.train.gateway.config;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;

// import reactor.core.publisher.Mono;

// @Component
// public class Test1Filter implements GlobalFilter,Ordered {

//     private final static Logger LOG = LoggerFactory.getLogger(Test1Filter.class);

//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         LOG.info("test filter 1");
//         return chain.filter(exchange);//继续链式过滤
//         // return exchange.getResponse().setComplete(); //终结过滤链
//     }

//     //多个过滤类时，用于指定过滤顺序，前提是实现Ordered接口
//     @Override
//     public int getOrder() {
//         return 0;
//     }
    
// }
