package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.domain.ports.in.IProductCarPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.ProductCarResquestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.ProductCarResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.IProductCarMapperDto;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    /**
     * Endpoint para obtener todas los objetos.
     * @return Un Flux de objetos como DTO.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductCarResponseDto> getAllProductCar(){
        return service.getAllProductCar()
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para obtener un usuario por su ID.
     * @param id ID del usuario a consultar.
     * @return objeto encontrado o respuesta 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductCarResponseDto> getProductCarById(@PathVariable("id")Long id){
        return service.getByProductCarId(id)
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para crear un objeto
     * @param request data que tendra el objeto
     * @return un mono o un vacio creado en forma de dto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductCarResponseDto> createProductCar(ProductCarResquestDto request){
        ProductCarModel model = mapper.toModel(request);
        return service.createProductCar(model)
                .map(mapper::toResponse);
    }
}
