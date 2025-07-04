package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.DuplicateResourceException;
import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.EmailNotificationModel;
import com.arka.microservice.carrito.domain.models.OrderModel;
import com.arka.microservice.carrito.domain.models.UserModel;
import com.arka.microservice.carrito.domain.models.enums.OrderStatus;
import com.arka.microservice.carrito.domain.ports.in.IOrderPortUseCase;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import com.arka.microservice.carrito.domain.ports.out.OrderPersistencePort;
import com.arka.microservice.carrito.domain.service.RandomReferenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.DB_EMPTY;
import static com.arka.microservice.carrito.domain.exception.error.CommonErrorCode.ID_NOT_FOUND;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Service
@RequiredArgsConstructor
public class OrderUseCaseImpl implements IOrderPortUseCase {
    private final OrderPersistencePort service;
    private final CarPersistencePort carService;
    private final WebClient userWebClient;
    private final WebClient lambdaWebClient;
    private final RandomReferenceGenerator generatorRef;

    /**
     * Servicio usado para crear una orden de forma reactiva.
     * @param model objeto order con los parámetros necesarios para la creación.
     * @return retorna un Mono con la orden creada o un error.
     */
    @Override
    public Mono<OrderModel> createOrder(OrderModel model) {
        model.setOrderDate(LocalDate.now());
        model.setReference(generatorRef.Generate());
        model.setOrderStatus(OrderStatus.waiting);
        model.setAmountValue(7);
        model.setSalePrice(BigDecimal.valueOf(1.500));
        return service.save(model);
    }

    /**
     * Servicio que permite actualizar una direccion especifica de manera reactiva
     * @param model objeto con la data a actualizar
     * @param id identificador del objeto
     * @return retorna un Mono con la direccion actualizada o error si no existe
     */
    @Override
    public Mono<OrderModel> updateOrder(OrderModel model, Long id) {
        return service.findById(id)
                .flatMap(existing -> {
                    OrderStatus oldStatus = existing.getOrderStatus();
                    existing.setId(id);
                    if (model.getPaymentMethod() != null) existing.setPaymentMethod(model.getPaymentMethod());
                    if (model.getAmountValue() != null) existing.setAmountValue(model.getAmountValue());
                    if (model.getSalePrice() != null) existing.setSalePrice(model.getSalePrice());
                    if (model.getOrderStatus() != null) existing.setOrderStatus(model.getOrderStatus());
                    
                    return service.update(existing)
                            .flatMap(updatedOrder -> {
                                // Si cambió el estado, enviar notificación
                                if (model.getOrderStatus() != null && !model.getOrderStatus().equals(oldStatus)) {
                                    return sendOrderStatusNotification(updatedOrder)
                                            .thenReturn(updatedOrder);
                                }
                                return Mono.just(updatedOrder);
                            });
                });
    }

    private Mono<Void> sendOrderStatusNotification(OrderModel order) {
        return carService.findById(order.getCarId())
                .flatMap(car -> userWebClient.get()
                        .uri("/api/users/{id}", car.getUserId())
                        .retrieve()
                        .bodyToMono(UserModel.class))
                .flatMap(user -> {
                    EmailNotificationModel notification = new EmailNotificationModel();
                    notification.setTo(user.getEmail());
                    notification.setSubject("Actualización de tu orden");
                    notification.setBody("Tu orden #" + order.getReference() + " ha cambiado a estado: " + order.getOrderStatus());
                    
                    return lambdaWebClient.post()
                            .bodyValue(notification)
                            .retrieve()
                            .bodyToMono(Void.class);
                })
                .onErrorResume(error -> {
                    error.printStackTrace();
                    return Mono.empty(); // No fallar la actualización si falla la notificación
                });
    }


    /** Servicio que obtiene todos los objetos de forma reactiva
     * @return un flux que emite o un flux error si la lista es vacia
     */
    @Override
    public Flux<OrderModel> getAllOders() {
        return service.findAll()
                .switchIfEmpty(Flux.error(new DuplicateResourceException(DB_EMPTY)));
    }

    /** Servicio que busca por un objeto por identificador
     * @param id identificador del objeto a buscar
     * @return retorna un mono o un mono error en caso de vacio
     */
    @Override
    public Mono<OrderModel> getOrderById(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ID_NOT_FOUND)));
    }
}
