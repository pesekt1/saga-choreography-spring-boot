package com.saga.order.controller;

import com.saga.commons.dto.OrderRequestDto;
import com.saga.order.entity.PurchaseOrder;
import com.saga.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/create")
    public Mono<PurchaseOrder> createOrder(@RequestBody OrderRequestDto orderRequestDto){
        return Mono.fromCallable(() -> orderService.createOrder(orderRequestDto))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Flux<PurchaseOrder> getOrders(){
        return Mono.fromCallable(() -> orderService.getAllOrders())
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }

    // lightweight health/ping endpoint that doesn't use JPA
    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("ok");
    }
}
