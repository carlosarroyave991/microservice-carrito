package com.arka.microservice.carrito.application.usecases;

import com.arka.microservice.carrito.domain.exception.NotFoundException;
import com.arka.microservice.carrito.domain.models.CarModel;
import com.arka.microservice.carrito.domain.ports.out.CarPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarUseCaseImplTest {

    @Mock
    private CarPersistencePort carPersistencePort;

    @InjectMocks
    private CarUseCaseImpl carUseCase;

    private CarModel carModel;

    @BeforeEach
    void setUp() {
        carModel = new CarModel();
        carModel.setId(1L);
        carModel.setUserId(1L);
        carModel.setCreatedDate(LocalDate.now());
    }

    @Test
    void createCar_ShouldReturnCarModel() {
        when(carPersistencePort.save(any(CarModel.class))).thenReturn(Mono.just(carModel));

        StepVerifier.create(carUseCase.createCar(carModel))
                .expectNext(carModel)
                .verifyComplete();
    }

    @Test
    void updateCar_ShouldReturnUpdatedCarModel() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.just(carModel));
        when(carPersistencePort.update(any(CarModel.class))).thenReturn(Mono.just(carModel));

        StepVerifier.create(carUseCase.updateCar(carModel, 1L))
                .expectNext(carModel)
                .verifyComplete();
    }

    @Test
    void updateCar_ShouldThrowNotFoundException_WhenCarNotFound() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(carUseCase.updateCar(carModel, 1L))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void getAllOders_ShouldReturnFluxOfCarModels() {
        when(carPersistencePort.findAll()).thenReturn(Flux.just(carModel));

        StepVerifier.create(carUseCase.getAllOders())
                .expectNext(carModel)
                .verifyComplete();
    }

    @Test
    void getCarById_ShouldReturnCarModel() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.just(carModel));

        StepVerifier.create(carUseCase.getCarById(1L))
                .expectNext(carModel)
                .verifyComplete();
    }

    @Test
    void getCarById_ShouldThrowNotFoundException_WhenCarNotFound() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(carUseCase.getCarById(1L))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void deleteCarById_ShouldCompleteSuccessfully() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.just(carModel));
        when(carPersistencePort.delete(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(carUseCase.deleteCarById(1L))
                .verifyComplete();
    }

    @Test
    void deleteCarById_ShouldThrowNotFoundException_WhenCarNotFound() {
        when(carPersistencePort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(carUseCase.deleteCarById(1L))
                .expectError(NotFoundException.class)
                .verify();
    }
}