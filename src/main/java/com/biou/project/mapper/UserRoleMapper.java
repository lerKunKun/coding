package com.biou.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biou.project.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 *
 * @author biou
 * @since 2024-01-01
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 删除记录数
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除用户角色关联
     *
     * @param roleId 角色ID
     * @return 删除记录数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入用户角色关联
     *
     * @param userRoles 用户角色关联列表
     * @return 插入记录数
     */
    int batchInsert(@Param("userRoles") List<UserRole> userRoles);

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
} 