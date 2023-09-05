package com.yuxuan66.ecmc.modules.system.mapper;

import com.yuxuan66.ecmc.modules.system.entity.Config;
import com.yuxuan66.ecmc.support.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统-配置表(SysConfig)表数据库访问层
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
}
