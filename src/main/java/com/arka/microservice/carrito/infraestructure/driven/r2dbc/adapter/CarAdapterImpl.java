package com.arka.microservice.carrito.infraestructure.driven.r2dbc.adapter;

import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.CarEntity;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.mapper.ICarEntityMapper;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.repository.ICarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * El adaptador se encargara de conectar ambas capas, pasando la informacion de la entidad
 * al modelo de dominio.
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CarAdapterImpl implements CarPersistencePort {
    private final ICarRepository repository;
    private final ICarEntityMapper mapper;

    /**
     * Funcion que consulta un objeto por id y lo mapea a modelo
     * @param id identificador el objeto a buscar
     * @return retorna un objeto mapeado para dominio
     */
    @Override
    public Mono<CarModel> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    /**
     * Funcion que consulta todos los objetos y los mapea a modelo.
     * @return retorna todos los objetos mapeados para el dominio
     */
    @Override
    public Flux<CarModel> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    /**
     * Funcion actualiza un objeto existente. Se busca por id
     * @param model objeto a actualizar, contiene un id en sus atributos
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<CarModel> update(CarModel model) {
        CarEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    /**
     * Funcion que guarda un objeto
     * @param model objeto a guardar
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<CarModel> save(CarModel model) {
        CarEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    /**
     * Funcion que eliminar un objeto dado su id y retorna el Mono<Void> correspondiente.
     * @param id identificador del objeto a eliminar
     */
    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
