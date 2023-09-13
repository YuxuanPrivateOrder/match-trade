package com.yuxuan66.fast.match.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/***
 * Order type collection
 * @author kinbug 
 */
@Getter
@AllArgsConstructor
public enum OrderType {

	/* 订单类型 */
	GTC(1, "正常的限价单"),
	MTC(2, "市价转撤销，无对手价时撤销");

	private final int code;
	private final String desc;



	public static OrderType of(int code) {
		Optional<OrderType> optional =  Arrays.stream(values()).filter(i -> i.code == code).findFirst();
		return optional.orElse(null);
	}
}
