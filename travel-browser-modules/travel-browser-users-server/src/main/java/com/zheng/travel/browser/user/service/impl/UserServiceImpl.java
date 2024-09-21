package com.zheng.travel.browser.user.service.impl;

import com.zheng.travel.browser.auth.config.JwtProperties;
import com.zheng.travel.browser.auth.util.AuthenticationUtils;
import com.zheng.travel.browser.core.exception.BusinessException;
import com.zheng.travel.browser.core.utils.Md5Utils;
import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.redis.utils.RedisCache;
import com.zheng.travel.browser.user.domain.UserInfo;
import com.zheng.travel.browser.user.dto.UserInfoDTO;
import com.zheng.travel.browser.user.mapper.UserInfoMapper;
import com.zheng.travel.browser.user.redis.key.UserRedisKeyPrefix;
import com.zheng.travel.browser.user.service.UserInfoService;
import com.zheng.travel.browser.user.vo.LoginUser;
import com.zheng.travel.browser.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisCache redisCache;
    private final JwtProperties jwtProperties;

    public UserServiceImpl(RedisCache redisCache, JwtProperties jwtProperties) {
        this.redisCache = redisCache;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 注册业务
     * @param req 注册请求对象
     */
    @Override
    public void register(RegisterRequest req) {
        // 1. 基于手机号查询是否已经存在该手机号, 如果存在则返回异常
        UserInfo userInfo = this.findByPhone(req.getPhone());
        if (userInfo != null) {
            throw new BusinessException(R.CODE_REGISTER_ERROR, "手机号已存在, 请不要重复注册");
        }
        // 2. 从 redis 中获取验证码, 与前端传入的验证码进行校验是否一致, 如果不一致则抛出异常
        String fullKey = UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING.fullKey(req.getPhone());
        String code = redisCache.getCacheObject(fullKey);
        if (!req.getVerifyCode().equalsIgnoreCase(code)) {
            throw new BusinessException(R.CODE_REGISTER_ERROR, "验证码错误");
        }
        // 3. 将验证码从 redis 中删除
        redisCache.deleteObject(fullKey);
        // 4. 创建用户对象, 填入参数并补充其他默认值
        userInfo = this.buildUserInfo(req);
        // 5. 对密码进行加密操作
        // 加盐 + 散列(hash)次数
        String encryptPassword = Md5Utils.getMD5(userInfo.getPassword() + userInfo.getPhone());
        userInfo.setPassword(encryptPassword);
        // 6. 保存用户对象到数据库
        super.save(userInfo);
    }


    /**
     * 登录业务
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public Map<String, Object> login(String username, String password) {
        // 1. 基于用户名查询用户对象, 如果为空直接抛出异常
        UserInfo userInfo = this.findByPhone(username);
        if (userInfo == null) {
            throw new BusinessException(500401, "用户名或密码错误");
        }
        // 2. 对前端传入的密码进行加密
        String encryptPassword = Md5Utils.getMD5(password + username);
        // 3. 校验前端密码与数据库密码是否一致, 如果不一致直接抛出异常
        if (!encryptPassword.equalsIgnoreCase(userInfo.getPassword())) {
            throw new BusinessException(500401, "用户名或密码错误");
        }

        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userInfo, loginUser);

        // 当前时间
        long now = System.currentTimeMillis();
        loginUser.setLoginTime(now);
        // 过期时间
        long expireTime = jwtProperties.getExpireTime() * LoginUser.MINUTES_MILLISECONDS;
        long fullExpireTime = now + expireTime;
        loginUser.setExpireTime(fullExpireTime);

        // 生成一个用户存入 redis 的唯一标识
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        UserRedisKeyPrefix loginInfoString = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING;
        loginInfoString.setTimeout(expireTime);
        loginInfoString.setUnit(TimeUnit.MILLISECONDS);
        redisCache.setCacheObject(loginInfoString, loginUser, uuid);

        // 4. 使用 JWT 生成 token, 往 JWT 中存入 用户基础信息
        Map<String, Object> payload = new HashMap<>();
        payload.put(LoginUser.LOGIN_USER_REDIS_UUID, uuid);

        String jwtToken = Jwts.builder().addClaims(payload)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();

        // 5. 构建 Map 对象, 存入 token 以及用户对象, 返回给前端
        payload.clear();
        payload.put("token", jwtToken);
        payload.put("user", loginUser);
        return payload;
    }


    /**
     * 查找手机号业务
     * @param phone 手机号
     * @return
     */
    @Override
    public UserInfo findByPhone(String phone) {
        // WHERE phone = #{phone}
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>()
                .eq("phone", phone);
        return getOne(wrapper);
    }

    /**
     * 获取用户信息业务实现
     * @param id
     * @return
     */
    @Override
    public UserInfoDTO getDtoById(Long id) {
        UserInfo userInfo = super.getById(id);
        if (userInfo != null) {
            UserInfoDTO dto = new UserInfoDTO();
            BeanUtils.copyProperties(userInfo, dto);
            return dto;
        }
        return null;
    }

    @Override
    public List<Long> getFavorStrategyIdList(Long userId) {
        List<Long> list = getBaseMapper().selectFavorStrategyIdList(userId);
        return list;
    }

    @Override
    public boolean favoriteStrategy(Long sid) {
        // 1. 获取当前登录的用户
        LoginUser user = AuthenticationUtils.getUser();
        // 2. 获取当前用户收藏的文章列表
        List<Long> list = this.getFavorStrategyIdList(user.getId());
        // 3. 判断当前用户是否已经收藏过该文章
        if (list.contains(sid)) {
            // 4. 收藏过 => 取消收藏, 数量-1
            getBaseMapper().deleteFavorStrategy(user.getId(), sid);
            redisCache.hashIncrement(UserRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "favornum",
                    -1, sid + "");
            return false;
        }

        // 5. 没收藏 => 收藏, 数量+1
        getBaseMapper().insertFavorStrategy(user.getId(), sid);
        redisCache.hashIncrement(UserRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "favornum",
                1, sid + "");
        return true;
    }

    private UserInfo buildUserInfo(RegisterRequest req) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(req, userInfo);
        userInfo.setInfo("这个人很懒, 什么都没写.");
        userInfo.setHeadImgUrl("/images/default.jpg");
        // 假如没有使用请求对象接收参数, 而是使用完整的用户对象接收参数
        // 记得一定要重新设置关键参数的默认值, 不能任由前端传递, 会出现数据被覆盖的安全问题
        userInfo.setState(UserInfo.STATE_NORMAL);
        return userInfo;
    }
}
