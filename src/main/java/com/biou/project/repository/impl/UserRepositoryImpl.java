package com.biou.project.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biou.project.dto.PageQueryDTO;
import com.biou.project.dto.UserQueryDTO;
import com.biou.project.entity.User;
import com.biou.project.mapper.UserMapper;
import com.biou.project.repository.UserRepository;
import com.biou.project.utils.QueryWrapperUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Repository实现类
 *
 * @author Jax
 * @since 2024-01-01
 */
@Repository
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> implements UserRepository {

    @Override
    public User findOne(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<User> list(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<User> page(PageQueryDTO pageDTO, UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        Page<User> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize(), pageDTO.getSearchCount());
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public long count(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public boolean update(User entity, UserQueryDTO queryDTO) {
        LambdaUpdateWrapper<User> wrapper = QueryWrapperUtils.buildUserUpdateWrapper(queryDTO);
        int result = baseMapper.update(entity, wrapper);
        return result > 0;
    }

    @Override
    public boolean remove(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        int result = baseMapper.delete(wrapper);
        return result > 0;
    }
} 