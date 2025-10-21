package com.jojoldu.book.springboot.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // 특정 사용자의 진행중인 주문 조회
//    @Query("SELECT o FROM Orders o WHERE o.userId = ?1 AND o.status != 'COMPLETED' ORDER BY o.id DESC")
//    List<Orders> findCurrentOrdersByUserId(String userId);
    @Query("SELECT o FROM Orders o WHERE o.roomNumber = :roomNumber AND o.status != 'COMPLETED' ORDER BY o.id DESC")
    List<Orders> findCurrentOrdersByRoom(@Param("roomNumber") String roomNumber);


    // 특정 사용자의 완료된 주문 조회
//    @Query("SELECT o FROM Orders o WHERE o.userId = ?1 AND o.status = 'COMPLETED' ORDER BY o.id DESC")
//    List<Orders> findCompletedOrdersByUserId(String userId);

    @Query("SELECT o FROM Orders o WHERE o.roomNumber = :roomNumber AND o.status = 'COMPLETED' ORDER BY o.id DESC")
    List<Orders> findCompletedOrdersByRoom(@Param("roomNumber") String roomNumber);
    @Query("SELECT o FROM Orders o WHERE o.userId = ?1 AND o.status = 'COMPLETED' ORDER BY o.id DESC")
    List<Orders> findCompletedOrdersByUserId(String userId);

    // 오늘의 주문 건수
    @Query("SELECT COUNT(DISTINCT o.orderNumber) FROM Orders o")
    Long countTodayOrders();

    // 오늘의 총 매출 (가격 * 수량)
    @Query("SELECT COALESCE(SUM(o.price * o.quantity), 0) FROM Orders o")
    Long sumTodayRevenue();

    // 현재 배달중인 주문 수
    Long countByStatus(String status);
}