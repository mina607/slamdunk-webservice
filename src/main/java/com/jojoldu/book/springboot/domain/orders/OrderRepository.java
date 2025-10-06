package com.jojoldu.book.springboot.domain.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT p FROM Orders p ORDER BY p.id DESC")
    List<Orders> findAllDesc();
}

