package com.biou.project.convert;

import com.biou.project.dto.UserCreateDTO;
import com.biou.project.entity.User;
import com.biou.project.vo.UserVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户转换类
 *
 * @author biou
 * @since 2024-01-01
 */
public class UserConvert {

    /**
     * DTO转Entity
     *
     * @param dto DTO对象
     * @return Entity对象
     */
    public static User dtoToEntity(UserCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);
        return user;
    }

    /**
     * Entity转VO
     *
     * @param entity Entity对象
     * @return VO对象
     */
    public static UserVO entityToVO(User entity) {
        if (entity == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getUsername());
        vo.setEmail(entity.getEmail());
        vo.setPhone(entity.getPhone());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    /**
     * Entity列表转VO列表
     *
     * @param entityList Entity列表
     * @return VO列表
     */
    public static List<UserVO> entityListToVOList(List<User> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<UserVO> voList = new ArrayList<>();
        for (User entity : entityList) {
            voList.add(entityToVO(entity));
        }
        return voList;
    }
} 