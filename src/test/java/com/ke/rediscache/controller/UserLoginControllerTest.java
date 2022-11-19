package com.ke.rediscache.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ke.rediscache.entity.UserLogin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author stephen 2022/11/19 21:32
 */
class UserLoginControllerTest {

    @Test
    void testTranscation() {
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName("1");
        userLogin.setUserPassword("2");
        boolean b = BeanUtil.hasNullField(userLogin);
        Assertions.assertTrue(b);

        UserLogin userLogin1 = new UserLogin();
        userLogin.setUserName("1")
                .setUserPassword("2")
                .setId(1L)
                .setUserPermissions("2022-09-11")
                .setModifyDate("2022-09-11");
        boolean c = BeanUtil.hasNullField(userLogin);
        Assertions.assertFalse(c);
    }
}