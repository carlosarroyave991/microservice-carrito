package com.arka.microservice.carrito.infraestructure.driver.rest.controller;

import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.domain.ports.in.ICarPortUseCase;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.req.CarRequestDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.dto.resp.CarResponseDto;
import com.arka.microservice.carrito.infraestructure.driver.rest.mapper.ICarMapperDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Obtener todos los carritos", description = "Retorna una lista de todos los carritos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de carritos obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron carritos")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CarResponseDto> getAllCars(){
        return service.getAllOders()
                .map(mapper::toResponse);
    }

    @Operation(summary = "Obtener carrito por ID", description = "Retorna un carrito específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CarResponseDto> getCarById(@Parameter(description = "ID del carrito") @PathVariable("id")Long id){
        return service.getCarById(id)
                .map(mapper::toResponse);
    }

    @Operation(summary = "Crear nuevo carrito", description = "Crea un nuevo carrito con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrito creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createCar(@Parameter(description = "Datos del carrito a crear") @Valid @RequestBody CarRequestDto request){
        CarModel model = mapper.toModel(request);
        return service.createCar(model).then();
    }

    @Operation(summary = "Actualizar carrito", description = "Actualiza un carrito existente con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateCar(@Parameter(description = "Datos del carrito a actualizar") @RequestBody CarRequestDto request, @Parameter(description = "ID del carrito") @PathVariable("id")Long id){
        CarModel model = mapper.toModel(request);
        return service.updateCar(model, id)
                .then();
    }

    @Operation(summary = "Eliminar carrito", description = "Elimina un carrito por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Carrito eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCar(@Parameter(description = "ID del carrito a eliminar") @PathVariable("id")Long id){
        return service.deleteCarById(id);
    }
}
