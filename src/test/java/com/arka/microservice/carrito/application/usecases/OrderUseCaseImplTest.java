package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.domain.models.OrderModel;
import com.arka.microservice.carrito.domain.models.UserModel;
import com.arka.microservice.carrito.domain.models.enums.OrderStatus;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import com.arka.microservice.carrito.domain.ports.out.OrderPersistencePort;
import com.arka.microservice.carrito.domain.service.RandomReferenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseImplTest {

    @Mock
    private OrderPersistencePort orderPersistencePort;
    
    @Mock
    private CarPersistencePort carPersistencePort;
    
    @Mock
    private WebClient userWebClient;
    
    @Mock
    private WebClient lambdaWebClient;
    
    @Mock
    private RandomReferenceGenerator randomReferenceGenerator;
    


    @InjectMocks
    private OrderUseCaseImpl orderUseCase;

    private OrderModel orderModel;
    private CarModel carModel;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setReference("REF123");
        orderModel.setCarId(1L);
        orderModel.setOrderStatus(OrderStatus.waiting);
        orderModel.setAmountValue(7);
        orderModel.setSalePrice(BigDecimal.valueOf(1.500));
        orderModel.setOrderDate(LocalDate.now());

        carModel = new CarModel();
        carModel.setId(1L);
        carModel.setUserId(1L);

        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test User");
    }

    @Test
    void createOrder_ShouldReturnOrderModel() {
        when(randomReferenceGenerator.Generate()).thenReturn("REF123");
        when(orderPersistencePort.save(any(OrderModel.class))).thenReturn(Mono.just(orderModel));

        StepVerifier.create(orderUseCase.createOrder(orderModel))
                .expectNext(orderModel)
                .verifyComplete();
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrderModel_WhenStatusNotChanged() {
        OrderModel updateModel = new OrderModel();
        updateModel.setPaymentMethod("CARD");
        
        when(orderPersistencePort.findById(anyLong())).thenReturn(Mono.just(orderModel));
        when(orderPersistencePort.update(any(OrderModel.class))).thenReturn(Mono.just(orderModel));

        StepVerifier.create(orderUseCase.updateOrder(updateModel, 1L))
                .expectNext(orderModel)
                .verifyComplete();
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrderModel_WhenStatusChanged() {
        OrderModel updateModel = new OrderModel();
        updateModel.setOrderStatus(OrderStatus.shipped);
        
        when(orderPersistencePort.findById(anyLong())).thenReturn(Mono.just(orderModel));
        when(orderPersistencePort.update(any(OrderModel.class))).thenReturn(Mono.just(orderModel));
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.error(new RuntimeException("Notification failed")));

        StepVerifier.create(orderUseCase.updateOrder(updateModel, 1L))
                .expectNext(orderModel)
                .verifyComplete();
    }

    @Test
    void getAllOders_ShouldReturnFluxOfOrderModels() {
        when(orderPersistencePort.findAll()).thenReturn(Flux.just(orderModel));

        StepVerifier.create(orderUseCase.getAllOders())
                .expectNext(orderModel)
                .verifyComplete();
    }

    @Test
    void getOrderById_ShouldReturnOrderModel() {
        when(orderPersistencePort.findById(anyLong())).thenReturn(Mono.just(orderModel));

        StepVerifier.create(orderUseCase.getOrderById(1L))
                .expectNext(orderModel)
                .verifyComplete();
    }

    @Test
    void getOrderById_ShouldThrowNotFoundException_WhenOrderNotFound() {
        when(orderPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(orderUseCase.getOrderById(1L))
                .expectError(NotFoundException.class)
                .verify();
    }
}