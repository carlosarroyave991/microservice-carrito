package com.arka.microservice.carrito.infraestructure.driven.r2dbc.mapper;

import com.arka.microservice.carrito.domain.models.OrderModel;
import com.arka.microservice.carrito.domain.models.enums.OrderStatus;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.entity.OrderEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IOrderEntityMapper {
    @Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "enumToString")
    OrderEntity toEntity(OrderModel model);

    @Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "stringToEnum")
    OrderModel toModel(OrderEntity entity);

    @Named("enumToString")
    default String enumToString(OrderStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToEnum")
    default OrderStatus stringToEnum(String status) {
        return status != null ? OrderStatus.valueOf(status) : null;
    }
}
