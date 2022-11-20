package com.ke.rediscache.controller;

import com.ke.rediscache.constant.Redis;
import com.ke.rediscache.dao.UserLoginDAO;
import com.ke.rediscache.entity.UserLogin;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stephen 2022/11/19 13:30
 */
@Slf4j
@RestController
public class UserLoginController {

    public final UserLoginDAO userLoginDAO;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserLoginController(RedisTemplate<String, Object> redisTemplate, UserLoginDAO userLoginDAO) {
        this.redisTemplate = redisTemplate;
        this.userLoginDAO = userLoginDAO;
    }

    @PostMapping("selectAll")
    public ResponseEntity<?> selectAll() {
        final List<UserLogin> userLogins = userLoginDAO.selectAll();
        return ResponseEntity.ok(userLogins);
    }

    @GetMapping("queryById")
    public ResponseEntity<?> getUserById(Long id) {
        final UserLogin userLogin = userLoginDAO.selectByPrimaryKey(id);
        return ResponseEntity.ok(userLogin);
    }

    @Transactional(transactionManager = "mysqlTransactionManager",
        rollbackFor = Exception.class,
        propagation = Propagation.NESTED
    )
    @PostMapping("insert")
    public ResponseEntity<?> insert(@RequestBody UserLogin userLogin) {
        userLoginDAO.insert(userLogin);
        return ResponseEntity.ok().build();
    }


    @PostMapping("updateBySeclective")
    public ResponseEntity<?> updateBySelective(@RequestBody UserLogin userLogin) {
        final UserLogin userLogins = userLoginDAO.updateByPrimaryKeySelective(userLogin);
        return ResponseEntity.ok(userLogins);
    }

    @PostMapping("deserial")
    public List<Object> deserial() {
        String redisKey = Redis.PREFIX.getKey() + Redis.User.getKey();
        final List<Object> userLogins;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            userLogins = Collections.singletonList(redisTemplate.opsForValue().get(redisKey));
        } else {
            userLogins = Collections.singletonList(userLoginDAO.selectAll());
        }
        userLogins.forEach(System.out::println);
        return userLogins;
    }

    @PostMapping("testTranscation")
    public void testTranscation(@RequestBody UserLogin userLogin) throws Exception {
        log.info("入参为：{}", userLogin);
        userLoginDAO.updateByPrimaryKeySelective(userLogin);
        userLoginDAO.deleteByPrimaryKey(1L);
        final List<UserLogin> userLogins = userLoginDAO.selectAll();
        log.info("此时数据位：{}", userLogins);
        throw new Exception("哎呀，抛异常啦");
    }

}
