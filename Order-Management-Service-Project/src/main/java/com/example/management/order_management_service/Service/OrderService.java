package com.example.management.order_management_service.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.management.order_management_service.Model.Order;
import com.example.management.order_management_service.Model.Product;
import com.example.management.order_management_service.Repository.OrderRepository;
import com.example.management.order_management_service.Repository.ProductRepository;

@Service
public class OrderService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	public Order createorder(Long productId, int quantity) {
		Optional<Product> Optionalproduct = productRepository.findById(productId);

		if (Optionalproduct.isEmpty()) {
			throw new RuntimeException("product not found");
		}
		Product product = Optionalproduct.get();

		if (Optionalproduct.get().getStock() < quantity) {
			throw new RuntimeException("insufficient stock");
		}

		product.setStock(product.getStock() - quantity);
		productRepository.save(product);

		Order order = new Order();
		order.setProduct(product);
		order.setQuantity(quantity);
		order.setStatus(Order.OrderStatus.PENDING);

		return orderRepository.save(order);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();

	}

	public Optional<Order> GetOrderById(Long id) {
		Optional<Order> optionalOrder = orderRepository.findById(id);
		return optionalOrder;
	}

	@Transactional
	public Order updateOrder(Long id, int quantity) {
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if (optionalOrder.isEmpty()) {
			throw new RuntimeException("Order not found.");
		}

		Order order = optionalOrder.get();

		if (order.getStatus() == Order.OrderStatus.COMPLETED) {
			throw new RuntimeException("Cannot update a completed order.");
		}

		Product product = order.getProduct();
		product.setStock(product.getStock() + order.getQuantity());
		if (product.getStock() < quantity) {
			throw new RuntimeException("Insufficient stock to update the order.");
		}
		product.setStock(product.getStock() - quantity);
		productRepository.save(product);

		order.setQuantity(quantity);
		return orderRepository.save(order);
	}

	
	public void deleteOrder(Long id) {
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if (optionalOrder.isEmpty()) {
			throw new RuntimeException("Order not found");
		}
		Order order = optionalOrder.get();

		if (order.getStatus() == Order.OrderStatus.COMPLETED) {
			throw new RuntimeException("Cannot delete a completed order");
		}

		orderRepository.deleteById(id);

	}

	public Product createProduct(String name, int stock) {
		Product product = new Product();
		product.setName(name);
		product.setStock(stock);
		return productRepository.save(product);
	}

	@Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
	public void updateOrderStatuses() {
		List<Order> orders = orderRepository.findAll();
		for (Order order : orders) {
			if (order.getStatus() == Order.OrderStatus.PENDING) {
				order.setStatus(Order.OrderStatus.COMPLETED);
				orderRepository.save(order);
			}
		}
	}

}
