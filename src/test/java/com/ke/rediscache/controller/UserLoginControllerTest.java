package com.ke.rediscache.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ke.rediscache.entity.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

/**
 * @author stephen 2022/11/19 21:32
 */
@Slf4j
@SuppressWarnings("FieldCanBeLocal")
@DisplayName("用户登录测试")
class UserLoginControllerTest {

    private long timeBefore;

    private long timeAfter;

    @BeforeEach
    public void timeBefore() {
        timeBefore = System.currentTimeMillis();
    }

    @AfterEach
    public void time() {
        timeAfter = System.currentTimeMillis();
        log.info("执行时间为：{}", timeBefore - timeAfter);
    }

    @Test
    @DisplayName("传入参数校验")
    @Timeout(value = 20, unit = TimeUnit.MILLISECONDS)
    void testTranscation() throws InterruptedException {
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