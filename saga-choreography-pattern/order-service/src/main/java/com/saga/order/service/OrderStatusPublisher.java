package com.saga.order.service;

import com.saga.commons.dto.OrderRequestDto;
import com.saga.commons.event.OrderEvent;
import com.saga.commons.event.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {

    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;


    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus){
        OrderEvent orderEvent=new OrderEvent(orderRequestDto,orderStatus);
        System.out.println("orderEvent: " + orderEvent);
        orderSinks.tryEmitNext(orderEvent);
    }
}
