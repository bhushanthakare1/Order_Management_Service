package com.example.management.order_management_service.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.management.order_management_service.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	

}
