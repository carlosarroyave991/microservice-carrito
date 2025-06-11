package com.arka.microservice.carrito.domain.ports.out;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface ProductCarPersistencePort {
    Mono<ProductCarModel> save(ProductCarModel model);
}
