package com.yuxuan66.fast.match.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Data
@AllArgsConstructor
public class OkOrderResult {

    private Order taker;

    private Order maker;

    private Long dealNum;
}
