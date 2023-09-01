package com.yuxuan66.ecmc.support.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yuxuan66.ecmc.common.utils.Lang;
import com.yuxuan66.ecmc.common.utils.StpUtil;
import com.yuxuan66.ecmc.modules.system.entity.User;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 自动追加基础数据
 *
 * @author Sir丶雨轩
 * @since 2022/12/10
 */
@Component
public class BaseEntityMetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        updateBaseInfo(metaObject, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        updateBaseInfo(metaObject, false);
    }


    /**
     * 更新实体的基础属性
     *
     * @param metaObject 元数据
     * @param isInsert   是否是新增
     */
    private void updateBaseInfo(MetaObject metaObject, boolean isInsert) {
        String timeField = isInsert ? "createTime" : "updateTime";
        String userField = isInsert ? "createBy" : "updateBy";
        String userIdField = isInsert ? "createId" : "updateId";

        if (metaObject.getValue(timeField) == null) {
           if(isInsert){
               this.strictInsertFill(metaObject, timeField, Lang::getNowTimestamp, Timestamp.class);
           }else {
               this.strictUpdateFill(metaObject, timeField, Lang::getNowTimestamp, Timestamp.class);
           }
        }
        User loginUser = null;
        if (StpUtil.isLogin()) {
            loginUser = StpUtil.getUser(User.class);
        }

        User finalLoginUser = loginUser;

        if (metaObject.getValue(userField) == null) {
            if(isInsert){
                this.strictInsertFill(metaObject, userField, () -> finalLoginUser == null ? "系统自动处理" : finalLoginUser.getNickName(), String.class);
            }else{
                this.strictUpdateFill(metaObject, userField, () -> finalLoginUser == null ? "系统自动处理" : finalLoginUser.getNickName(), String.class);
            }

        }
        if (metaObject.getValue(userIdField) == null) {
            if(isInsert){
                this.strictInsertFill(metaObject, userIdField, () -> finalLoginUser == null ? -1L : finalLoginUser.getId(), Long.class);
            }else {
                this.strictUpdateFill(metaObject, userIdField, () -> finalLoginUser == null ? -1L : finalLoginUser.getId(), Long.class);
            }
        }


    }
}
