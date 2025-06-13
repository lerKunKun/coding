package com.biou.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biou.project.dto.UserCreateDTO;
import com.biou.project.entity.User;
import com.biou.project.vo.UserVO;

import java.util.List;

/**
 * 用户Service接口
 *
 * @author Jax
 * @since 2024-01-01
 */
public interface UserService extends IService<User> {

    /**
     * 创建用户
     *
     * @param createDTO 创建用户DTO
     * @return 用户VO
     */
    UserVO createUser(UserCreateDTO createDTO);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户VO
     */
    UserVO getUserByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<UserVO> getUserPage(Page<UserVO> page);

    /**
     * 查询所有启用状态的用户
     *
     * @return 用户VO列表
     */
    List<UserVO> getEnabledUsers();

    /**
     * 更新用户状态
     *
     * @param id 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long id, Integer status);

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 统计各状态用户数量
     *
     * @return 统计结果
     */
    long[] getUserStatusCount();

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone);

    /**
     * 根据用户名查询用户实体
     *
     * @param username 用户名
     * @return 用户实体
     */
    User findByUsername(String username);

    /**
     * 根据钉钉UnionId查询用户
     *
     * @param unionId 钉钉UnionId
     * @return 用户实体
     */
    User findByDingtalkUnionId(String unionId);

    /**
     * 加密用户密码
     *
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    String encodePassword(String rawPassword);

    /**
     * 验证密码是否匹配
     *
     * @param rawPassword 明文密码
     * @param encodedPassword 加密密码
     * @return 是否匹配
     */
    boolean matches(String rawPassword, String encodedPassword);
} 