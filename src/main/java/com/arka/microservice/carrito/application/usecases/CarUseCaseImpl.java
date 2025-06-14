package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.DuplicateResourceException;
import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.domain.ports.in.ICarPortUseCase;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.DB_EMPTY;
import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.ID_NOT_FOUND;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CarUseCaseImpl implements ICarPortUseCase {
    private final CarPersistencePort service;

    /**
     * Servicio usado para crear un objeto de forma reactiva.
     * @param model objeto con los parámetros necesarios para la creación.
     * @return retorna un Mono con el objeto creado o un error.
     */
    @Transactional
    @Override
    public Mono<CarModel> createCar(CarModel model) {
        model.setCreatedDate(LocalDate.now());
        return service.save(model);
    }

    /**
     * Servicio que permite actualizar un objeto específico de forma reactiva.
     * @param model objeto con los datos a actualizar.
     * @param id   identificador del objeto.
     * @return retorna un Mono con el objeto actualizado o error si no existe.
     */
    @Transactional
    @Override
    public Mono<CarModel> updateCar(CarModel model, Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)))
                .flatMap(existing ->{
                    existing.setId(id);
                    if (model.getUserId() != null)existing.setUserId(model.getUserId());
                    return service.update(existing);
                });
    }

    /**
     * Servicio que obtiene todos los objetos existentes de forma reactiva.
     * @return retorna un Flux que emite cada objeto o error si la lista está vacía.
     */
    @Override
    public Flux<CarModel> getAllOders() {
        return service.findAll()
                .switchIfEmpty(Flux.error(new DuplicateResourceException(DB_EMPTY)));
    }

    /**
     * Servicio para obtener un objeto especifico de forma reactiva.
     * @param id parámetro usado para la consulta del objeto.
     * @return retorna un Mono que emite el objeto o error si no se encuentra.
     */
    @Override
    public Mono<CarModel> getCarById(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)));
    }

    /**
     * Funcion que eliminar un objeto dado su id y retorna el Mono<Void> correspondiente.
     * @param id identificador del objeto a eliminar
     */
    @Override
    public Mono<Void> deleteCarById(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)))
                .flatMap(existing -> service.delete(existing.getId()));
    }
}
