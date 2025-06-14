package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.domain.ports.in.ICarPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.CarRequestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.CarResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.ICarMapperDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
@Tag(name = "Car Controller", description = "Endpoints para la gestion de carritos")
public class CarController {
    private final ICarPortUseCase service;
    private final ICarMapperDto mapper;

    /**
     * Endpoint para obtener todas los objetos.
     * @return Un Flux de objetos como DTO.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CarResponseDto> getAllCars(){
        return service.getAllOders()
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para obtener un objeto por su ID.
     * @param id ID del objeto a consultar.
     * @return objeto encontrado o respuesta 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CarResponseDto> getCarById(@PathVariable("id")Long id){
        return service.getCarById(id)
                .map(mapper::toResponse);
    }

    /**
     * Endpoint para crear un objeto
     * @param request data que tendra el objeto
     * @return objeto creado en forma de dto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createCar(@Valid @RequestBody CarRequestDto request){
        CarModel model = mapper.toModel(request);
        return service.createCar(model).then();
    }

    /**
     * Endpoint para crear un product
     * @param request data que tendra el prudcto
     * @return product creado en forma de dto
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateCar(@RequestBody CarRequestDto request, @PathVariable("id")Long id){
        CarModel model = mapper.toModel(request);
        return service.updateCar(model, id)
                .then();
    }

    /**
     * Endpoint para eliminar un objeto por su ID
     * @return Un Mono vacío que indica que la operación se completó.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCar(@PathVariable("id")Long id){
        return service.deleteCarById(id);
    }
}
