package com.arka.microservice.carrito.infraestructure.driver.rest.mapper;


import com.arka.microservice.carrito.domain.models.OrderModel;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.OrderRequestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.OrderResponseDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IOrderMapperDto {
    OrderModel toModel(OrderRequestDto request);

    @InheritInverseConfiguration
    OrderResponseDto toResponse(OrderModel model);

    // pendiente de orderModel con data de storage y de carrito
}
