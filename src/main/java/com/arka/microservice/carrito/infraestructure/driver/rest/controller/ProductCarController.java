package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.CarWithProductsModel;
import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.domain.models.ProductDetailModel;
import com.arka.microservice.carrito.domain.ports.in.IProductCarPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.ProductCarResquestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.ProductCarResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.IProductCarMapperDto;
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
@RequiredArgsConstructor
@RequestMapping("/api/product-car")
@Tag(name = "Product Car Controller", description = "Endpoints para la gestion de la relacion entre Product y Car")
public class ProductCarController {
    private final IProductCarPortUseCase service;
    private final IProductCarMapperDto mapper;

    @Operation(summary = "Obtener todos los productos del carrito", description = "Retorna una lista de todas las relaciones producto-carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos en carritos")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductCarResponseDto> getAllProductCar(){
        return service.getAllProductCar()
                .map(mapper::toResponse);
    }

    @Operation(summary = "Obtener producto-carrito por ID", description = "Retorna una relación producto-carrito específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductCarResponseDto> getProductCarById(@Parameter(description = "ID de la relación producto-carrito") @PathVariable("id")Long id){
        return service.getByProductCarId(id)
                .map(mapper::toResponse);
    }

    @Operation(summary = "Obtener carrito con productos", description = "Retorna un carrito con todos sus productos y detalles del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito con productos obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/car/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CarWithProductsModel> getProductsByCarId(@Parameter(description = "ID del carrito") @PathVariable("carId")Long carId){
        return service.getProductsByCarId(carId);
    }

    @Operation(summary = "Agregar producto al carrito", description = "Agrega un producto a un carrito específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto agregado al carrito exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductCarResponseDto> createProductCar(@Parameter(description = "Datos del producto a agregar al carrito") @Valid @RequestBody ProductCarResquestDto request){
        ProductCarModel model = mapper.toModel(request);
        return service.createProductCar(model)
                .map(mapper::toResponse);
    }

    @Operation(summary = "Actualizar producto en carrito", description = "Actualiza la cantidad o datos de un producto en el carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto en carrito actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto en carrito no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductCarResponseDto> updateProductCar(@Parameter(description = "Datos del producto en carrito a actualizar") @RequestBody ProductCarResquestDto request,
                                                        @Parameter(description = "ID de la relación producto-carrito") @PathVariable("id")Long id){
        ProductCarModel model = mapper.toModel(request);
        return service.updateProductCar(model, id)
                .map(mapper::toResponse);
    }

    @Operation(summary = "Eliminar producto del carrito", description = "Elimina un producto específico del carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado del carrito exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto en carrito no encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProductCar(@Parameter(description = "ID de la relación producto-carrito a eliminar") @PathVariable("id")Long id){
        return service.deleteById(id);
    }
}
