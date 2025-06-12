package com.biou.project.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.biou.project.dto.UserQueryDTO;
import com.biou.project.entity.User;
import org.springframework.util.StringUtils;

/**
 * QueryWrapper工具类
 * 负责将DTO转换为MyBatis-Plus的QueryWrapper
 *
 * @author biou
 * @since 2024-01-01
 */
public class QueryWrapperUtils {

    /**
     * 将UserQueryDTO转换为LambdaQueryWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaQueryWrapper
     */
    public static LambdaQueryWrapper<User> buildUserQueryWrapper(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (queryDTO == null) {
            return wrapper;
        }

        // ID条件
        if (queryDTO.getId() != null) {
            wrapper.eq(User::getId, queryDTO.getId());
        }

        // 用户名条件
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.eq(User::getUsername, queryDTO.getUsername());
        }

        // 邮箱条件
        if (StringUtils.hasText(queryDTO.getEmail())) {
            wrapper.eq(User::getEmail, queryDTO.getEmail());
        }

        // 手机号条件
        if (StringUtils.hasText(queryDTO.getPhone())) {
            wrapper.eq(User::getPhone, queryDTO.getPhone());
        }

        // 状态条件
        if (queryDTO.getStatus() != null) {
            wrapper.eq(User::getStatus, queryDTO.getStatus());
        }

        // 删除状态条件
        if (queryDTO.getDeleted() != null) {
            wrapper.eq(User::getDeleted, queryDTO.getDeleted());
        }

        // 创建时间范围
        if (queryDTO.getCreateTimeStart() != null) {
            wrapper.ge(User::getCreateTime, queryDTO.getCreateTimeStart());
        }
        if (queryDTO.getCreateTimeEnd() != null) {
            wrapper.le(User::getCreateTime, queryDTO.getCreateTimeEnd());
        }

        // 排序
        if (StringUtils.hasText(queryDTO.getOrderBy())) {
            boolean isAsc = !"DESC".equalsIgnoreCase(queryDTO.getOrderDirection());
            
            switch (queryDTO.getOrderBy().toLowerCase()) {
                case "id":
                    wrapper.orderBy(true, isAsc, User::getId);
                    break;
                case "username":
                    wrapper.orderBy(true, isAsc, User::getUsername);
                    break;
                case "email":
                    wrapper.orderBy(true, isAsc, User::getEmail);
                    break;
                case "status":
                    wrapper.orderBy(true, isAsc, User::getStatus);
                    break;
                case "createtime":
                case "create_time":
                    wrapper.orderBy(true, isAsc, User::getCreateTime);
                    break;
                case "updatetime":
                case "update_time":
                    wrapper.orderBy(true, isAsc, User::getUpdateTime);
                    break;
                default:
                    // 默认按创建时间倒序
                    wrapper.orderByDesc(User::getCreateTime);
                    break;
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc(User::getCreateTime);
        }

        return wrapper;
    }

    /**
     * 将UserQueryDTO转换为LambdaUpdateWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaUpdateWrapper
     */
    public static LambdaUpdateWrapper<User> buildUserUpdateWrapper(UserQueryDTO queryDTO) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        
        if (queryDTO == null) {
            return wrapper;
        }

        // ID条件
        if (queryDTO.getId() != null) {
            wrapper.eq(User::getId, queryDTO.getId());
        }

        // 用户名条件
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.eq(User::getUsername, queryDTO.getUsername());
        }

        // 邮箱条件
        if (StringUtils.hasText(queryDTO.getEmail())) {
            wrapper.eq(User::getEmail, queryDTO.getEmail());
        }

        // 手机号条件
        if (StringUtils.hasText(queryDTO.getPhone())) {
            wrapper.eq(User::getPhone, queryDTO.getPhone());
        }

        // 状态条件
        if (queryDTO.getStatus() != null) {
            wrapper.eq(User::getStatus, queryDTO.getStatus());
        }

        // 删除状态条件
        if (queryDTO.getDeleted() != null) {
            wrapper.eq(User::getDeleted, queryDTO.getDeleted());
        }

        // 创建时间范围
        if (queryDTO.getCreateTimeStart() != null) {
            wrapper.ge(User::getCreateTime, queryDTO.getCreateTimeStart());
        }
        if (queryDTO.getCreateTimeEnd() != null) {
            wrapper.le(User::getCreateTime, queryDTO.getCreateTimeEnd());
        }

        return wrapper;
    }
} 