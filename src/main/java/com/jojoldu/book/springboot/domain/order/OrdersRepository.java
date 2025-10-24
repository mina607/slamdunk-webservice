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


    // 전체 주문 건수 세기 (중복 주문번호 제거)
    @Query("SELECT COUNT(DISTINCT o.orderNumber) FROM Orders o")
    Long countTodayOrders();

    // 전체 매출 계산 (가격 × 수량의 합계)
    @Query("SELECT COALESCE(SUM(o.price * o.quantity), 0) FROM Orders o")
    Long sumTodayRevenue();

    // 배달중인 주문 개수 세기
    Long countByStatus(String status);

    // 배달 상태에 따른 주문 조회
//    List<Orders> findByStatus(String status);
    List<Orders> findByStatusOrderByIdDesc(String status);

    // 인기 메뉴 조회 (물품 제외, 음식만)
    @Query("SELECT o.itemName, o.option, o.category, SUM(o.quantity) " +
            "FROM Orders o " +
            "WHERE o.option != 'article' " +  // 물품 제외
            "AND o.option IS NOT NULL " +
            "GROUP BY o.itemName, o.option " +
            "ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findPopularMenus();
}