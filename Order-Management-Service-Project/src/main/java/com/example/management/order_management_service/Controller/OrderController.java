package com.example.management.order_management_service.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.management.order_management_service.Model.Order;
import com.example.management.order_management_service.Model.Product;
import com.example.management.order_management_service.Service.OrderService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("/api")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/orders")
	public ResponseEntity<?> createorder(@RequestParam Long productId, @RequestParam int quantity) {
		try {
			Order order = orderService.createorder(productId, quantity);
			return ResponseEntity.status(201).body(order);

		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@GetMapping("/orders")
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		return orderService.GetOrderById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(404).build());

	}

	@PutMapping("/orders/{id}")
	public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestParam int quantity) {
		try {
			Order order = orderService.updateOrder(id, quantity);
			return ResponseEntity.ok(order);
		} catch (RuntimeException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
		try {
			orderService.deleteOrder(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	} 

	@PostMapping("/products")
	public ResponseEntity<Product> CreateProduct(@RequestParam String name, @RequestParam int stock) {
		Product product = orderService.createProduct(name, stock);
		return ResponseEntity.status(201).body(product);
	}
}
