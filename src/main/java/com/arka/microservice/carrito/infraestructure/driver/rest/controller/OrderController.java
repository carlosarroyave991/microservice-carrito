package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.OrderModel;
import com.arka.microservice.carrito.domain.ports.in.IOrderPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.OrderRequestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.OrderResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.IOrderMapperDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoint para la gestion de las ordenes de compra")
public class OrderController {
    private final IOrderPortUseCase service;
    private final IOrderMapperDto mapper;

    /**
     * Endpoint para crear un order
     * La validación de datos (por ejemplo, @Valid) se realiza en el DTO recibido.
     * @param request datos del order
     * @return mono creado en forma de dto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createOrder(@Valid @RequestBody OrderRequestDto request){
        OrderModel model = mapper.toModel(request);
        return service.createOrder(model).then();
    }

    /**
     * Endpoint para actualizar un order
     * @param id identificador del objeto
     * @param request datos del order
     * @return objeto actualizado en forma de dto
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateOder(@RequestBody OrderRequestDto request, @PathVariable("id")Long id){
        OrderModel model = mapper.toModel(request);
        return service.updateOrder(model, id).then();
    }

    /**
     * Endpoint para obtener todos los objetos
     * @return fluz que emite los objetos transformados en dto
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderResponseDto> getAllOrders(){
        return service.getAllOders()
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para obtener un objeto especifico
     * @param id identificador del objeto
     * @return objeto creado en forma de dto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<OrderResponseDto> getOrderById(@PathVariable("id")Long id){
        return service.getOrderById(id)
                .map(mapper::toResponse);
    }
}
