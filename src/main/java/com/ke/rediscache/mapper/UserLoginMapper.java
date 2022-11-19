package com.ke.rediscache.mapper;

import com.ke.rediscache.entity.UserLogin;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 针对表【user_login(用户登录表)】的数据库操作Mapper
 *
 * @author stephen
 * @date 2022-11-19 13:16:22
 * @see com.ke.rediscache.entity.UserLogin
 */
@Mapper
public interface UserLoginMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserLogin record);

    int insertSelective(UserLogin record);

    @Select("select * from user_login where id = #{id}")
    UserLogin selectByPrimaryKey(@Param("id") Long id);

    List<UserLogin> selectAll();

    int updateByPrimaryKeySelective(UserLogin record);

    int updateByPrimaryKey(UserLogin record);

}
