package com.biou.project.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biou.project.dto.PageQueryDTO;
import com.biou.project.dto.UserQueryDTO;
import com.biou.project.entity.User;

import java.util.List;

/**
 * 用户Repository接口
 * 
 * 继承MyBatis-Plus的IService，已包含以下通用方法：
 * - getById(Serializable id) - 根据ID查询
 * - save(T entity) - 保存
 * - saveBatch(Collection<T> entityList) - 批量保存
 * - updateById(T entity) - 根据ID更新
 * - removeById(Serializable id) - 根据ID删除
 * 
 * 自定义方法使用DTO封装查询条件，不向上层暴露ORM框架的具体实现
 *
 * @author Jax
 * @since 2025-06-14
 */
public interface UserRepository extends IService<User> {

    /**
     * 根据条件查询单个用户
     *
     * @param queryDTO 查询条件
     * @return 用户信息，如果找到多个则返回第一个，如果没找到则返回null
     */
    User findOne(UserQueryDTO queryDTO);

    /**
     * 根据条件查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 用户列表
     */
    List<User> list(UserQueryDTO queryDTO);

    /**
     * 根据条件分页查询用户
     *
     * @param pageDTO 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<User> page(PageQueryDTO pageDTO, UserQueryDTO queryDTO);

    /**
     * 根据条件统计用户数量
     *
     * @param queryDTO 查询条件
     * @return 用户数量
     */
    long count(UserQueryDTO queryDTO);

    /**
     * 根据条件更新用户
     *
     * @param entity 更新内容
     * @param queryDTO 更新条件
     * @return 是否成功
     */
    boolean update(User entity, UserQueryDTO queryDTO);

    /**
     * 根据条件删除用户
     *
     * @param queryDTO 删除条件
     * @return 是否成功
     */
    boolean remove(UserQueryDTO queryDTO);
} 