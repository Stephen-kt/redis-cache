package com.ke.rediscache.dao;

import com.ke.rediscache.constant.Redis;
import com.ke.rediscache.entity.UserLogin;
import com.ke.rediscache.mapper.UserLoginMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author stephen 2022/11/19 13:25
 */
@SuppressWarnings("ALL")
@Slf4j
@Repository
@CacheConfig(cacheNames = "UserLogin")
public class UserLoginDAO {

    private final UserLoginMapper userLoginMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public UserLoginDAO(UserLoginMapper userLoginMapper, RedisTemplate<String, Object> redisTemplate) {
        this.userLoginMapper = userLoginMapper;
        this.redisTemplate = redisTemplate;
    }


    @Cacheable(key = "'id-' + #id")
    public UserLogin selectByPrimaryKey(Long id) {
        System.out.println("selectByPrimaryKey被执行了");
        return userLoginMapper.selectByPrimaryKey(id);
    }

    @CacheEvict(key = "'id-' + #id")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public int deleteByPrimaryKey(Long id) throws Exception {
        System.out.println("deleteByPrimaryKey被执行了");
        redisTemplate.delete(Redis.PREFIX.getKey() + Redis.User.getKey());
        //noinspection UnnecessaryLocalVariable
        final int deleteOrNot = userLoginMapper.deleteByPrimaryKey(id);
        // if (deleteOrNot == 1) {
        //    throw new Exception("删除异常啦");
        //}
        return deleteOrNot;
    }

    @CachePut(key = "'id-' + #userLogin.id")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public UserLogin updateByPrimaryKeySelective(UserLogin userLogin) {
        System.out.println("updateByPrimaryKeySelective被执行了");
        userLoginMapper.updateByPrimaryKeySelective(userLogin);
        redisTemplate.delete(Redis.PREFIX.getKey() + Redis.User.getKey());
        return userLoginMapper.selectByPrimaryKey(userLogin.getId());
    }

    @CacheEvict(key = "'id-' + #userLogin.id")
    public int insert(UserLogin userLogin) {
        System.out.println("insert被执行了");
        redisTemplate.delete(Redis.PREFIX.getKey() + Redis.User.getKey());
        return userLoginMapper.insert(userLogin);
    }

    @Cacheable(key = "'AllUser'")
    public List<UserLogin> selectAll() {
        System.out.println("selectAll被执行了");
        return userLoginMapper.selectAll();

    }
}
