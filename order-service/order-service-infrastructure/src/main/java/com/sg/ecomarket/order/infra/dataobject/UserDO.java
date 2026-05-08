package com.sg.ecomarket.order.infra.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户数据对象（仅用于权限验证）
 */
@Data
@TableName("user_user")
public class UserDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String role;
}
