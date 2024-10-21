package com.example.management.order_management_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.management.order_management_service.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
