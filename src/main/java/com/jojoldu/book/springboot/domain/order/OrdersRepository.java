package com.jojoldu.book.springboot.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // 특정 사용자의 진행중인 주문 조회
    @Query("SELECT o FROM Orders o WHERE o.userId = ?1 AND o.status != 'COMPLETED' ORDER BY o.id DESC")
    List<Orders> findCurrentOrdersByUserId(String userId);

    // 특정 사용자의 완료된 주문 조회
    @Query("SELECT o FROM Orders o WHERE o.userId = ?1 AND o.status = 'COMPLETED' ORDER BY o.id DESC")
    List<Orders> findCompletedOrdersByUserId(String userId);
}