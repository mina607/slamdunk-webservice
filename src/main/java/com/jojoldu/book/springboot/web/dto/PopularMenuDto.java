package com.jojoldu.book.springboot.web.dto;

import lombok.Getter;

@Getter
public class PopularMenuDto {
    private final int rank;
    private final String menuName;
    private final String category;
    private final Long orderCount;
    private final boolean isTop3;

    public PopularMenuDto(int rank, String menuName, String category, Long orderCount) {
        this.rank = rank;
        this.menuName = menuName;
        this.category = category;
        this.orderCount = orderCount;
        this.isTop3 = rank <= 3;
    }
}