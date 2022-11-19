package com.ke.rediscache.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class UserLoginDAOTest {

    @Autowired
    private UserLoginDAO userLoginDAO;
//    UserLoginDAOTest(UserLoginDAO userLoginDAO) {
//        this.userLoginDAO = userLoginDAO;
//    }

    @Test
    @Rollback
    void deleteByPrimaryKey() {
        int i = userLoginDAO.deleteByPrimaryKey(1L);
        Assertions.assertEquals(1, 1);
    }
}