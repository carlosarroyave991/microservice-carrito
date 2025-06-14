package com.arka.microservice.carrito.infraestructure.driver.rest.dto.req;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {
    //El id debe ser nulo para una operación de creación
    @Null(message = "The id must be null for a create operation")
    private Long id; // Opcional para 'save', necesario para 'update'
    private LocalDate createdDate;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
