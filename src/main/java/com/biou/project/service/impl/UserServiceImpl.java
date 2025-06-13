package com.biou.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biou.project.convert.UserConvert;
import com.biou.project.dto.PageQueryDTO;
import com.biou.project.dto.UserCreateDTO;
import com.biou.project.dto.UserQueryDTO;
import com.biou.project.entity.User;
import com.biou.project.exception.BusinessException;
import com.biou.project.mapper.UserMapper;
import com.biou.project.repository.UserRepository;
import com.biou.project.service.UserService;
import com.biou.project.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户Service实现类
 *
 * @author Jax
 * @since 2025-06-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String USER_CACHE_KEY = "user:";
    private static final long CACHE_EXPIRE_TIME = 30;

    @Override
    public UserVO createUser(UserCreateDTO createDTO) {
        logger.info("创建用户: {}", createDTO);

        // 校验用户名是否已存在
        if (checkUsernameExists(createDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 校验邮箱是否已存在
        if (checkEmailExists(createDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }

        // 校验手机号是否已存在
        if (checkPhoneExists(createDTO.getPhone())) {
            throw new BusinessException("手机号已存在");
        }

        // 转换DTO为Entity
        User user = UserConvert.dtoToEntity(createDTO);
        
        // 加密密码
        user.setPassword(encodePassword(createDTO.getPassword()));

        // 保存用户
        boolean saved = userRepository.save(user);
        if (!saved) {
            throw new BusinessException("创建用户失败");
        }

        // 转换Entity为VO
        UserVO userVO = UserConvert.entityToVO(user);

        // 缓存用户信息
        String cacheKey = USER_CACHE_KEY + user.getId();
        redisTemplate.opsForValue().set(cacheKey, userVO, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);

        logger.info("用户创建成功: {}", userVO);
        return userVO;
    }

    @Override
    public UserVO getUserById(Long id) {
        logger.debug("根据ID查询用户: {}", id);

        // 先从缓存中查询
        String cacheKey = USER_CACHE_KEY + id;
        UserVO userVO = (UserVO) redisTemplate.opsForValue().get(cacheKey);
        if (userVO != null) {
            logger.debug("从缓存中获取用户信息: {}", userVO);
            return userVO;
        }

        // 缓存中没有，从数据库查询
        User user = userRepository.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }

        userVO = UserConvert.entityToVO(user);

        // 存入缓存
        redisTemplate.opsForValue().set(cacheKey, userVO, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);

        return userVO;
    }

    @Override
    public UserVO getUserByUsername(String username) {
        logger.debug("根据用户名查询用户: {}", username);

        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUsername(username);
        queryDTO.setDeleted(0);
        
        User user = userRepository.findOne(queryDTO);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return UserConvert.entityToVO(user);
    }

    @Override
    public IPage<UserVO> getUserPage(Page<UserVO> page) {
        logger.debug("分页查询用户列表: 当前页={}, 每页大小={}", page.getCurrent(), page.getSize());

        PageQueryDTO pageDTO = new PageQueryDTO(page.getCurrent(), page.getSize());
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setDeleted(0);
        queryDTO.setOrderBy("createTime");
        queryDTO.setOrderDirection("DESC");

        IPage<User> userPage = userRepository.page(pageDTO, queryDTO);
        
        // 转换分页结果
        IPage<UserVO> voPage = userPage.convert(UserConvert::entityToVO);
        
        return voPage;
    }

    @Override
    public List<UserVO> getEnabledUsers() {
        logger.debug("查询启用状态的用户列表");

        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setStatus(1);
        queryDTO.setDeleted(0);
        queryDTO.setOrderBy("createTime");
        queryDTO.setOrderDirection("DESC");

        List<User> userList = userRepository.list(queryDTO);
        return UserConvert.entityListToVOList(userList);
    }

    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        logger.info("更新用户状态: id={}, status={}", id, status);

        User user = userRepository.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }

        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());

        boolean updated = userRepository.updateById(user);
        if (updated) {
            // 删除缓存
            String cacheKey = USER_CACHE_KEY + id;
            redisTemplate.delete(cacheKey);
        }

        return updated;
    }

    @Override
    public boolean deleteUser(Long id) {
        logger.info("删除用户: id={}", id);

        User user = userRepository.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户不存在");
        }

        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());

        boolean deleted = userRepository.updateById(user);
        if (deleted) {
            // 删除缓存
            String cacheKey = USER_CACHE_KEY + id;
            redisTemplate.delete(cacheKey);
        }

        return deleted;
    }

    @Override
    public long[] getUserStatusCount() {
        logger.debug("统计各状态用户数量");

        UserQueryDTO enabledQuery = new UserQueryDTO();
        enabledQuery.setStatus(1);
        enabledQuery.setDeleted(0);
        long enabledCount = userRepository.count(enabledQuery);

        UserQueryDTO disabledQuery = new UserQueryDTO();
        disabledQuery.setStatus(0);
        disabledQuery.setDeleted(0);
        long disabledCount = userRepository.count(disabledQuery);

        return new long[]{enabledCount, disabledCount};
    }

    @Override
    public boolean checkUsernameExists(String username) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUsername(username);
        queryDTO.setDeleted(0);
        return userRepository.count(queryDTO) > 0;
    }

    @Override
    public boolean checkEmailExists(String email) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setEmail(email);
        queryDTO.setDeleted(0);
        return userRepository.count(queryDTO) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setPhone(phone);
        queryDTO.setDeleted(0);
        return userRepository.count(queryDTO) > 0;
    }

    @Override
    public User findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                    .eq(User::getDeleted, 0);
        
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public User findByDingtalkUnionId(String unionId) {
        if (!StringUtils.hasText(unionId)) {
            return null;
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDingtalkUnionId, unionId)
                    .eq(User::getDeleted, 0);
        
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public String encodePassword(String rawPassword) {
        if (!StringUtils.hasText(rawPassword)) {
            throw new BusinessException("密码不能为空");
        }
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 