package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.CarWithProductsModel;
import com.arka.microservice.carrito.domain.models.ProductCarModel;
import com.arka.microservice.carrito.domain.models.ProductDetailModel;
import com.arka.microservice.carrito.domain.ports.in.IProductCarPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.ProductCarResquestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.ProductCarResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.IProductCarMapperDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
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
     * Endpoint para obtener todos los ids de los productos que pertenecena  un carrito
     * @param carId identificador del carrito
     * @return objeto mono o vacio creado en formato de dto
     */
    @GetMapping("/car/{carId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Obtiene un carrito con todos sus productos")
    public Mono<CarWithProductsModel> getProductsByCarId(@PathVariable("carId")Long carId){
        return service.getProductsByCarId(carId);
    }

    /**
     * Endpoint para crear un objeto
     * @param request data que tendra el objeto
     * @return un mono o un vacio creado en forma de dto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductCarResponseDto> createProductCar(@Valid @RequestBody ProductCarResquestDto request){
        ProductCarModel model = mapper.toModel(request);
        return service.createProductCar(model)
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para actualizar un objeto
     * @param id identificador del objeto
     * @param request datos del objeto
     * @return objeto actualizado en forma de dto
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductCarResponseDto> updateProductCar(@RequestBody ProductCarResquestDto request,
                                                        @PathVariable("id")Long id){
        ProductCarModel model = mapper.toModel(request);
        return service.updateProductCar(model, id)
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para eliminar un objeto
     * @param id identificador del objeto a eliminar
     * @return Mono vacio despues de borrarlo
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProductCar(@PathVariable("id")Long id){
        return service.deleteById(id);
    }
}
