package com.arka.microservice.carrito.domain.ports.in;

import com.arka.microservice.carrito.domain.models.OrderModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface IOrderPortUseCase {
    Mono<OrderModel> createOrder(OrderModel model);
    Mono<OrderModel> updateOrder(OrderModel model, Long id);
    Flux<OrderModel> getAllOders();
    Mono<OrderModel> getOrderById(Long id);
    //Mono<Void> sendOrderStatusNotification(OrderModel order);
}
