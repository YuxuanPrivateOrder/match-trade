package com.yuxuan66.fast.match.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * Order State
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Getter
@AllArgsConstructor
public enum OrderState {

    // 订单状态
    ERROR(-1, "委托异常"),
    REQUEST(0, "委托请求"),
    ORDER(1, "委托中"),
    SOME_DEAL(2, "部分成交 "),
    ALL_DEAL(3, "全部成交"),
    CANCEL(4, "已撤销");

    private final int code;
    private final String desc;

    public static Optional<OrderState> of(int code) {
        return Arrays.stream(values()).filter(i -> i.code == code).findFirst();
    }
}
