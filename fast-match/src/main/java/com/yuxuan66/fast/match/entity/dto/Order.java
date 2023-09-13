package com.yuxuan66.fast.match.entity.dto;

import com.yuxuan66.fast.match.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单数据
 *
 * @author Sir丶雨轩
 * @since 2023/9/11
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 单价 *100 为分
     */
    private Long price;

    /**
     * 手续费单价
     */
    private Long sxfPrice;

    /**
     * 数量s
     */
    private Long num;

    /**
     * 是否是出价单（ 买单true )
     */
    private boolean isBuy;

    /**
     * 订单类型 （ 暂定全为1 ）
     */
    private int orderType;


    /**
     * 总金额
     */
    private Long sumPrice;

    /**
     * 印花税
     */
    private Long taxes;

    /**
     * 股票类型key
     */
    private String key;

    /**
     * 订单的唯一标识
     */
    private String uuid;

    /**
     * 没有成交的数量
     */
    private Long noDealNum;
    /**
     * 没有成交的金额
     */
    private Long noDealAmount;

    /**
     * 成交的数量
     */
    private Long dealNum;
    /**
     * 成交的金额
     */
    private Long dealAmount;

    /**
     * 订单状态，默认为委托中
     */
    private OrderState orderState = OrderState.ORDER;


    public Long getTaxes() {
        if (this.taxes == null) {
            return 0L;
        }
        return this.taxes;
    }


}
