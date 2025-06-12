package com.biou.project.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色权限分配DTO
 *
 * @author biou
 * @since 2024-01-01
 */
public class RolePermissionDTO {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 权限ID列表
     */
    @NotEmpty(message = "权限列表不能为空")
    private List<Long> permissionIds;

    public RolePermissionDTO() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Override
    public String toString() {
        return "RolePermissionDTO{" +
                "roleId=" + roleId +
                ", permissionIds=" + permissionIds +
                '}';
    }
} 