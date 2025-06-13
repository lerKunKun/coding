package com.biou.project.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 权限创建DTO
 *
 * @author Jax
 * @since 2025-06-14
 */
public class PermissionCreateDTO {

    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    private String permissionCode;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称长度不能超过100个字符")
    private String permissionName;

    /**
     * 资源类型：menu-菜单，button-按钮，api-接口
     */
    @NotBlank(message = "资源类型不能为空")
    private String resourceType;

    /**
     * 资源路径
     */
    @Size(max = 200, message = "资源路径长度不能超过200个字符")
    private String resourcePath;

    /**
     * 父权限ID，0表示顶级权限
     */
    private Long parentId;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 权限描述
     */
    @Size(max = 200, message = "权限描述长度不能超过200个字符")
    private String description;

    public PermissionCreateDTO() {
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PermissionCreateDTO{" +
                "permissionCode='" + permissionCode + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourcePath='" + resourcePath + '\'' +
                ", parentId=" + parentId +
                ", sortOrder=" + sortOrder +
                ", description='" + description + '\'' +
                '}';
    }
} 