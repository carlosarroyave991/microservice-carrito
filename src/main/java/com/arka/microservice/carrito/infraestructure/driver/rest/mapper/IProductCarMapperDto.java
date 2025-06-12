package com.arka.microservice.carrito.infraestructure.driver.rest.mapper;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.ProductCarEntity;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.ProductCarResquestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.ProductCarResponseDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IProductCarMapperDto {
    ProductCarModel toModel(ProductCarResquestDto resquest);

    @InheritInverseConfiguration
    ProductCarResponseDto toResponse(ProductCarModel model);
}
