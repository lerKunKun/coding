package com.biou.project.dto;

import java.io.Serializable;

/**
 * 分页查询DTO
 *
 * @author Jax
 * @since 2024-01-01
 */
public class PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，从1开始
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 是否查询总数
     */
    private Boolean searchCount;

    public PageQueryDTO() {
        this.current = 1L;
        this.size = 10L;
        this.searchCount = true;
    }

    public PageQueryDTO(Long current, Long size) {
        this.current = current;
        this.size = size;
        this.searchCount = true;
    }

    public PageQueryDTO(Long current, Long size, Boolean searchCount) {
        this.current = current;
        this.size = size;
        this.searchCount = searchCount;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Boolean searchCount) {
        this.searchCount = searchCount;
    }

    @Override
    public String toString() {
        return "PageQueryDTO{" +
                "current=" + current +
                ", size=" + size +
                ", searchCount=" + searchCount +
                '}';
    }
} 