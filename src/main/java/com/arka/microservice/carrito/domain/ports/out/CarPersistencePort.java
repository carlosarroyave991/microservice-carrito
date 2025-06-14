package com.arka.microservice.carrito.domain.ports.out;

import com.arka.microservice.carrito.domain.models.CarModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface CarPersistencePort {
    Mono<CarModel> save(CarModel model);
    Mono<CarModel> update(CarModel model);
    Flux<CarModel> findAll();
    Mono<CarModel> findById(Long id);
    Mono<Void> delete(Long id);
}
