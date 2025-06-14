package com.arka.microservice.carrito.infraestructure.driven.r2dbc.adapter;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.domain.ports.out.ProductCarPersistencePort;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.ProductCarEntity;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.mapper.IProductCarEntityMapper;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.repository.IProductCarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class ProductCarAdapterImpl implements ProductCarPersistencePort {
    private final IProductCarRepository repository;
    private final IProductCarEntityMapper mapper;

    /**
     * Funcion que guarda un objeto
     * @param model objeto a guardar
     * @return retorna un objeto mapeado para el dominio
     */
    @Transactional
    @Override
    public Mono<ProductCarModel> save(ProductCarModel model) {
        ProductCarEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    /**
     * Funcion que consulta todos los objetos y los mapea a modelo.
     * @return retorna todos los objetos mapeados para el dominio
     */
    @Override
    public Flux<ProductCarModel> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    /**
     * Funcion que consulta un objeto por id y lo mapea a modelo
     * @param id identificador el objeto a buscar
     * @return retorna un objeto mapeado para dominio
     */
    @Override
    public Mono<ProductCarModel> findById(Long id) {
        return repository.findById(id)
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

    /** Funcion que actualiza el objeto en la base de datos
     * @param model data con informacion a actualizar
     * @return mono error o vacio
     */
    @Transactional
    @Override
    public Mono<ProductCarModel> update(ProductCarModel model) {
        ProductCarEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }
}
