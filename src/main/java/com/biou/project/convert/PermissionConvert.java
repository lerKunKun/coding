package com.biou.project.convert;

import com.biou.project.dto.PermissionCreateDTO;
import com.biou.project.entity.Permission;
import com.biou.project.vo.PermissionVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限转换器
 *
 * @author Jax
 * @since 2024-01-01
 */
@Component
public class PermissionConvert {

    /**
     * 创建DTO转实体
     *
     * @param dto 创建DTO
     * @return 实体
     */
    public Permission toEntity(PermissionCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Permission permission = new Permission();
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setResourceType(dto.getResourceType());
        permission.setResourcePath(dto.getResourcePath());
        permission.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        permission.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        permission.setDescription(dto.getDescription());
        permission.setStatus(1);
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permission.setDeleted(0);

        return permission;
    }

    /**
     * 实体转VO
     *
     * @param entity 实体
     * @return VO
     */
    public PermissionVO toVO(Permission entity) {
        if (entity == null) {
            return null;
        }

        PermissionVO vo = new PermissionVO();
        vo.setId(entity.getId());
        vo.setPermissionCode(entity.getPermissionCode());
        vo.setPermissionName(entity.getPermissionName());
        vo.setResourceType(entity.getResourceType());
        vo.setResourcePath(entity.getResourcePath());
        vo.setParentId(entity.getParentId());
        vo.setSortOrder(entity.getSortOrder());
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
    public List<PermissionVO> toVOList(List<Permission> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }

        List<PermissionVO> voList = new ArrayList<>();
        for (Permission entity : entities) {
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
    public void updateEntity(Permission entity, PermissionCreateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getPermissionCode() != null) {
            entity.setPermissionCode(dto.getPermissionCode());
        }
        if (dto.getPermissionName() != null) {
            entity.setPermissionName(dto.getPermissionName());
        }
        if (dto.getResourceType() != null) {
            entity.setResourceType(dto.getResourceType());
        }
        if (dto.getResourcePath() != null) {
            entity.setResourcePath(dto.getResourcePath());
        }
        if (dto.getParentId() != null) {
            entity.setParentId(dto.getParentId());
        }
        if (dto.getSortOrder() != null) {
            entity.setSortOrder(dto.getSortOrder());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        entity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 构建权限树形结构
     *
     * @param permissions 权限列表
     * @return 树形权限列表
     */
    public List<PermissionVO> buildPermissionTree(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }

        List<PermissionVO> permissionVOList = toVOList(permissions);
        List<PermissionVO> tree = new ArrayList<>();

        // 先找到所有根节点
        for (PermissionVO permission : permissionVOList) {
            if (permission.getParentId() == null || permission.getParentId() == 0) {
                tree.add(permission);
            }
        }

        // 为每个根节点递归构建子树
        for (PermissionVO root : tree) {
            buildChildren(root, permissionVOList);
        }

        return tree;
    }

    /**
     * 递归构建子节点
     *
     * @param parent      父节点
     * @param permissions 所有权限
     */
    private void buildChildren(PermissionVO parent, List<PermissionVO> permissions) {
        List<PermissionVO> children = new ArrayList<>();
        for (PermissionVO permission : permissions) {
            if (permission.getParentId() != null && permission.getParentId().equals(parent.getId())) {
                children.add(permission);
                buildChildren(permission, permissions);
            }
        }
        parent.setChildren(children);
    }
} 