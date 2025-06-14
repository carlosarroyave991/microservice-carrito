package com.arka.microservice.carrito.domain.ports.in;

import com.arka.microservice.carrito.domain.models.CarModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface ICarPortUseCase {
    Mono<CarModel> createCar(CarModel model);
    Mono<CarModel> updateCar(CarModel model, Long id);
    Flux<CarModel> getAllOders();
    Mono<CarModel> getCarById(Long id);
    Mono<Void> deleteCarById(Long id);
}
