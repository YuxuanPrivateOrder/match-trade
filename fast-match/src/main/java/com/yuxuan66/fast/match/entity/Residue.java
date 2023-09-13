package com.yuxuan66.fast.match.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.ecmc.support.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Data
@TableName("gx_residue")
public class Residue extends BaseEntity<Residue> {
    /**
     * 总金额
     */
    private BigDecimal total;
    /**
     * 剩余金额
     */
    private BigDecimal surplus;
}
