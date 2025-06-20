package com.arka.microservice.carrito.domain.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class CarWithProductsModel {
    private Long id;
    private LocalDate createdDate;
    //private String name;
    //private Long userId;
    List<ProductDetailModel> productDetailModels;
    UserModel userModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<ProductDetailModel> getProductDetailModels() {
        return productDetailModels;
    }

    public void setProductDetailModels(List<ProductDetailModel> productDetailModels) {
        this.productDetailModels = productDetailModels;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
