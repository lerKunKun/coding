package com.biou.project.convert;

import com.biou.project.dto.RoleCreateDTO;
import com.biou.project.entity.Role;
import com.biou.project.vo.RoleVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色转换器
 *
 * @author biou
 * @since 2024-01-01
 */
@Component
public class RoleConvert {

    /**
     * 创建DTO转实体
     *
     * @param dto 创建DTO
     * @return 实体
     */
    public Role toEntity(RoleCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setStatus(1);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setDeleted(0);

        return role;
    }

    /**
     * 实体转VO
     *
     * @param entity 实体
     * @return VO
     */
    public RoleVO toVO(Role entity) {
        if (entity == null) {
            return null;
        }

        RoleVO vo = new RoleVO();
        vo.setId(entity.getId());
        vo.setRoleCode(entity.getRoleCode());
        vo.setRoleName(entity.getRoleName());
        vo.setDescription(entity.getDescription());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());

        return vo;
    }

    /**
     * 实体列表转VO列表
     *
     * @param entities 实体列表
     * @return VO列表
     */
    public List<RoleVO> toVOList(List<Role> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }

        List<RoleVO> voList = new ArrayList<>();
        for (Role entity : entities) {
            voList.add(toVO(entity));
        }
        return voList;
    }

    /**
     * 更新实体（用于部分更新）
     *
     * @param entity 原实体
     * @param dto    更新DTO
     */
    public void updateEntity(Role entity, RoleCreateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getRoleCode() != null) {
            entity.setRoleCode(dto.getRoleCode());
        }
        if (dto.getRoleName() != null) {
            entity.setRoleName(dto.getRoleName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        entity.setUpdateTime(LocalDateTime.now());
    }
} 