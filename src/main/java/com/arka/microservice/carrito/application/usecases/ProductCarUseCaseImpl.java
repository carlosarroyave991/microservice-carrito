package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.domain.ports.in.IProductCarPortUseCase;
import com.arka.microservice.carrito.domain.ports.out.ProductCarPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.DB_EMPTY;
import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.ID_NOT_FOUND;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Service
@RequiredArgsConstructor
public class ProductCarUseCaseImpl implements IProductCarPortUseCase {
    private final ProductCarPersistencePort service;

    /**
     * Servicio usado para crear un objeto de forma reactiva.
     * @param model objeto con los parámetros necesarios para la creación.
     * @return retorna un Mono con el objeto creado o un error.
     */
    @Override
    public Mono<ProductCarModel> createProductCar(ProductCarModel model) {
        return service.save(model);
    }

    /**
     * Servicio que obtiene todas las categorias existentes de forma reactiva
     * @return returna un Flux que emite cada category o error si la lista esta vacia
     */
    @Override
    public Flux<ProductCarModel> getAllProductCar() {
        return service.findAll()
                .switchIfEmpty(Flux.error(new NotFoundException(DB_EMPTY)));
    }

    /** Servicio que obtiene un objeto por medio de un identificador
     * @param id identificador del objeto
     * @return objeto mono o mono vacio
     */
    @Override
    public Mono<ProductCarModel> getByProductCarId(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)));
    }
}
