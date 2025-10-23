package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.order.Orders;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
import com.jojoldu.book.springboot.web.dto.OrderGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final OrdersRepository ordersRepository;

    @Transactional(readOnly = true)
    public List<OrderGroupDto> getOrdersByStatus(String status) {

        // status가 null이거나 비어있으면 전체 조회
        List<Orders> orders;
        if (status == null || status.isEmpty()) {
            orders = ordersRepository.findAll();
        } else {
            orders = ordersRepository.findByStatusOrderByIdDesc(status);
        }

        // 주문번호로 그룹핑
        Map<String, List<Orders>> grouped = orders.stream()
                .collect(Collectors.groupingBy(Orders::getOrderNumber));

        // DTO로 변환
        return grouped.entrySet().stream()
                .map(entry -> new OrderGroupDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
