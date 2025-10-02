package com.jojoldu.book.springboot.domain.orders;

import com.jojoldu.book.springboot.domain.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT p FROM order p ORDER BY p.id DESC")
    List<Order> findAllDesc();
}

