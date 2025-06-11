package com.arka.microservice.carrito.infraestructure.driver.rest.mapper;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.ProductCarEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IProductCarEntityMapper {
    ProductCarEntity toEntity(ProductCarModel model);

    @InheritInverseConfiguration
    ProductCarModel toModel(ProductCarEntity entity);
}
