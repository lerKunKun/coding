package com.biou.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biou.project.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，已包含以下基础方法：
 * - selectById(Serializable id) - 根据ID查询
 * - selectOne(Wrapper<T> queryWrapper) - 根据条件查询单个
 * - selectList(Wrapper<T> queryWrapper) - 根据条件查询列表
 * - selectPage(IPage<T> page, Wrapper<T> queryWrapper) - 分页查询
 * - insert(T entity) - 插入
 * - updateById(T entity) - 根据ID更新
 * - update(T entity, Wrapper<T> updateWrapper) - 根据条件更新
 * - deleteById(Serializable id) - 根据ID删除
 * - delete(Wrapper<T> queryWrapper) - 根据条件删除
 * - selectCount(Wrapper<T> queryWrapper) - 根据条件统计
 *
 * @author Jax
 * @since 2025-06-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    // 如果需要复杂的自定义SQL查询，可以在这里添加方法并在XML中实现
    // 目前使用MyBatis-Plus的基础方法已经足够满足需求
}