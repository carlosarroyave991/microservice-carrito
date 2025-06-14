package com.arka.microservice.carrito.infraestructure.driver.rest.mapper;

import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.CarRequestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.CarResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ICarMapperDto {
    CarModel toModel(CarRequestDto request);

    CarResponseDto toResponse(CarModel model);
}
