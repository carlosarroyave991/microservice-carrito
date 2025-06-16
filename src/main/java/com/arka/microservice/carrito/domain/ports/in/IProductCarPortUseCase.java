package com.arka.microservice.carrito.domain.ports.in;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.domain.models.CarWithProductsModel;
import com.arka.microservice.carrito.domain.models.ProductDetailModel;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface IProductCarPortUseCase {
    Mono<ProductCarModel> createProductCar(ProductCarModel model);
    Flux<ProductCarModel> getAllProductCar();
    Mono<ProductCarModel> getByProductCarId(Long id);
    Mono<CarWithProductsModel> getProductsByCarId(Long carId);
    Mono<Void> deleteById(Long id);
    Mono<ProductCarModel> updateProductCar(ProductCarModel model, Long id);
}
