package com.arka.microservice.carrito.domain.ports.out;

import com.arka.microservice.carrito.domain.models.OrderModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface OrderPersistencePort {
    Mono<OrderModel> save(OrderModel model);
    Mono<OrderModel> update(OrderModel model);
    Flux<OrderModel> findAll();
    Mono<OrderModel> findById(Long id);
}
