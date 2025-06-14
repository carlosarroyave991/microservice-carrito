package com.arka.microservice.carrito.domain.ports.out;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface ProductCarPersistencePort {
    Mono<ProductCarModel> save(ProductCarModel model);
    Flux<ProductCarModel> findAll();
    Mono<ProductCarModel> findById(Long id);
    Mono<Void> delete(Long id);
    Mono<ProductCarModel> update(ProductCarModel model);
}
