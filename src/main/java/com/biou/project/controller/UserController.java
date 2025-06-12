package com.biou.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biou.project.dto.UserCreateDTO;
import com.biou.project.service.UserService;
import com.biou.project.vo.Result;
import com.biou.project.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author biou
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 创建用户
     *
     * @param createDTO 创建用户DTO
     * @return 用户信息
     */
    @PostMapping
    public Result<UserVO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        logger.info("创建用户请求: {}", createDTO);
        UserVO userVO = userService.createUser(createDTO);
        return Result.success("用户创建成功", userVO);
    }

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable @NotNull @Min(1) Long id) {
        logger.debug("查询用户: id={}", id);
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public Result<UserVO> getUserByUsername(@PathVariable String username) {
        logger.debug("根据用户名查询用户: username={}", username);
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success(userVO);
    }

    /**
     * 分页查询用户列表
     *
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<UserVO>> getUserPage(
            @RequestParam(defaultValue = "1") @Min(1) Long current,
            @RequestParam(defaultValue = "10") @Min(1) Long size) {
        logger.debug("分页查询用户: current={}, size={}", current, size);
        
        Page<UserVO> page = new Page<>(current, size);
        IPage<UserVO> userPage = userService.getUserPage(page);
        return Result.success(userPage);
    }

    /**
     * 查询启用状态的用户列表
     *
     * @return 用户列表
     */
    @GetMapping("/enabled")
    public Result<List<UserVO>> getEnabledUsers() {
        logger.debug("查询启用状态的用户列表");
        List<UserVO> userList = userService.getEnabledUsers();
        return Result.success(userList);
    }

    /**
     * 更新用户状态
     *
     * @param id 用户ID
     * @param status 状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Object> updateUserStatus(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam @NotNull Integer status) {
        logger.info("更新用户状态: id={}, status={}", id, status);
        
        if (status != 0 && status != 1) {
            return Result.error("状态值无效，只能是0或1");
        }
        
        boolean success = userService.updateUserStatus(id, status);
        if (success) {
            return Result.success("状态更新成功");
        } else {
            return Result.error("状态更新失败");
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Object> deleteUser(@PathVariable @NotNull @Min(1) Long id) {
        logger.info("删除用户: id={}", id);
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }

    /**
     * 统计用户状态分布
     *
     * @return 统计结果
     */
    @GetMapping("/statistics")
    public Result<Map<String, Long>> getUserStatistics() {
        logger.debug("统计用户状态分布");
        long[] counts = userService.getUserStatusCount();
        
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("enabled", counts[0]);
        statistics.put("disabled", counts[1]);
        statistics.put("total", counts[0] + counts[1]);
        
        return Result.success(statistics);
    }

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check/username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        logger.debug("检查用户名是否存在: username={}", username);
        boolean exists = userService.checkUsernameExists(username);
        return Result.success(exists);
    }

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/check/email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        logger.debug("检查邮箱是否存在: email={}", email);
        boolean exists = userService.checkEmailExists(email);
        return Result.success(exists);
    }

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 检查结果
     */
    @GetMapping("/check/phone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        logger.debug("检查手机号是否存在: phone={}", phone);
        boolean exists = userService.checkPhoneExists(phone);
        return Result.success(exists);
    }
} 