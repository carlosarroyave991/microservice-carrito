package com.arka.microservice.carrito.infraestructure.driven.r2dbc.mapper;

import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.CarEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ICarEntityMapper {

    CarModel toModel(CarEntity entity);

    @InheritInverseConfiguration
    CarEntity toEntity(CarModel model);
}
