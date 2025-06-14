package com.arka.microservice.carrito.infraestructure.driven.r2dbc.repository;

import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.CarEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Se ocupa de las interacciones con la base de datos u otra fuente de datos,
 * generalmente utilizando entidades JPA u otro mapeo relacionado con la base de datos.
 * Puede no necesitar todos los métodos definidos en el puerto.
 */
public interface ICarRepository extends ReactiveCrudRepository<CarEntity, Long> {
}
