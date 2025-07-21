package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.OrderModel;
import com.arka.microservice.carrito.domain.ports.in.IOrderPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.OrderRequestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.OrderResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.IOrderMapperDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "Crear nueva orden", description = "Crea una nueva orden de compra con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createOrder(@Parameter(description = "Datos de la orden a crear") @Valid @RequestBody OrderRequestDto request){
        OrderModel model = mapper.toModel(request);
        return service.createOrder(model).then();
    }

    @Operation(summary = "Actualizar orden", description = "Actualiza una orden existente con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateOder(@Parameter(description = "Datos de la orden a actualizar") @RequestBody OrderRequestDto request, @Parameter(description = "ID de la orden") @PathVariable("id")Long id){
        OrderModel model = mapper.toModel(request);
        return service.updateOrder(model, id).then();
    }

    @Operation(summary = "Obtener todas las órdenes", description = "Retorna una lista de todas las órdenes disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron órdenes")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderResponseDto> getAllOrders(){
        return service.getAllOders()
                .map(mapper::toResponse);
    }

    @Operation(summary = "Obtener orden por ID", description = "Retorna una orden específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<OrderResponseDto> getOrderById(@Parameter(description = "ID de la orden") @PathVariable("id")Long id){
        return service.getOrderById(id)
                .map(mapper::toResponse);
    }
}
