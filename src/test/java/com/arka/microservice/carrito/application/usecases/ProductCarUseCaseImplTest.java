package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.DuplicateResourceException;
import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.*;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import com.arka.microservice.carrito.domain.ports.out.ProductCarPersistencePort;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCarUseCaseImplTest {

    @Mock
    private ProductCarPersistencePort productCarPersistencePort;
    
    @Mock
    private CarPersistencePort carPersistencePort;
    
    @Mock
    private WebClient productWebClient;
    
    @Mock
    private WebClient userWebClient;
    


    @InjectMocks
    private ProductCarUseCaseImpl productCarUseCase;

    private ProductCarModel productCarModel;
    private CarModel carModel;
    private UserModel userModel;
    private ProductDetailModel productDetailModel;

    @BeforeEach
    void setUp() {
        productCarModel = new ProductCarModel();
        productCarModel.setId(1L);
        productCarModel.setProductId(1L);
        productCarModel.setCarId(1L);
        productCarModel.setQuantity(2);

        carModel = new CarModel();
        carModel.setId(1L);
        carModel.setUserId(1L);
        carModel.setCreatedDate(LocalDate.now());

        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test User");

        productDetailModel = new ProductDetailModel();
        productDetailModel.setId(1L);
        productDetailModel.setName("Test Product");
        productDetailModel.setQuantity(2);
    }

    @Test
    void createProductCar_ShouldThrowException_WhenSaveFails() {
        when(productCarPersistencePort.save(any(ProductCarModel.class))).thenReturn(Mono.error(new RuntimeException("Save failed")));

        StepVerifier.create(productCarUseCase.createProductCar(productCarModel))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getAllProductCar_ShouldReturnFluxOfProductCarModels() {
        when(productCarPersistencePort.findAll()).thenReturn(Flux.just(productCarModel));

        StepVerifier.create(productCarUseCase.getAllProductCar())
                .expectNext(productCarModel)
                .verifyComplete();
    }

    @Test
    void getByProductCarId_ShouldReturnProductCarModel() {
        when(productCarPersistencePort.findById(anyLong())).thenReturn(Mono.just(productCarModel));

        StepVerifier.create(productCarUseCase.getByProductCarId(1L))
                .expectNext(productCarModel)
                .verifyComplete();
    }

    @Test
    void getByProductCarId_ShouldThrowNotFoundException_WhenNotFound() {
        when(productCarPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productCarUseCase.getByProductCarId(1L))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void deleteById_ShouldCompleteSuccessfully() {
        when(productCarPersistencePort.findById(anyLong())).thenReturn(Mono.just(productCarModel));
        when(productCarPersistencePort.delete(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productCarUseCase.deleteById(1L))
                .verifyComplete();
    }

    @Test
    void updateProductCar_ShouldReturnUpdatedProductCarModel_WhenQuantityNotChanged() {
        ProductCarModel updateModel = new ProductCarModel();
        updateModel.setProductId(2L); // Cambio que no afecta cantidad
        
        when(productCarPersistencePort.findById(anyLong())).thenReturn(Mono.just(productCarModel));
        when(productCarPersistencePort.update(any(ProductCarModel.class))).thenReturn(Mono.just(productCarModel));

        StepVerifier.create(productCarUseCase.updateProductCar(updateModel, 1L))
                .expectNext(productCarModel)
                .verifyComplete();
    }



    @Test
    void getProductsByCarId_ShouldThrowNotFoundException_WhenCarNotFound() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productCarUseCase.getProductsByCarId(1L))
                .expectError(NotFoundException.class)
                .verify();
    }
}