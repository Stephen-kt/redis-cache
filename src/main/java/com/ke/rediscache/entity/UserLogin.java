package com.ke.rediscache.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户登录表
 *
 * @TableName user_login
 */
@Data
public class UserLogin implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键 id
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 用户权限
     */
    private String userPermissions;
    /**
     * 修改时间
     */
    private String modifyDate;


}