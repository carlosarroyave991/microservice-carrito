package com.arka.microservice.carrito.domain.ports.in;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface IProductCarPortUseCase {
    Mono<ProductCarModel> createProductCar(ProductCarModel model);
}
