package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.DuplicateResourceException;
import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.*;
import com.arka.microservice.carrito.domain.ports.in.IProductCarPortUseCase;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import com.arka.microservice.carrito.domain.ports.out.ProductCarPersistencePort;
import com.arka.microservice.carrito.infraestructure.driven.r2dbc.config.webclient.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.*;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Service
@RequiredArgsConstructor
public class ProductCarUseCaseImpl implements IProductCarPortUseCase {
    private final ProductCarPersistencePort service;
    private final CarPersistencePort carService;
    private final WebClient productWebClient;

    /**
     * Servicio usado para crear un objeto de forma reactiva.
     * @param model objeto con los parámetros necesarios para la creación.
     * @return retorna un Mono con el objeto creado o un error.
     */
    @Transactional
    @Override
    public Mono<ProductCarModel> createProductCar(ProductCarModel model) {
        return service.save(model)
                .flatMap(savedSupply ->{
                    return productWebClient.put()
                            .uri("/api/product/{id}/stock", model.getProductId())
                            .bodyValue(new StockUpdateModel(-model.getQuantity()))
                            .retrieve()
                            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(errorBody ->{
                                                System.out.println("Error en la llamada al microservicio: " + response.statusCode());
                                                System.out.println("Cuerpo del error: " + errorBody);
                                                return Mono.error(new DuplicateResourceException(WEBCLIENT));
                                            }))
                            .bodyToMono(Void.class)
                            .thenReturn(savedSupply);
                })
                .onErrorResume( error -> {
                    error.printStackTrace();
                    return Mono.error(new DuplicateResourceException(WEBCLIENT));
                });
    }

    /**
     * Servicio que obtiene todas las categorias existentes de forma reactiva
     * @return returna un Flux que emite cada category o error si la lista esta vacia
     */
    @Override
    public Flux<ProductCarModel> getAllProductCar() {
        return service.findAll()
                .switchIfEmpty(Flux.error(new NotFoundException(DB_EMPTY)));
    }

    /** Servicio que obtiene un objeto por medio de un identificador
     * @param id identificador del objeto
     * @return objeto mono o mono vacio
     */
    @Override
    public Mono<ProductCarModel> getByProductCarId(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)));
    }

    /**
     * Servicio para eliminar un objeto de forma reactiva.
     * @param id identificador del objeto a eliminar.
     * @return retorna un Mono<Void> que completa o emite un error si el objeto no existe.
     */
    @Override
    public Mono<Void> deleteById(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)))
                .flatMap(existing -> service.delete(id));
    }

    /** Servicio que permite actualizar el objeto de manera reactiva
     * @param id identificador el objeto
     * @param model data del objeto a modificar
     * @return objeto mono con la data encontrada o mono error
     */
    @Transactional
    @Override
    public Mono<ProductCarModel> updateProductCar(ProductCarModel model, Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)))
                .flatMap(existing -> {
                    Integer oldQuantity = existing.getQuantity();

                    if (model.getProductId() != null) existing.setProductId(model.getProductId());
                    if (model.getCarId() != null) existing.setCarId(model.getCarId());
                    if (model.getQuantity() != null) existing.setQuantity(model.getQuantity());

                    return service.update(existing)
                            .flatMap(savedSupply -> {
                                if (model.getQuantity() != null && !model.getQuantity().equals(oldQuantity)) {
                                    // Invertimos el signo: si la nueva cantidad es mayor, debemos RESTAR más del stock
                                    Integer stockAdjustment = -(model.getQuantity() - oldQuantity);

                                    return productWebClient.put()
                                            .uri("/api/product/{id}/stock", existing.getProductId())
                                            .bodyValue(new StockUpdateModel(stockAdjustment))
                                            .retrieve()
                                            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                                    response -> response.bodyToMono(String.class)
                                                            .flatMap(errorBody -> Mono.error(new DuplicateResourceException(WEBCLIENT))))
                                            .bodyToMono(Void.class)
                                            .thenReturn(savedSupply);
                                }
                                return Mono.just(savedSupply);
                            });
                });
    }

    /**
     * Servicio que traera todos los productos que le pertenecen a un carrito
     * @param carId identificador del carrito
     * @return objeto flux o flux vacio
     */
    @Override
    public Mono<CarWithProductsModel> getProductsByCarId(Long carId) {
        return carService.findById(carId)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)))
                .flatMap(carModel -> {
                    // Crear el modelo de respuesta
                    CarWithProductsModel response = new CarWithProductsModel();
                    response.setId(carModel.getId());
                    response.setCreatedDate(carModel.getCreatedDate());
                    response.setUserId(carModel.getUserId());

                    // Obtener todos los productos del carrito
                    return service.findAllByCarId(carId)
                            .collectList()
                            .flatMap(productCars -> {
                                // Si no hay productos devolver una lista vacia
                                if (productCars.isEmpty()) {
                                    response.setProductDetailModels(List.of());
                                    return Mono.just(response);
                                }

                                // Extraer los IDs de productos
                                List<Long> productIds = productCars.stream() //stream me permite realizar operaciones en cadena
                                        .map(ProductCarModel::getProductId)
                                        .collect(Collectors.toList());

                                // Crear mapa de ID de producto -> cantidad
                                Map<Long, Integer> quantityMap = productCars.stream() //stream me permite realizar operaciones en cadena
                                        .collect(Collectors.toMap(
                                                ProductCarModel::getProductId,
                                                ProductCarModel::getQuantity
                                        ));

                                // Llamar al endpoint /by-ids con todos los IDs
                                return productWebClient.post()
                                        .uri("/api/product/by-ids")
                                        .bodyValue(new ProductIdsRequestDto(productIds))
                                        .retrieve()
                                        .bodyToFlux(ProductDetailModel.class)
                                        .map(productDetail -> {
                                            // Asignar la cantidad desde el mapa
                                            productDetail.setQuantity(quantityMap.get(productDetail.getId()));
                                            return productDetail;
                                        })
                                        .collectList()
                                        .map(productList -> {
                                            response.setProductDetailModels(productList);
                                            return response;
                                        })
                                        .onErrorResume(error -> {
                                            error.printStackTrace();
                                            return Mono.error(new DuplicateResourceException(WEBCLIENT));
                                        });
                            });
                });
    }
}
