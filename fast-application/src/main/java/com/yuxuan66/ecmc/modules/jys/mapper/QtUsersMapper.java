package com.yuxuan66.ecmc.modules.jys.mapper;

import com.yuxuan66.ecmc.modules.jys.entity.QtUsers;
import com.yuxuan66.ecmc.support.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2023/9/15
 */
@Mapper
public interface QtUsersMapper extends BaseMapper<QtUsers> {

    /**
     * 根据用户id查询真实姓名
     * @param userId 用户id
     * @return 真实姓名
     */
    @Select("select real_name from dm_qt_user_person where user_id = #{userId}")
    String getRealName(Integer userId);

    /**
     * 查询用户出金总数
     * @param userId 用户ID
     * @return 出金总数
     */
    @Select("SELECT SUM(money) FROM dm_bk_users_withdrawal WHERE `status`= 5 AND user_id=#{userId}")
    BigDecimal sumWithdrawal(Integer userId);

    /**
     * 查询用户入金总数
     * @param userId 用户ID
     * @return 入金总数
     */
    @Select("SELECT SUM(money) FROM dm_bk_users_recharge WHERE `status`= 5 AND user_id=#{userId}")
    BigDecimal sumRecharge(Integer userId);

    /**
     * 用户充值资产
     * @param userId 用户ID
     * @return 充值资产
     */
    @Select("SELECT market_id,SUM(num) num FROM dm_qt_users_recharge_market WHERE `status`= 2 AND user_id= #{userId} GROUP BY market_id")
    List<Map<String,Object>> sumRechargeMarket(Integer userId);


}
